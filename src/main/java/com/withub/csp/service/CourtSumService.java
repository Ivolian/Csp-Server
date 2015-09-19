package com.withub.csp.service;

import com.alibaba.fastjson.JSONArray;
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

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;


@Service
@Transactional
public class CourtSumService extends BaseService {

    @Value("${temp.path}")
    private String tempPath;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    //

    public JSONObject exportExcel(String fileName, String beginTime, String endTime) throws Exception {

        HSSFWorkbook hssfWorkbook = createExcel(beginTime,endTime);

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

    public JSONArray list(String beginTime, String endTime) throws Exception {

        return getResultJSONArray(beginTime, endTime);
    }

    private HSSFWorkbook createExcel(String beginTime, String endTime) {

        JSONArray resultJSONArray = getResultJSONArray(beginTime, endTime);

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        Row row;
        Cell cCourtName, cUserCount, cUserCount_20, cUserCount_0_20, cUserCount_0, cUserCount_l, cLoginRate, cGoodLoginRate,
                cLoginTimes, cReadTimes, cReadScale, cCommentCount, cCommentScale, cThumbCount, cThumbScale;

        // 表头
        row = hssfSheet.createRow(0);
        cCourtName = row.createCell(0);
        cCourtName.setCellValue("法院");
        cUserCount = row.createCell(1);
        cUserCount.setCellValue("人数");
        cUserCount_20 = row.createCell(2);
        cUserCount_20.setCellValue("大于20次");
        cUserCount_0_20 = row.createCell(3);
        cUserCount_0_20.setCellValue("0-20次");
        cUserCount_0 = row.createCell(4);
        cUserCount_0.setCellValue("0次");
        cUserCount_l = row.createCell(5);
        cUserCount_l.setCellValue("登录过的人数");
        cLoginRate = row.createCell(6);
        cLoginRate.setCellValue("登录率");
        cGoodLoginRate = row.createCell(7);
        cGoodLoginRate.setCellValue("登录次数达标率");
        cLoginTimes = row.createCell(8);
        cLoginTimes.setCellValue("登录次数");
        cReadTimes = row.createCell(9);
        cReadTimes.setCellValue("阅读次数");
        cReadScale = row.createCell(10);
        cReadScale.setCellValue("阅读比");
        cCommentCount = row.createCell(11);
        cCommentCount.setCellValue("评论次数");
        cCommentScale = row.createCell(12);
        cCommentScale.setCellValue("评论比");
        cThumbCount = row.createCell(13);
        cThumbCount.setCellValue("点赞次数");
        cThumbScale = row.createCell(14);
        cThumbScale.setCellValue("点赞比");

        int size = resultJSONArray.size();
        for (int i=0;i!=size;i++) {
            JSONObject jsonObject = resultJSONArray.getJSONObject(i);
            row = hssfSheet.createRow(i+1);
            cCourtName = row.createCell(0);
            cCourtName.setCellValue(jsonObject.get("courtName").toString());
            cUserCount = row.createCell(1);
            cUserCount.setCellValue(jsonObject.get("userCount").toString());
            cUserCount_20 = row.createCell(2);
            cUserCount_20.setCellValue(jsonObject.get("userCount_20").toString());
            cUserCount_0_20 = row.createCell(3);
            cUserCount_0_20.setCellValue(jsonObject.get("userCount_0_20").toString());
            cUserCount_0 = row.createCell(4);
            cUserCount_0.setCellValue(jsonObject.get("userCount_0").toString());
            cUserCount_l = row.createCell(5);
            cUserCount_l.setCellValue(jsonObject.get("userCount_l").toString());
            cLoginRate = row.createCell(6);
            cLoginRate.setCellValue(jsonObject.get("loginRate").toString());
            cGoodLoginRate = row.createCell(7);
            cGoodLoginRate.setCellValue(jsonObject.get("goodLoginRate").toString());
            cLoginTimes = row.createCell(8);
            cLoginTimes.setCellValue(jsonObject.get("loginTimes").toString());
            cReadTimes = row.createCell(9);
            cReadTimes.setCellValue(jsonObject.get("readTimes").toString());
            cReadScale = row.createCell(10);
            cReadScale.setCellValue(jsonObject.get("readScale").toString());
            cCommentCount = row.createCell(11);
            cCommentCount.setCellValue(jsonObject.get("commentCount").toString());
            cCommentScale = row.createCell(12);
            cCommentScale.setCellValue(jsonObject.get("commentScale").toString());
            cThumbCount = row.createCell(13);
            cThumbCount.setCellValue(jsonObject.get("thumbCount").toString());
            cThumbScale = row.createCell(14);
            cThumbScale.setCellValue(jsonObject.get("thumbScale").toString());
        }

        return hssfWorkbook;
    }

    private JSONArray getResultJSONArray(String beginTime, String endTime) {

        List<Object[]> resultList = getResultList(beginTime, endTime);

        JSONArray jsonArray = new JSONArray();
        for (Object[] object : resultList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("courtName", object[0]);
            BigInteger userCount = (BigInteger) object[1];
            jsonObject.put("userCount", userCount);
            BigDecimal userCount_20 = (BigDecimal) object[2];
            jsonObject.put("userCount_20", userCount_20);
            BigDecimal userCount_0_20 = (BigDecimal) object[3];
            jsonObject.put("userCount_0_20", userCount_0_20);
            jsonObject.put("userCount_0", object[4]);
            BigDecimal userCount_l = userCount_20.add(userCount_0_20);
            jsonObject.put("userCount_l", userCount_l);
            double loginRate = userCount_l.doubleValue() / userCount.intValue();
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            numberFormat.setMaximumFractionDigits(2);
            jsonObject.put("loginRate", numberFormat.format(loginRate));

            double goodLoginRate = userCount_20.doubleValue() / userCount.doubleValue();
            jsonObject.put("goodLoginRate", numberFormat.format(goodLoginRate));

            jsonObject.put("loginTimes", object[5]);

            BigDecimal readTimes = (BigDecimal) object[6];
            jsonObject.put("readTimes", readTimes);
            int readScale = readTimes.intValue() / userCount.intValue();
            jsonObject.put("readScale", readScale);

            BigDecimal commentCount = (BigDecimal) object[7];
            jsonObject.put("commentCount", commentCount);
            int commentScale = commentCount.intValue() / userCount.intValue();
            jsonObject.put("commentScale", commentScale);


            BigDecimal thumbCount = (BigDecimal) object[8];
            jsonObject.put("thumbCount", thumbCount);
            int thumbScale = thumbCount.intValue() / userCount.intValue();
            jsonObject.put("thumbScale", thumbScale);

            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    private List getResultList(String beginTime, String endTime) {

        Query query = entityManagerFactory.createEntityManager().createNativeQuery(getNativeSql());
        query.setParameter("beginTime", beginTime);
        query.setParameter("endTime", endTime);
        return query.getResultList();
    }


    private String getNativeSql() {

        return "SELECT \n" +
                "  a.name,\n" +
                "  b.user_count,\n" +
                "  c.login_count_20,\n" +
                "  c.login_count_0_20,\n" +
                "  c.login_count_0,\n" +
                "  b.login_times,\n" +
                "  b.read_times,\n" +
                "  b.comment_count,\n" +
                "  b.thumb_count \n" +
                "FROM\n" +
                "  csp_court a \n" +
                "  LEFT JOIN \n" +
                "    (SELECT \n" +
                "      f.id court_id,\n" +
                "      COUNT(a.id) user_count,\n" +
                "      SUM(IFNULL(b.loginTimes, 0)) login_times,\n" +
                "      SUM(IFNULL(c.readTimes, 0)) read_times,\n" +
                "      SUM(IFNULL(d.commentCount, 0)) comment_count,\n" +
                "      SUM(IFNULL(e.thumbCount, 0)) thumb_count \n" +
                "    FROM\n" +
                "      csp_user a \n" +
                "      LEFT JOIN \n" +
                "        (SELECT \n" +
                "          user_id,\n" +
                "          COUNT(*) loginTimes \n" +
                "        FROM\n" +
                "          csp_user_login \n" +
                "\t\t\t\tWHERE event_time > :beginTime\n" +
                "\t\t\t\tAND event_time < :endTime\n" +
                "        GROUP BY user_id) b \n" +
                "        ON a.id = b.user_id \n" +
                "      LEFT JOIN \n" +
                "        (SELECT \n" +
                "          user_id,\n" +
                "          COUNT(*) readTimes \n" +
                "        FROM\n" +
                "          csp_news_read \n" +
                "\t\t\t\tWHERE event_time > :beginTime\n" +
                "\t\t\t\tAND event_time < :endTime\n" +
                "        GROUP BY user_id) c \n" +
                "        ON a.id = c.user_id \n" +
                "      LEFT JOIN \n" +
                "        (SELECT \n" +
                "          user_id,\n" +
                "          COUNT(*) commentCount \n" +
                "        FROM\n" +
                "          csp_comment \n" +
                "\t\t\t\tWHERE event_time > :beginTime\n" +
                "\t\t\t\tAND event_time < :endTime\n" +
                "        GROUP BY user_id) d \n" +
                "        ON a.id = d.user_id \n" +
                "      LEFT JOIN \n" +
                "        (SELECT \n" +
                "          user_id,\n" +
                "          COUNT(*) thumbCount \n" +
                "        FROM\n" +
                "          csp_thumb \n" +
                "\t\t\t\tWHERE event_time > :beginTime\n" +
                "\t\t\t\tAND event_time < :endTime\n" +
                "        GROUP BY user_id) e \n" +
                "        ON a.id = e.user_id \n" +
                "      LEFT JOIN csp_court f \n" +
                "        ON a.court_id = f.id \n" +
                "    GROUP BY f.id) b \n" +
                "    ON a.id = b.court_id \n" +
                "  LEFT JOIN \n" +
                "    (SELECT \n" +
                "      a.court_id,\n" +
                "      SUM(\n" +
                "        CASE\n" +
                "          WHEN times = '>20' \n" +
                "          THEN a.count \n" +
                "          ELSE 0 \n" +
                "        END\n" +
                "      ) AS 'login_count_20',\n" +
                "      SUM(\n" +
                "        CASE\n" +
                "          WHEN times = '0~20' \n" +
                "          THEN a.count \n" +
                "          ELSE 0 \n" +
                "        END\n" +
                "      ) AS 'login_count_0_20',\n" +
                "      SUM(\n" +
                "        CASE\n" +
                "          WHEN times = '0' \n" +
                "          THEN a.count \n" +
                "          ELSE 0 \n" +
                "        END\n" +
                "      ) AS 'login_count_0' \n" +
                "    FROM\n" +
                "      (SELECT \n" +
                "        a.court_id,\n" +
                "        CASE\n" +
                "          WHEN a.loginTimes = 0 \n" +
                "          THEN '0' \n" +
                "          WHEN a.loginTimes > 20 \n" +
                "          THEN '>20' \n" +
                "          ELSE '0~20' \n" +
                "        END times,\n" +
                "        COUNT(1) COUNT\n" +
                "      FROM\n" +
                "        (SELECT \n" +
                "          f.id court_id,\n" +
                "          IFNULL(b.loginTimes, 0) loginTimes \n" +
                "        FROM\n" +
                "          csp_user a \n" +
                "          LEFT JOIN \n" +
                "            (SELECT \n" +
                "              user_id,\n" +
                "              COUNT(*) loginTimes \n" +
                "            FROM\n" +
                "              csp_user_login \n" +
                "\t\t\t\tWHERE event_time > :beginTime\n" +
                "\t\t\t\tAND event_time < :endTime\n" +
                "            GROUP BY user_id) b \n" +
                "            ON a.id = b.user_id \n" +
                "          LEFT JOIN csp_court f \n" +
                "            ON a.court_id = f.id \n" +
                "        ORDER BY loginTimes) a \n" +
                "      GROUP BY a.court_id,\n" +
                "        CASE\n" +
                "          WHEN a.loginTimes = 0 \n" +
                "          THEN 1 \n" +
                "          WHEN a.loginTimes > 20 \n" +
                "          THEN 2 \n" +
                "          ELSE 3 \n" +
                "        END) a \n" +
                "\t\t\n" +
                "    GROUP BY a.court_id) c \n" +
                "    ON a.id = c.court_id \n";
//                "--  WHERE a.id = 1" +
//                (courtId.equals("") ? "" : "WHERE a.id = :courtId");
    }

    public String getTempPath() {

        return tempPath;
    }

}
