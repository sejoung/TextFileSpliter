package kr.co.killers.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class jsonToExcel {
    private static final Logger log = LoggerFactory.getLogger(jsonToExcel.class);

    public static void main(String[] args) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        FileOutputStream fos = new FileOutputStream(new File("E:\\CLICK_20170923.xls"));
        //FileOutputStream fos = new FileOutputStream(new File("E:\\CONVERSION_20170923.xls"));

        jsonToExcel jte = new jsonToExcel();

        //jte.filelist("E:\\lowdata\\conversion", CommonConstants.CONVERSION,wb,fos);
        jte.filelist("E:\\lowdata\\rfshop", CommonConstants.CLICK,wb,fos);
    }
    
    public void filelist(String filePath, String code, HSSFWorkbook wb,  FileOutputStream fos) throws IOException {

        File f1 = new File(filePath);
        String[] list = f1.list();

        for (int i = 0; i < list.length; i++) {
            File f2 = new File(filePath, list[i]);

            if (f2.isDirectory()) {
                this.filelist(f2.getCanonicalPath(), code, wb, fos);
            } else {
                this.writerfile(f2.getCanonicalPath(), f2.getName(), code, wb, fos);
            }
        }
        
        wb.write(fos);
    }

    private void writerfile(String filePath, String fileName, String code, HSSFWorkbook wb,  FileOutputStream fos) throws IOException {

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
            String line = null;
            HSSFSheet sheet = wb.createSheet();
            
            int rownum = 0;
            while ((line = in.readLine()) != null) {
                if (CommonConstants.CLICK.equals(code)) {
                    AdvertiserClick click = (AdvertiserClick) JsonUtil.parseRequestJson(line, AdvertiserClick.class);
                    HSSFRow row = sheet.createRow(rownum);
                    HSSFCell datecell = row.createCell(1);
                    datecell.setCellValue(click.getRegdate());
                    HSSFCell platformcell = row.createCell(2);
                    platformcell.setCellValue(click.getPcMobileGubun());
                    HSSFCell auidcell = row.createCell(3);
                    auidcell.setCellValue(click.getAuid());
                    HSSFCell pcodecell = row.createCell(4);
                    pcodecell.setCellValue(click.getPcode());
                    HSSFCell gbcell = row.createCell(5);
                    gbcell.setCellValue(click.getGb());
                    
                } else if (CommonConstants.CONVERSION.equals(code)) {
                    AdvertiserConversion conversion = (AdvertiserConversion) JsonUtil.parseRequestJson(line, AdvertiserConversion.class);
                    HSSFRow row = sheet.createRow(rownum);
                    HSSFCell datecell = row.createCell(1);
                    datecell.setCellValue(conversion.getRegdate());
                    HSSFCell platformcell = row.createCell(2);
                    platformcell.setCellValue(conversion.getPltfomTpCode());
                    HSSFCell auidcell = row.createCell(3);
                    auidcell.setCellValue(conversion.getAuid());
                    HSSFCell pcodecell = row.createCell(4);
                    pcodecell.setCellValue(conversion.getPcode());
                    
                    log.debug("conversion ={}", conversion);

                } else {
                    log.debug("--------------------------------->>>>>>>>>>>>>>>>>>>>>ERROR");
                }
                rownum ++;
            }
            
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
