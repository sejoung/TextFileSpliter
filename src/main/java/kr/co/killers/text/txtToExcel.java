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

public class txtToExcel {
    private static final Logger log = LoggerFactory.getLogger(txtToExcel.class);

    public static void main(String[] args) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        FileOutputStream fos = new FileOutputStream(new File("D:\\waslog.xls"));
        txtToExcel jte = new txtToExcel();

        jte.filelist("D:\\waslog", wb,fos);
    }
    
    public void filelist(String filePath, HSSFWorkbook wb,  FileOutputStream fos) throws IOException {

        File f1 = new File(filePath);
        String[] list = f1.list();

        for (int i = 0; i < list.length; i++) {
            File f2 = new File(filePath, list[i]);

            if (f2.isDirectory()) {
                this.filelist(f2.getCanonicalPath(), wb, fos);
            } else {
                this.writerfile(f2.getCanonicalPath(), f2.getName(), wb, fos);
            }
        }
        
        wb.write(fos);
    }

    private void writerfile(String filePath, String fileName, HSSFWorkbook wb,  FileOutputStream fos) throws IOException {

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
            String line = null;
            HSSFSheet sheet = wb.createSheet();
            
            int rownum = 0;
            while ((line = in.readLine()) != null) {
            HSSFRow row = sheet.createRow(rownum);
            HSSFCell datecell = row.createCell(1);
            datecell.setCellValue(line);
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
