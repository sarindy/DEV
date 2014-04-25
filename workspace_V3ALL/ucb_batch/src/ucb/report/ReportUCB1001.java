package ucb.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

import ucb.batch.AbstractBatch;
import ucb.batch.BatchReturnCodes;
import ucb.batch.support.row.BranchRow;
import ucb.batch.util.BranchList;
import ucb.batch.util.ParameterContext;
import ucb.batch.util.ReportConstEX;
import ucb.batch.util.expressions.HashtableParameterContext;
import ucb.batch.util.imp.FTPUtil;
import ucb.report.dataAccessor.ReportUCB1001DataAccessor;
import ucb.report.row.UCB1001Row;

/**
 * @author Shuan Tsai
 * CUSTOMER INFORMATION
 */
public class ReportUCB1001 extends AbstractBatch implements BatchReturnCodes {
	private static final String FILE_TYPE = ".xls";
	private static final String SHEET_ID = "UCB1001";

	//private static final String TITLE_1 = "CUSTOMER INFORMATION";
	private static final String END_OF_REPORT = "* * * * * * * End of Report * * * * * * * ";

	private static final String HEADING_1 = "CUST KEY";
	private static final String HEADING_2 = "CUST NAME";
	private static final String HEADING_3 = "NRIC";
	private static final String HEADING_4 = "DOB";
	private static final String HEADING_5 = "E-MAIL";
	private static final String HEADING_6 = "SEX";
	private static final String HEADING_7 = "HOME PHONE";
	private static final String HEADING_8 = "OFFICE PHONE";
	private static final String HEADING_9 = "FAX";
	private static final String HEADING_10 = "OCCUPATION";
	private static final String[] HEADING_ARRAY = {HEADING_1, HEADING_2, HEADING_3, HEADING_4, HEADING_5, HEADING_6, HEADING_7, HEADING_8, HEADING_9, HEADING_10};	

	private static String BRANCH_ID = "";
	private static String DBU_OBU_TYPE_IN = "ALL";
	private static final String OPT_DATE = "OPT_DATE";
	private static final String UNDERLINE = "_";
	private static final String TRANS_FILE_STR = "TRANS_FILE：[{1}]-";
	private static final String REPORT_SUCCESS = "REPORT SUCCESS！";
	private static final String REPORT_FAILURE = "REPORT FAILURE！";
	private final String FTP_UTIL = "ftpUtil2";
	FTPUtil ftpUtil = new FTPUtil(FTP_UTIL);
	
	public static void main(String[] args) {
		/* args[0]:yyyymmdd Start Date*/    
		int returnCode = RC1_08;
		if (args.length > 0) {
			String argsDate = args[0].trim();
			ReportUCB1001 reportUCB1001 = new ReportUCB1001();
			boolean isOk = reportUCB1001.exec(argsDate);
			if (isOk)
				returnCode = RC_0000;
			else
				returnCode = RC2_02;
		}
		System.exit(returnCode);
	}
	
	public boolean exec(String startDate) {
		boolean isOk = false;
		boolean result = false;
		boolean allResult = true;    	
		try {
			BranchList branchList = new BranchList();
				
		
			for (BranchRow branchRow  : branchList.getList()) {
				
				BRANCH_ID = branchRow.getBranchId();
					    		
			
				HSSFWorkbook wb = genReport(genData(DBU_OBU_TYPE_IN,startDate), startDate);
				
				String fileName = SHEET_ID + UNDERLINE + startDate + UNDERLINE + BRANCH_ID +FILE_TYPE;
		
				File file = new File(ftpUtil.getLocalDirectory() + fileName);
				FileOutputStream fileOut = new FileOutputStream(file);
		
				wb.write(fileOut);
				fileOut.flush();
				fileOut.close();
					
				//result = ftpUtil.sendDirectly(fileName); //FTP server
				
				if(result==false){
					allResult = false;
				}
				
				logger.debug(TRANS_FILE_STR.replace("{1}", fileName) + isOk);
				
					
			
			}
		} catch (IOException ex) {
			logger.error(ex);
		}catch (Exception e) {
			 logger.error(e);
			 isOk = false;	        					
		}
    	
		if (allResult)
			isOk = true;
		logger.debug((isOk) ? REPORT_SUCCESS : REPORT_FAILURE);
		return isOk;
	}
	
	private List<UCB1001Row> genData(String DBU_OBU_TYPE_IN, String startDate){

		ReportUCB1001DataAccessor jobDao = new ReportUCB1001DataAccessor();
		ParameterContext parmContext = new HashtableParameterContext();
		parmContext.setValue("DBU_OBU_TYPE_IN", DBU_OBU_TYPE_IN);
		parmContext.setValue(OPT_DATE, startDate);
		
		return jobDao.getReportUCB1001QueryResult(parmContext);
	}
	
