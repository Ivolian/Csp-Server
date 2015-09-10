package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Identities;

import java.io.File;
import java.io.FileOutputStream;


@Service
@Transactional
public class CourtDataService extends BaseService {

    @Value("${temp.path}")
    private String tempPath;

    public JSONObject exportExcel() throws Exception {

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        Row row = hssfSheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(2);

        String tempFileName = Identities.uuid();
        String tempFilePath = getTempPath() + "/" + tempFileName;
        File file = new File(tempFilePath);
        file.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(tempFilePath);
        hssfWorkbook.write(fileOutputStream);
        fileOutputStream.close();

        JSONObject response = new JSONObject();
        response.put("tempFileName", tempFileName);
        return response;
    }

    public String getTempPath() {

        return tempPath;
    }


}
