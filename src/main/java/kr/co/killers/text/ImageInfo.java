package kr.co.killers.text;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ImageInfo {
    public static void main(String[] args) throws Exception {

        FileInputStream fis = new FileInputStream("D:\\00.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        int rowindex = 0;
        int columnindex = 0;
        // 시트 수 (첫번째에만 존재하므로 0을 준다)
        // 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 행의 수
        int rows = sheet.getPhysicalNumberOfRows();
        for (rowindex = 1; rowindex < rows; rowindex++) {
            // 행을 읽는다
            HSSFRow row = sheet.getRow(rowindex);
            if (row != null) {
                // 셀의 수
                int cells = row.getPhysicalNumberOfCells();

                // 셀값을 읽는다
                HSSFCell cell = row.getCell(1);
                String value = "";
                // 셀이 빈값일경우를 위한 널체크
                if (cell == null) {
                    continue;
                } else {
                    // 타입별로 내용 읽기
                    switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_FORMULA:
                        value = cell.getCellFormula();
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        value = cell.getNumericCellValue() + "";
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue() + "";
                        break;
                    case HSSFCell.CELL_TYPE_BLANK:
                        value = cell.getBooleanCellValue() + "";
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        value = cell.getErrorCellValue() + "";
                        break;
                    }
                }

                try {

                    URL url = new URL(value);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    InputStream is = url.openStream();
                    byte[] b = new byte[2^16];
                    int read = is.read(b);
                    while (read>-1) {
                        baos.write(b,0,read);
                        read = is.read(b);
                    }
                    int countInBytes = baos.toByteArray().length;
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    Image image = ImageIO.read(bais);
                    int width = image.getWidth(null);
                    int height = image.getHeight(null);
                    String imageInfo = width + "x" + height + " px ";
                    System.out.println(imageInfo);

                } catch (Exception e) {
                    System.out.println("없음");

                }
            }
        }

      
    }

}
