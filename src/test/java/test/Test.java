package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Test {

	public static void main(String[] args) throws Exception {

		InputStream is = Test.class	.getClassLoader().getResourceAsStream("system.properties");

		Properties props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
		}
		
		
		Pattern pattern = Pattern.compile(props.getProperty("exceptRegExp"));

		BufferedReader in = new BufferedReader(new FileReader("src/test/resources/test.txt"));

		String line = null;

		// 파일을 만들 위치 및 파일명
		FileOutputStream fos = new FileOutputStream(new File("src/test/resources/test.xls"));

		// 엑셀을 workbook을 만듭니다.
		HSSFWorkbook wb = new HSSFWorkbook();

		// Sheet를 만들어요. 이름은 Name
		HSSFSheet sheet = wb.createSheet("test");

		// 앞으로 사용할 row와 cell이에요
		HSSFRow row = null;
		HSSFCell cell = null;
		
		int rownum = 0;
		
		while ((line = in.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			row = sheet.createRow(rownum);
			if (matcher.find()) {
			
				cell = row.createCell(1);
				cell.setCellValue(line);
				rownum++;

			} else {
				System.out.println("bbbb");
			}
		
		}

		// file을 만듭니다.
		wb.write(fos);

		if (fos != null) {
			fos.close();
		}
	

	}

}
