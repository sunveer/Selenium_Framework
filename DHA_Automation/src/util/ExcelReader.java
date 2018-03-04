package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	public static HashMap<String, String> getExcelData(String filepath) {
		XSSFRow row;
		XSSFCell cell;
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String currentWorkingDir = System.getProperty("user.dir");
			File src = new File(currentWorkingDir + "\\TestData\\" + filepath);

			FileInputStream fis = new FileInputStream(src);

			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet("DRTP_TestData");
			// Iterating all the rows in the sheet
			Iterator rows = sheet.rowIterator();

			while (rows.hasNext()) {
				row = (XSSFRow) rows.next();

				// Iterating all the cells of the current row
				//Iterator cells = row.cellIterator();
				String key = row.getCell(0).getStringCellValue();
				String value = row.getCell(1).getStringCellValue();
				map.put(key, value);
				//System.out.println(map.get(key));
			}
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
		return map;
	}

}// ends
