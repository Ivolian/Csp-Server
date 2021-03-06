package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Identities;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.List;


@Service
@Transactional
public class CourtDataService extends BaseService {

    @Value("${temp.path}")
    private String tempPath;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    //

    public JSONObject exportExcel(String fileName, List<String> courtIdList, String departmentId, Boolean underling, String beginTime, String endTime) throws Exception {

        List<Object[]> resultList = getResultList(courtIdList, departmentId, beginTime, endTime);
        HSSFWorkbook hssfWorkbook = createExcel(resultList);

        String tempFileName = Identities.uuid();
        String tempFilePath = getTempPath() + "/" + tempFileName;
        File tempFile = new File(tempFilePath);
        tempFile.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(tempFilePath);
        hssfWorkbook.write(fileOutputStream);
        fileOutputStream.close();

        JSONObject response = new JSONObject();
        response.put("tempFileName", tempFileName);
        response.put("fileName", fileName);
        return response;
    }

    private HSSFWorkbook createExcel(List<Object[]> resultList) {

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        Row row;
        Cell cUsername, cCnName, cCourtName, cDepartment,
                cLoginTimes, cReadTimes, cCommentCount, cThumbCount;

        // 表头
        row = hssfSheet.createRow(0);
        cUsername = row.createCell(0);
        cUsername.setCellValue("用户名");
        cCnName = row.createCell(1);
        cCnName.setCellValue("姓名");
        cCourtName = row.createCell(2);
        cCourtName.setCellValue("所属法院");
        cDepartment = row.createCell(3);
        cDepartment.setCellValue("所属部门");
        cLoginTimes = row.createCell(4);
        cLoginTimes.setCellValue("登录次数");
        cReadTimes = row.createCell(5);
        cReadTimes.setCellValue("阅读次数");
        cCommentCount = row.createCell(6);
        cCommentCount.setCellValue("评论次数");
        cThumbCount = row.createCell(7);
        cThumbCount.setCellValue("点赞次数");

        for (Object[] object : resultList) {
            int index = resultList.indexOf(object) + 1;
            row = hssfSheet.createRow(index);
            cUsername = row.createCell(0);
            cUsername.setCellValue(object[0].toString());
            cCnName = row.createCell(1);
            cCnName.setCellValue(object[1].toString());
            cCourtName = row.createCell(2);
            cCourtName.setCellValue(object[2].toString());
            cDepartment = row.createCell(3);
            if (object[3] != null) {
                cDepartment.setCellValue(object[3].toString());
            }
            cLoginTimes = row.createCell(4);
            cLoginTimes.setCellValue(((BigInteger) object[4]).intValue());
            cReadTimes = row.createCell(5);
            cReadTimes.setCellValue(((BigInteger) object[5]).intValue());
            cCommentCount = row.createCell(6);
            cCommentCount.setCellValue(((BigInteger) object[6]).intValue());
            cThumbCount = row.createCell(7);
            cThumbCount.setCellValue(((BigInteger) object[7]).intValue());
        }

        return hssfWorkbook;
    }

    private List getResultList(List<String> courtIdList, String departmentId, String beginTime, String endTime) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createNativeQuery(getNativeSql(departmentId));
        if (departmentId.equals("")) {
            query.setParameter("courtIdList", courtIdList);
        } else {
            query.setParameter("departmentId", departmentId);
        }
        query.setParameter("beginTime", beginTime);
        query.setParameter("endTime", endTime);
        List resultList = query.getResultList();
        entityManager.close();
        return resultList;
    }

    private String getNativeSql(String departmentId) {

        String sql = "SELECT \n" +
                "a.username '用户名',\n" +
                "a.cn_name '姓名',\n" +
                "f.name '所属法院',\n" +
                "g.name '所属部门',\n" +
                "IFNULL(b.loginTimes,0) '登录次数',\n" +
                "IFNULL(c.readTimes,0) '阅读次数',\n" +
                "IFNULL(d.commentCount,0) '评论次数',\n" +
                "IFNULL(e.thumbCount,0) '点赞次数'\n" +
                "FROM csp_user a\n" +
                "\n" +
                "LEFT JOIN \n" +
                "(\n" +
                "SELECT user_id,COUNT(*) loginTimes FROM csp_user_login\n" +
                "WHERE event_time>:beginTime \n" +
                "AND event_time<:endTime\n" +
                "GROUP BY user_id\n" +
                ")b ON a.id = b.user_id\n" +
                "\n" +
                "LEFT JOIN \n" +
                "(\n" +
                "SELECT user_id,COUNT(*) readTimes FROM csp_news_read\n" +
                "WHERE event_time>:beginTime \n" +
                "AND event_time<:endTime\n" +
                "GROUP BY user_id\n" +
                ")c ON a.id = c.user_id\n" +
                "\n" +
                "LEFT JOIN \n" +
                "(\n" +
                "SELECT user_id,COUNT(*) commentCount FROM csp_comment\n" +
                "WHERE event_time>:beginTime \n" +
                "AND event_time<:endTime\n" +
                "GROUP BY user_id\n" +
                ")d ON a.id = d.user_id\n" +
                "\n" +
                "LEFT JOIN \n" +
                "(\n" +
                "SELECT user_id,COUNT(*) thumbCount FROM csp_thumb\n" +
                "WHERE event_time>:beginTime \n" +
                "AND event_time<:endTime\n" +
                "GROUP BY user_id\n" +
                ")e ON a.id = e.user_id\n" +
                "\n" +
                "LEFT JOIN csp_court f ON a.court_id = f.id \n" +
                "\n" +
                "LEFT JOIN csp_department g ON a.department_id = g.id \n" +
                "\n";

        if (departmentId.equals("")) {
            sql += "WHERE a.court_id in :courtIdList\n";

        } else {
            sql += "WHERE a.department_id = :departmentId\n";

        }
        sql += " AND a.role_id IN\n" +
                "          (SELECT id\n" +
                "           FROM csp_role\n" +
                "           WHERE tag = 'General')";
        sql += "AND a.delete_flag = 0\n";
        sql += "AND a.enable = 1\n";
        sql += "ORDER BY a.court_id, a.department_id, b.loginTimes DESC";


        return sql;
    }

    public String getTempPath() {

        return tempPath;
    }

}
