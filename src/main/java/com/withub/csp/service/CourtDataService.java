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

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


@Service
@Transactional
public class CourtDataService extends BaseService {

    @Value("${temp.path}")
    private String tempPath;

    public JSONObject exportExcel(String fileName,String courtId,String beginTime,String endTime) throws Exception {

        Query query =entityManagerFactory.createEntityManager().createNativeQuery(getSql());
        query.setParameter("courtId",courtId);
        query.setParameter("beginTime",beginTime);
        query.setParameter("endTime",endTime);
        List<Object[]> resultList = query.getResultList();



        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();

        Row rowHead = hssfSheet.createRow(0);
        Cell cell1 = rowHead.createCell(0);
        Cell cell2 = rowHead.createCell(1);
        cell1.setCellValue("用户名");
        cell2.setCellValue("登录次数");

        for (Object[] object:resultList){
            int index = resultList.indexOf(object);
            Row row = hssfSheet.createRow(index+1);
             cell1 = row.createCell(0);
            cell1.setCellValue(object[0].toString());
             cell2 = row.createCell(1);
            cell2.setCellValue((object[1]).toString());
        }



        String tempFileName = Identities.uuid();
        String tempFilePath = getTempPath() + "/" + tempFileName;
        File file = new File(tempFilePath);
        file.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(tempFilePath);
        hssfWorkbook.write(fileOutputStream);
        fileOutputStream.close();

        JSONObject response = new JSONObject();
        response.put("tempFileName", tempFileName);
        response.put("fileName",fileName);
        return response;
    }

    public String getTempPath() {

        return tempPath;
    }

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private String getSql(){

        return "SELECT a.cn_name ,\n" +
                "IFNULL(d.logintimes,0) \n" +
                "FROM csp_user a \n" +
                "\n" +
                "LEFT JOIN \n" +
                "(\n" +
                "SELECT user_id,COUNT(*) logintimes FROM csp_user_login\n" +
                "WHERE event_time>:beginTime \n" +
                "and event_time<:endTime \n" +
                "GROUP BY user_id\n" +
                ")d ON a.id = d.user_id\n" +
                "\n" +
                "WHERE a.court_id = :courtId\n" +
                "ORDER BY d.logintimes DESC";
    }


}
