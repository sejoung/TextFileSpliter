package kr.co.killers.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class txtToExcel {
	private static final Logger log = LoggerFactory.getLogger(txtToExcel.class);
	static Map<Integer, String> mapA = new HashMap<Integer, String>();
	static Map<Integer, String> mapB = new HashMap<Integer, String>();

	public static void main(String[] args) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		FileOutputStream fos = new FileOutputStream(new File("D:\\waslog.xls"));
		txtToExcel jte = new txtToExcel();
		int sheetCount = 17;
		int min = 9;
		int rowCount = min * 60;
		log.info(" start ");
		setDataA(mapA, sheetCount, rowCount);
		setDataB(mapB, sheetCount, rowCount);

		jte.filelist("D:\\waslog", wb, fos);
	}

	public static void setDataA(Map<Integer, String> map, int sheetCount, int rowCount) {

		for (int i = 0; i < sheetCount; i++) {

			for (int j = 1; j <= rowCount; j++) {
				if (i != 0) {
					String val = (String) map.get(j);
					map.put(j, val + "+Sheet" + i + "!A" + j);

				} else {
					map.put(j, "Sheet" + i + "!A" + j);
				}
			}

		}

	}

	public static void setDataB(Map<Integer, String> map, int sheetCount, int rowCount) {

		for (int i = 0; i < sheetCount; i++) {

			for (int j = 1; j <= rowCount; j++) {
				map.put(j, "Sheet" + i + "!B" + j);
			}

		}

	}

	public void filelist(String filePath, HSSFWorkbook wb, FileOutputStream fos) throws IOException {

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
		HSSFSheet sheet = wb.createSheet("total");

		int rownum = 0;
		for (Integer key : mapA.keySet()) {

			HSSFRow row = sheet.createRow(rownum);
			HSSFCell cell1 = row.createCell(0);
			cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell1.setCellFormula(mapB.get(key));
			HSSFCell cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(mapA.get(key));
			rownum++;
		}
		wb.write(fos);
	}

	private void writerfile(String filePath, String fileName, HSSFWorkbook wb, FileOutputStream fos)
			throws IOException {

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
			String line = null;
			HSSFSheet sheet = wb.createSheet();

			int rownum = 0;
			while ((line = in.readLine()) != null) {
				String[] aa = line.split(" ");
				HSSFRow row = sheet.createRow(rownum);
				HSSFCell datecell = row.createCell(0);
				datecell.setCellValue(aa[4]);
				HSSFCell countcell = row.createCell(1);
				countcell.setCellValue(aa[5]);
				rownum++;
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
