package ucb.batch.util;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author LSC
 * @since 2008-08-01
 * @see 　報表格式化工具：格式化金額及字串
 *
 */
public abstract class Formatter {
	
	private static final String LEN_ERROR = "輸入的長度有誤";
	private static final String TWO_ZERO = "00";
	private static final String FOUR_ZERO = "0000";
	private static final String COMMA_DECIMAL_STR = "###,##0";
	private static final String DIGITS_DECIMAL_STR = "#.##";
	private static final String FIFTEEN_DECIMAL_STR = "#,###,###,##0.00";
	private static final String MAXBYTES_MUST_BE_POSITIVE = "maxBytes must be positive.";
	private static final String INVALID_JUSTIFICATION_ARG = "Invalid Justification Arg.";
	private static final char ONE_SPACE = ' ';
	private static final char ZERO = '0';	
	private static final String YYYYMMDD_SLASH = "yyyy/MM/dd";
	private static final String YYYMMDD_HYPHEN = "yyyy-MM-dd";
	private static final char SLASH = '/';
	private static final char HYPHEN = '-';
	/* Constant for left justification. */
	public static final int JUST_LEFT = 'l';
	/* Constant for centering. */
	public static final int JUST_CENTRE = 'c';
	/* Constant for right-justified Strings. */
	public static final int JUST_RIGHT = 'r';
	
	public static final int MAX_133_LEN = 132;
	public static final int MAX_199_LEN = 198;
	public static final int _133LEN = 133;
	public static final int _199LEN = 199;
	
    public static final int SPLIT_MONTH_START = 4;
    public static final int SPLIT_MONTH_END = 6;
    public static final int SPLIT_YEAR_START = 0;
    public static final int SPLIT_YEAR_END = 4;
	/**
	 * 常數：換行符號，使用系統變數
	 */
//	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String NEW_LINE = "\r\n";
	public static final char[] NOT_ALLWED_CHAR = new char[] {'\t', '\r', '\n'};
	public static final Charset CHARSET_BIG5 = Charset.forName("BIG5");
	/**
	 * 常數：垂直線，使用系統變數
	 */
	public static final String VERTICAL_BAR = "|";
	
	/**
	 * 常數：日期格式：yyyyMMdd
	 */
	public static final String YYYYMMDD = "yyyyMMdd";

	public Formatter() {
	}	
		