	private HSSFWorkbook genReport(List<UCB1001Row> rowList, String startDate){
		BranchList branchList = new BranchList();
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFSheet sheet = wb.createSheet(ReportConstEX.REPORT_TITLE_UCB1001);
		sheet.setAutobreaks(true);//設定縮小到一頁
		
		/* 設定此工作表為保護狀態 */
		sheet.protectSheet("");
		
		HSSFPrintSetup ps = sheet.getPrintSetup();
		ps.setLandscape(false); //設定橫印
		//設定縮小到一頁
		ps.setFitHeight((short)1);
	    ps.setFitWidth((short)1);
	    
		//設定樣式
		HSSFCellStyle textHCentel = wb.createCellStyle();
		textHCentel.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		HSSFCellStyle textHRCentel = wb.createCellStyle();
		textHRCentel.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		
		HSSFCellStyle textBottomCentel = wb.createCellStyle();
		textBottomCentel.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		//設定樣式
		HSSFCellStyle textHVCentel = wb.createCellStyle();
		textHVCentel.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		textHVCentel.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		textHVCentel.setBorderBottom(HSSFCellStyle.BORDER_THIN); //設定儲存格外框
		//textHVCentel.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//textHVCentel.setBorderRight(HSSFCellStyle.BORDER_THIN);
		textHVCentel.setBorderTop(HSSFCellStyle.BORDER_THIN);
		textHVCentel.setWrapText(true);

		//設定欄寬
		sheet.setColumnWidth((short)0,3000);
		sheet.setColumnWidth((short)1,4500);
		sheet.setColumnWidth((short)2,4000);
		sheet.setColumnWidth((short)3,3000);
		sheet.setColumnWidth((short)4,6000);
		sheet.setColumnWidth((short)5,2500);
		sheet.setColumnWidth((short)6,4000);
		sheet.setColumnWidth((short)7,4000);
		sheet.setColumnWidth((short)8,4000);
		sheet.setColumnWidth((short)9,4000);
		HSSFRow hssfRow = null;
		HSSFCell cell = null;
		
		hssfRow = sheet.createRow(0);
		cell = hssfRow.createCell(0);
		cell.setCellValue(new HSSFRichTextString(ReportConstEX.REPORT_TITLE_UCB1001));
		cell.setCellStyle(textHCentel);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,9));

		hssfRow = sheet.createRow(1);
		cell = hssfRow.createCell(0);
		cell.setCellValue(new HSSFRichTextString(ReportConstEX.REPORT_BRANCH + BRANCH_ID + " " + branchList.getName(BRANCH_ID)));
		sheet.addMergedRegion(new CellRangeAddress(1,1,0,3));
		cell = hssfRow.createCell(4);
		cell.setCellValue(new HSSFRichTextString(ReportConstEX.REPORT_DATA_DATE + ReportConstEX.getAdToday()));
		cell.setCellStyle(textHRCentel);
		sheet.addMergedRegion(new CellRangeAddress(1,1,4,9));
		
		hssfRow = sheet.createRow(2);
		for (int i = 0; i < HEADING_ARRAY.length; i++) {
			cell = hssfRow.createCell(i);
			cell.setCellValue(new HSSFRichTextString(HEADING_ARRAY[i]));
			cell.setCellStyle(textHVCentel);
			sheet.addMergedRegion(new CellRangeAddress(1,1,i,i));
		}
		
		int currentRow = 3;
		//int startRow = 3;
		for(UCB1001Row row : rowList){
			hssfRow = sheet.createRow(currentRow);
			
			cell = hssfRow.createCell(0);
			cell.setCellValue(new HSSFRichTextString(row.getCustKeyChar()+row.getCustKeyNum()));
			cell.setCellStyle(textHCentel);
			cell = hssfRow.createCell(1);
			cell.setCellValue(new HSSFRichTextString(row.getName()));
			cell.setCellStyle(textHCentel);
			cell = hssfRow.createCell(2);
			cell.setCellValue(new HSSFRichTextString(row.getNric()));
			cell.setCellStyle(textHCentel);
			cell = hssfRow.createCell(3);	
			cell.setCellValue(new HSSFRichTextString(row.getDob()));
			cell.setCellStyle(textHCentel);
			cell = hssfRow.createCell(4);		
			cell.setCellValue(new HSSFRichTextString(row.getZipCode()));
			cell.setCellStyle(textHCentel);
			cell = hssfRow.createCell(5);
			cell.setCellValue(new HSSFRichTextString(row.getSex()));
			cell.setCellStyle(textHCentel);			
			cell = hssfRow.createCell(6);
			cell.setCellValue(new HSSFRichTextString(row.gethPhone()));
			cell.setCellStyle(textHCentel);
			cell = hssfRow.createCell(7);
			cell.setCellValue(new HSSFRichTextString(row.getoPhone()));
			cell.setCellStyle(textHCentel);
			cell = hssfRow.createCell(8);
			cell.setCellValue(new HSSFRichTextString(row.getFax()));
			cell.setCellStyle(textHCentel);
			cell = hssfRow.createCell(9);
			cell.setCellValue(new HSSFRichTextString(row.getOcupation()));
			cell.setCellStyle(textHCentel);
			
			currentRow++;
		}
		
		if(rowList.size()!=0){
			hssfRow = sheet.createRow(currentRow);
			cell = hssfRow.createCell(1);
			cell.setCellStyle(textBottomCentel);
//			cell = hssfRow.createCell(9);
//			cell.setCellStyle(textBottomCentel);
			currentRow++;
			
			hssfRow = sheet.createRow(currentRow);
			cell = hssfRow.createCell(1);
			cell.setCellValue(new HSSFRichTextString(rowList.size() + "RECS"));
			cell.setCellStyle(textHCentel);
			
//			for (int j = 8; j < 9; j++) {
//				CellReference cr1 = new CellReference(startRow, j);
//				CellReference cr2 = new CellReference(currentRow-1, j);
//				hssfRow.createCell(j).setCellFormula("SUM(" + cr1.formatAsString() + ":" + cr2.formatAsString() + ")");
//			}
			currentRow++;
		}

		currentRow++;
		
		hssfRow = sheet.createRow(currentRow);
		cell = hssfRow.createCell(0);
		cell.setCellValue(new HSSFRichTextString(END_OF_REPORT));
		cell.setCellStyle(textHCentel);
		sheet.addMergedRegion(new CellRangeAddress(currentRow,currentRow,0,9));
		
		currentRow++;
		
		return wb;
		
	}
	
}