	protected final static void pad(StringBuffer to, int howMany) {
		for (int i = 0; i < howMany; i++) {
			to.append(ONE_SPACE);
		}
	}
	/**
	 * 將輸入的字元，依指定的最大位元組數產生字串
	 * 
	 * @param c 字元
	 * @param colSize 字串長度
	 * @return 傳回位元組長度等於colSize的字串
	 */
	public static String padChar(char c, int colSize){	    
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < colSize; i++) {
			sb.append(c);
		}
	    return sb.toString();
	}
	/**
	 * 將輸入的字串，變成指定的最大位元組數（會預留傳上AS400時，需要加上的0E, 0F的空間）
	 * 如果字串短於最大位元組長度，根據對齊方式補空白。如果最後只剩一個位元組，雙位元組的字元就無法加入。
	 * @param line 輸入的字串
	 * @param just 對齊方式
	 * @param maxBytesLength 最大的字元組長度
	 * @return 傳回位元組長度等於maxBytesLength的字串
	 */
	public static String formatCell(String line, int just, int maxBytesLength) {
		return formatCell(line, just, maxBytesLength, false);
	}
	
	/**
	 * 將輸入的字串，變成指定的最大位元組數（在中文前後補上' '）
	 * 如果字串短於最大位元組長度，根據對齊方式補空白。如果最後只剩一個位元組，雙位元組的字元就無法加入。
	 * @param line 輸入的字串
	 * @param just 對齊方式
	 * @param maxBytesLength 最大的字元組長度
	 * @param isPad 是否要在中文前後加上' '
	 * @return 傳回位元組長度等於maxBytesLength的字串
	 */
	public static String formatCell(String line, int just, int maxBytesLength, boolean isPad) {
	    
		if (!isValidJust(just)) {
			throw new IllegalArgumentException(INVALID_JUSTIFICATION_ARG);
		}
		if (maxBytesLength < 0) {
			throw new IllegalArgumentException(MAXBYTES_MUST_BE_POSITIVE);
		}
	
		StringBuffer wanted = new StringBuffer(); 
		int currBytesLength = 0; //目前已經加入的位元組長度
		boolean oeOn = false; //前面是否已經有0E
		
		CharsetDecoder decoder = CHARSET_BIG5.newDecoder();
        CharsetEncoder encoder = CHARSET_BIG5.newEncoder();
        String big5Line = "";
        try {
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(line));
            CharBuffer cbuf = decoder.decode(bbuf);
            big5Line = cbuf.toString();
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
		
		int length = big5Line.length();
		for (int i = 0; i < length; i++) { // 一個一個字元去處理，如果是中文，補上0E, 0F
			char ch = big5Line.charAt(i);
			
			if (!isAllowedChar(ch)) { // 若該字元不被允許，置換為空白。 
				ch = ONE_SPACE;
			}
			
			if (isOneByteChar(ch)) { //如果單位元組的字元，需檢查上一個字是不是中文，如果是中文要補上0F
				if (oeOn) { //如果前面的是中文，需補上0F
					oeOn = false;
					pad(wanted, isPad);
					currBytesLength+=1; //前面是中文，補上0F
				}				
				if (maxBytesLength - currBytesLength < 1) {
					break;
				} else {
					wanted.append(ch);
					currBytesLength+=1; // ch
				}
			} else if (oeOn && !isOneByteChar(ch)) { //如果是雙位元組的字元，且前面的字元是中文，不用再補上0E
				if (maxBytesLength - currBytesLength < 3) {//剩不到3 byte，則中文加0F放不進去。
					pad(wanted, isPad);
					currBytesLength+=1; //前面是中文，補上0F
					oeOn = false;
					break;
				} else {
					wanted.append(ch);
					currBytesLength+=2; //中文
				}
			} else if (!oeOn && !isOneByteChar(ch)) { //如果是雙位元組的字元，且前面的字元不是中文，表示要補0E
				if (maxBytesLength - currBytesLength < 4) {//剩不到4 byte，則中文加0E, 0F放不進去。
					break;
				} else {
					pad(wanted, isPad);
					oeOn = true;
					wanted.append(ch);
					currBytesLength+=3;
				}
			} // end if chinese
		} // end for
		if (oeOn) { // 如果0E還存在，表示還沒給0F，需補上0F。
			pad(wanted, isPad);
			currBytesLength+=1;
			oeOn = false;
		}
		// 補空白
		StringBuffer newLine = new StringBuffer();
		switch (just) {
		case JUST_RIGHT:
			pad(newLine, maxBytesLength - currBytesLength);
			newLine.append(wanted);
			break;
		case JUST_CENTRE:
			int toAdd = maxBytesLength - currBytesLength;
			pad(newLine, toAdd / 2);
			newLine.append(wanted);
			pad(newLine, toAdd - toAdd / 2);
			break;
		case JUST_LEFT:
			newLine.append(wanted);
			pad(newLine, maxBytesLength - currBytesLength);
			break;
		default:
			break;
		}		
		
		return newLine.toString();
	}
	private static void pad(StringBuffer wanted, boolean isPad) {
		if (isPad) {
			wanted.append(ONE_SPACE);
		}
	}
	
	private static boolean isValidJust(int just) {
		int[] validJust = {JUST_LEFT, JUST_CENTRE, JUST_RIGHT};
		boolean isValid = false;
		for (int i = 0; i < validJust.length; i++) {
			if (just == validJust[i]) {
				return true;
			}
		} //end for
		return isValid;
	}
	
	/**
	 * 檢查字元是否為允許的字元
	 * @param oneChar 要檢查的字元
	 * @return 如果該字元為 NOT_ALLWED_CHAR，則傳回 false。否則傳回 true 。
	 */
	private static boolean isAllowedChar(char oneChar) {
		for (int i = 0; i < NOT_ALLWED_CHAR.length; i++) {
			if (NOT_ALLWED_CHAR[i] == oneChar) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 將輸入字串轉換成133長度的字串，最前面補空白，並補上換行字元。
	 * 靠左對齊。
	 * @param line
	 * @return
	 */
	public static String toDocu133Line(String line) {
		return toDocuLine(line, JUST_LEFT, _133LEN, ONE_SPACE);
	}
	/**
	 * 將輸入字串轉換成133長度的字串，最前面補空白，並補上換行字元。
	 * @param line 輸入的字串
	 * @param just 對齊方式
	 * @return
	 */
	public static String toDocu133Line(String line, int just) {
		return toDocuLine(line, just, _133LEN, ONE_SPACE);
	}
	/**
	 * 將輸入字串轉換成133長度的字串，最前面補1，並補上換行字元。
	 * 靠左對齊。
	 * @param line
	 * @return
	 */
	public static String toDocu133AdvLine(String line) {
		return toDocuLine(line, JUST_LEFT, _133LEN, ReportConst.REPORT_PAGE_CTRL);
	}
	/**
	 * 將輸入字串轉換成133長度的字串，最前面補1，並補上換行字元。
	 * @param line
	 * @param just 對齊方式
	 * @return
	 */
	public static String toDocu133AdvLine(String line, int just) {
		return toDocuLine(line, just, _133LEN, ReportConst.REPORT_PAGE_CTRL);
	}
	/**
	 * 將輸入字串轉換成133長度的字串，最前面補空白，並補上換行字元。
	 * 靠左對齊。
	 * @param line
	 * @return
	 */
	public static String toDocu199Line(String line) {
		return toDocuLine(line, JUST_LEFT, _199LEN, ONE_SPACE);
	}
	/**
	 * 將輸入字串轉換成133長度的字串，最前面補空白，並補上換行字元。
	 * @param line
	 * @param just 對齊方式
	 * @return
	 */
	public static String toDocu199Line(String line, int just) {
		return toDocuLine(line, just, _199LEN, ONE_SPACE);
	}
	/**
	 * 將輸入字串轉換成199長度的字串，最前面補1，並補上換行字元。
	 * 靠左對齊。
	 * @param line
	 * @return
	 */
	public static String toDocu199AdvLine(String line) {
		return toDocuLine(line, JUST_LEFT, _199LEN, ReportConst.REPORT_PAGE_CTRL);
	}
	/**
	 * 將輸入字串轉換成199長度的字串，最前面補1，並補上換行字元。
	 * @param line
	 * @param just 對齊方式
	 * @return
	 */
	public static String toDocu199AdvLine(String line, int just) {
		return toDocuLine(line, just, _199LEN, ReportConst.REPORT_PAGE_CTRL);
	}

	/**
	 * 將輸入的字串轉換成報表備存的一行資料
	 * @param line 輸入的字串
	 * @param just 對齊的方式
	 * @param length 輸出的字串
	 * @param preChar 前面是要加' '還是換頁'1'
	 * @return 報表備存的一行資料，會加上"\r\n"
	 */
	private static String toDocuLine(String line, int just, int length, char preChar) {
		return formatCell(preChar + line, just, length) + NEW_LINE;
	}
	
	/**
	 * 將輸入double的金額轉換成字串 ###,##0.00
	 * @param double
	 * @return String
	 */
	public static String currencyFormat(double amount){
	    DecimalFormat decimalFormat = new DecimalFormat(FIFTEEN_DECIMAL_STR);
	    return decimalFormat.format(amount);	    
	}
	/**
	 * 將輸入double的金額轉換成字串 ###,##0，會四捨五入
	 * @param double
	 * @return String
	 */
	public static String currencyFormatNoDot(double amount){
	    DecimalFormat decimalFormat = new DecimalFormat(COMMA_DECIMAL_STR);
	    return decimalFormat.format(amount);	    
	}
	/**
     * 將輸入double的金額轉換成字串 ###,##，會四捨五入
     * @param double
     * @return String
     */
    public static String currencyDigitFormat(double amount){
        DecimalFormat decimalFormat = new DecimalFormat(DIGITS_DECIMAL_STR);
        return decimalFormat.format(amount);        
    }
	/**
	 * 將輸入的頁碼轉換成字串 0000。
	 * @param pageNo 頁碼
	 * @return 傳回 0000 的字串
	 */
	public static String pageNoFormat(int pageNo) {
		NumberFormat intF = new DecimalFormat(FOUR_ZERO);
		return intF.format(pageNo);
	}
	/**
	 * 根據傳入的數字，乘以一百後，在後面加入%符號。
	 * @param amount 數字。
	 * @param point 小數要幾位數，必須大於等於0。
	 * @return
	 */
	public static String withPercent(double amount, int point) {
		StringBuffer formatSB = new StringBuffer(15).append(ZERO);
		if (point > 0) {
			formatSB.append('.');
			for(int i = 0; i < point; i++) {
				formatSB.append(ZERO);
			}
		}
		DecimalFormat df = new DecimalFormat(formatSB.toString());
		return df.format(amount * 100) + '%';
	}
	
	/**
	 * 將入的字串日期的前四碼yyyy西元年，轉換成四碼民國年
	 * @param yearString 
	 * @return yyyy四碼民國年 + 原本後面的字串
	 */
	public static String toFourBytesChineseYear(String yearString) {
		if(yearString == null || yearString.length()<=4)
			return "        ";
		String year = yearString.substring(0, 4);
		DecimalFormat df = new DecimalFormat(FOUR_ZERO);
		year = df.format(Double.valueOf(year).doubleValue() - 1911);
		
		return year + yearString.substring(4);
	}
	/**
	 * 將入的字串日期的前四碼yyyy西元年，轉換成兩碼民國年
	 * @param yearString
	 * @return yy兩碼民國年 + 後面原本的字串
	 */
	public static String toTwoBytesChineseYear(String yearString) {
		String year = yearString.substring(0, 4);
		DecimalFormat df = new DecimalFormat(TWO_ZERO);
		year = df.format(Double.valueOf(year).doubleValue() - 1911);
		
		return year + yearString.substring(4);
	}
	/**
	 * 輸入的字串日期西元年yyyymmdd，轉換成yyyy/mm/dd
	 * @param yearMonthDateString
	 * @return yyyy/mm/dd
	 */
	public static String toDateWithSlash(String yearMonthDateString) {	    
	    return toDateFormat(yearMonthDateString, SLASH);
	}
	
	/**
	 * 輸入的字串日期西元年yyyymmdd，轉換成yyyy-mm-dd
	 * @param yearMonthDateString
	 * @return yyyy-mm-dd
	 */
	public static String toDateWithHyphen(String yearMonthDateString) {	    
	    return toDateFormat(yearMonthDateString, HYPHEN);
	}
	
	/**
	 * 輸入的字串日期西元年yymmdd，轉換成yyyymmdd
	 * @param yearMonthDateString
	 * @return yyyymmdd
	 */
	public static String toFourBytesDate(String yearMonthDateString) {	 
		
		String fourBytesyear = null;
			
		Date newDate = new Date();
		
		String formatNewDate = new SimpleDateFormat(YYYYMMDD).format(newDate);
		
		fourBytesyear = formatNewDate.substring(0,4);
		
		return fourBytesyear + yearMonthDateString.substring(2,6);
	
	}
	
	private static String toDateFormat(String dateYearString, char splitterType){
	    try {
	    	String formatDateStr = null;
			if (dateYearString.length() == 8) {
				Date parseDate = new SimpleDateFormat(YYYYMMDD).parse(dateYearString);
				formatDateStr = (HYPHEN == splitterType) ? 
						new SimpleDateFormat(YYYMMDD_HYPHEN).format(parseDate) : 
						new SimpleDateFormat(YYYYMMDD_SLASH).format(parseDate);
			}
			return formatDateStr;
		} catch (ParseException e) {
			return e.getMessage();
		}
	}
	/**
	 * 判斷輸入的字元是否為一個位元組的字元
	 * 
	 * @param ch
	 *            輸入的字元
	 * @return 如果是一個位元組的字元，傳回True，如果不是，傳回false
	 */
	public static boolean isOneByteChar(char ch) {	
		Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
		return (Character.UnicodeBlock.BASIC_LATIN.equals(block)) ? true : false;
	}

	/**
	 * 根據輸入的年度、報表代號，以及報表長度，產生標準制式 133 長度的表尾
	 * @param year 年度，如：五、十、十五、二十 @see com.esb.fx.batch.util.ReportConst.REPROT_FIVE_YEAR
	 * @param fsNo  報表代號，如：EF3001
	 * @return
	 */
	public static String get133ReportTail(String year, String fsNo) {
		return getReportTail(year, fsNo, MAX_133_LEN);
	}
	
	/**
	 * 根據輸入的年度、報表代號，以及報表長度，產生標準制式 133 長度的表尾
	 * @param year 年度，如：五、十、十五、二十 @see com.esb.fx.batch.util.ReportConstEX.REPROT_FIVE_YEAR
	 * @param fsNo  報表代號，如：EF3001
	 * @return
	 */
	public static String get133ReportTailEX(String year, String fsNo) {
		return getReportTailEX(year, fsNo, MAX_133_LEN);
	}
	
	/**
	 * 根據輸入的年度、報表代號，以及報表長度，產生標準制式 199 長度的表尾
	 * @param year 年度，如：五、十、十五、二十 @see com.esb.fx.batch.util.ReportConst.REPROT_FIVE_YEAR
	 * @param fsNo  報表代號，如：EF3001
	 * @return  制式表尾
	 */
	public static String get199ReportTail(String year, String fsNo) {
		return getReportTail(year, fsNo, MAX_199_LEN);
	}
	
	/**
	 * 根據輸入的年度、報表代號，以及報表長度，產生標準制式 199 長度的表尾
	 * @param year 年度，如：五、十、十五、二十 @see com.esb.fx.batch.util.ReportConstEX.REPROT_FIVE_YEAR
	 * @param fsNo  報表代號，如：EF3001
	 * @return  制式表尾
	 */
	public static String get199ReportTailEX(String year, String fsNo) {
		return getReportTailEX(year, fsNo, MAX_199_LEN);
	}
	
	/**
	 * 根據輸入的年度、報表代號，以及報表長度，產生標準制式的表尾
	 * ====================================================================================================================================
	 * 核章：                              會計：                              經辦：                              製表單位：   資訊處    
     *                                                                        保存年限：五年                      報表代號：   EF3001PT  
	 * @param year 年度，如：五、十、十五、二十 @see com.esb.fx.batch.util.ReportConst.REPROT_FIVE_YEAR
	 * @param fsNo 報表代號，如：EF3001
	 * @param colSize 長度，只允許 MAX_133_LEN 與 MAX_199_LEN 兩種
	 * @return 制式表尾
	 */
	public static String getReportTail(String year, String fsNo, int colSize) {
		if (!isValidLength(colSize)) {
			throw new IllegalArgumentException(LEN_ERROR);
		}
		StringBuffer tail = new StringBuffer();
		StringBuffer line = new StringBuffer();
		
		// 分隔線
		if (colSize == MAX_133_LEN) {
			tail.append(toDocu133Line(padChar(ReportConst.REPORT_SPLITTER_1, colSize)));
		} else if (colSize == MAX_199_LEN) {
			tail.append(toDocu199Line(padChar(ReportConst.REPORT_SPLITTER_1, colSize)));
		}
        
        // 核章
		int cellLen133 = 36;
		int cellLen199 = 58;
        int cellLen = 0;
        int maxLen = 0;
        // 要使用的cell長度，最大的長度
        if (colSize == MAX_133_LEN) {
			cellLen = cellLen133;
			maxLen = MAX_133_LEN;
		} else if (colSize == MAX_199_LEN) {
			cellLen = cellLen199;
			maxLen = MAX_199_LEN;
		}
	        line.append(Formatter.formatCell(ReportConst.REPORT_CONSIGNER_AUTHORIZED, JUST_LEFT, cellLen));
	        line.append(Formatter.formatCell(ReportConst.REPORT_CONSIGNER_ACCOUNTANT, JUST_LEFT, cellLen));
	        line.append(Formatter.formatCell(ReportConst.REPORT_CONSIGNER_AGENT, JUST_LEFT, cellLen));
	        line.append(Formatter.formatCell(ReportConst.REPORT_FACTORY_TITLE + ONE_SPACE + ReportConst.REPORT_FACTORY,
	        		JUST_LEFT, maxLen - cellLen * 3));
        
        if (colSize == MAX_133_LEN) {
        	tail.append(Formatter.toDocu133Line(line.toString()));
		} else if (colSize == MAX_199_LEN) {
			tail.append(Formatter.toDocu199Line(line.toString()));
		}
        line.delete(0, line.length());
        // 保存期限
	    line.append(Formatter.formatCell(String.valueOf(ONE_SPACE), JUST_LEFT, cellLen * 2));
	    line.append(Formatter.formatCell(ReportConst.REPORT_EXPIRATION + year + ReportConst.TW_YEAR, JUST_LEFT, cellLen));
	    line.append(Formatter.formatCell(ReportConst.REPORT_ID + ' ' + ' ' + fsNo + ReportConst.PT_PREFIX, JUST_LEFT, maxLen - cellLen * 3));

        if (colSize == MAX_133_LEN) {
        	tail.append(Formatter.toDocu133Line(line.toString()));
		} else if (colSize == MAX_199_LEN) {
			tail.append(Formatter.toDocu199Line(line.toString()));
		}
        line.delete(0, line.length());
        
        return tail.toString();
	}
	
	/**
	 * 根據輸入的年度、報表代號，以及報表長度，產生標準制式的表尾
	 * ====================================================================================================================================
	 * 核章：                              會計：                              經辦：                              製表單位：   資訊處    
     *                                                                        保存年限：五年                      報表代號：   EF3001PT  
	 * @param year 年度，如：五、十、十五、二十 @see com.esb.fx.batch.util.ReportConstEX.REPROT_FIVE_YEAR
	 * @param fsNo 報表代號，如：EF3001
	 * @param colSize 長度，只允許 MAX_133_LEN 與 MAX_199_LEN 兩種
	 * @return 制式表尾
	 */
	public static String getReportTailEX(String year, String fsNo, int colSize) {
		if (!isValidLength(colSize)) {
			throw new IllegalArgumentException(LEN_ERROR);
		}
		StringBuffer tail = new StringBuffer();
		StringBuffer line = new StringBuffer();
		
		// 分隔線
		if (colSize == MAX_133_LEN) {
			tail.append(toDocu133Line(padChar(ReportConstEX.REPORT_SPLITTER_1, colSize)));
		} else if (colSize == MAX_199_LEN) {
			tail.append(toDocu199Line(padChar(ReportConstEX.REPORT_SPLITTER_1, colSize)));
		}
        
        // 核章
		int cellLen133 = 23;
		int cellLen199 = 45;
        int cellLen = 0;
        int maxLen = 0;
        // 要使用的cell長度，最大的長度
        if (colSize == MAX_133_LEN) {
			cellLen = cellLen133;
			maxLen = MAX_133_LEN;
		} else if (colSize == MAX_199_LEN) {
			cellLen = cellLen199;
			maxLen = MAX_199_LEN;
		}
	        line.append(Formatter.formatCell(ReportConstEX.REPORT_CONSIGNER_AUTHORIZED, JUST_LEFT, cellLen));
	        line.append(Formatter.formatCell(ReportConstEX.REPORT_CONSIGNER_ACCOUNTANT, JUST_LEFT, cellLen));
	        line.append(Formatter.formatCell(ReportConstEX.REPORT_CONSIGNER_AGENT, JUST_LEFT, cellLen));
	        line.append(Formatter.formatCell(ReportConstEX.REPORT_FACTORY_TITLE + ONE_SPACE + ReportConstEX.REPORT_FACTORY,
	        		JUST_LEFT, maxLen - cellLen * 3));
	        
        if (colSize == MAX_133_LEN) {
        	tail.append(Formatter.toDocu133Line(line.toString()));
		} else if (colSize == MAX_199_LEN) {
			tail.append(Formatter.toDocu199Line(line.toString()));
		}
        line.delete(0, line.length());
        // 保存期限
	    line.append(Formatter.formatCell(String.valueOf(ONE_SPACE), JUST_LEFT, cellLen * 2 + 20));
	    line.append(Formatter.formatCell(ReportConstEX.REPORT_EXPIRATION + year + ReportConstEX.TW_YEAR, JUST_LEFT, cellLen+15));
	    line.append(Formatter.formatCell(ReportConstEX.REPORT_ID + ' ' + fsNo + ReportConstEX.PT_PREFIX, JUST_LEFT, maxLen - cellLen * 3));

        if (colSize == MAX_133_LEN) {
        	tail.append(Formatter.toDocu133Line(line.toString()));
		} else if (colSize == MAX_199_LEN) {
			tail.append(Formatter.toDocu199Line(line.toString()));
		}
        line.delete(0, line.length());
        
        return tail.toString();
	}	
	
	private static boolean isValidLength(int colSize) {
		boolean isValid = false;
		int[] validLength = {MAX_133_LEN, MAX_199_LEN};
		for (int i = 0; i < validLength.length; i++) {
			if (colSize == validLength[i]) {
				return true;
			}
		}
		return isValid;
	}
	/**
	 * 取得『資料列印結束』的字串，給133的報表使用。
	 * @return 一行分隔線 "----" 一行 " ***  資料列印結束  ***  " 共兩行資料
	 */
	public static String get133ReportEnd() {
		StringBuffer endOfLine = new StringBuffer();
		endOfLine.append(Formatter.toDocu133Line(padChar(HYPHEN, MAX_133_LEN), Formatter.JUST_LEFT));
		endOfLine.append(Formatter.toDocu133Line(ReportConst.REPORT_END_OF_LINE, Formatter.JUST_CENTRE));
		return endOfLine.toString();
	}
	
	/**
	 * 取得『資料列印結束』的字串，給133EX的報表使用。
	 * @return 一行分隔線 "----" 一行 " ***  資料列印結束  ***  " 共兩行資料
	 */
	public static String get133ReportEndEX() {
		StringBuffer endOfLine = new StringBuffer();
		endOfLine.append(Formatter.toDocu133Line(padChar(HYPHEN, MAX_133_LEN), Formatter.JUST_LEFT));
		endOfLine.append(Formatter.toDocu133Line(ReportConstEX.REPORT_END_OF_LINE, Formatter.JUST_CENTRE));
		return endOfLine.toString();
	}
	
	/**
	 * 取得『資料列印結束』的字串，給199的報表使用。
	 * @return 一行分隔線 "----" 一行 " ***  資料列印結束  ***  " 共兩行資料
	 */
	public static String get199ReportEnd() {
		StringBuffer endOfLine = new StringBuffer();
		endOfLine.append(Formatter.toDocu199Line(padChar(HYPHEN, MAX_199_LEN), Formatter.JUST_LEFT));
		endOfLine.append(Formatter.toDocu199Line(ReportConst.REPORT_END_OF_LINE, Formatter.JUST_CENTRE));
		return endOfLine.toString();
	}
	
	/**
	 * 取得『資料列印結束』的字串，給199EX的報表使用。
	 * @return 一行分隔線 "----" 一行 " ***  資料列印結束  ***  " 共兩行資料
	 */
	public static String get199ReportEndEX() {
		StringBuffer endOfLine = new StringBuffer();
		endOfLine.append(Formatter.toDocu199Line(padChar(HYPHEN, MAX_199_LEN), Formatter.JUST_LEFT));
		endOfLine.append(Formatter.toDocu199Line(ReportConstEX.REPORT_END_OF_LINE, Formatter.JUST_CENTRE));
		return endOfLine.toString();
	}
	
	/**
	 * 根據傳入的Array，將其轉換成CPS所需要的一行格式
	 * 例如："字串",數字,數字,"字串",
	 * @param columns 要轉換的欄位
	 * @return 例如："字串",數字,數字,"字串" + 換行字元
	 */
	public static String toCPSLine(Object[] columns) {
		{
			if (columns == null || columns.length < 1) {
				return null; 
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < columns.length; i++) {
				{
					if (columns[i] instanceof java.lang.String) {
						if (((String) columns[i]).trim().length() > 0) {//長度不為零
							sb.append('"').append(((String) columns[i]).trim()).append('"');
						}
					} else {
						if (columns[i] != null) {
							sb.append(columns[i]);
						}
					}
				}
				sb.append(',');
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1); 
			}
			sb.append(NEW_LINE);
			return sb.toString();
		}
	}
	/**
	 * 根據傳入的String，計算String長度
	 * 例如："字串",數字,數字,"字串",
	 * @param inputStr 要計算長度的欄位
	 * @return 例如：長度數字
	 */
	public static int LenString(String inputStr){
	    // 運用技巧： Integer.parseInt(Integer.toString(inputStr.charAt(i),16) : 指定字串特定位元, 並將其轉換成 16 進位的內碼值
	    int strLen = 0;
	    int inputStrLen = 0;
	    int i = 0;
	    if (inputStr != null){
	        strLen+=2; //中文字加2個byte  	    	
	       inputStrLen = inputStr.length();      
	       while (inputStrLen > i){
	        //判斷該字元是否為雙位元資料
	          if (Integer.parseInt(Integer.toString(inputStr.charAt(i),16) ,16) >= 128  ) { 
	              strLen = strLen + 2;  
	          }else{ 
	              strLen++;
	          }
	      i++;
	      }
	    }
	    return strLen;
	    
	  } 
}

