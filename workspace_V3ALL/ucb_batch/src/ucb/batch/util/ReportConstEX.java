/*
 * Created on Aug 5, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ucb.batch.util;
import java.util.Calendar;
import java.util.Date;
import java.text.*;

/**
 * @author LSC
 * @see 報表常數檔：報表名稱, 表頭, 表尾, 及報表頁面控制碼
 * @see 日期工具：系統當日中華民國日期及西元日期
 */
public abstract class ReportConstEX {
	/**
	 * 常數
	 */
	public static final String REPORT_CODE_99 = "EXECUTION REPORT CODE FAIL！";// 報表程式執行失敗
	public static final String REPORT_CODE_0 = "EXECUTION REPORT CODE SUCCESSFUL！"; // 報表程式執行成功
	public static final String REPORT_CODE_1 = "NO DATA！";// 無資料

	/** BEGIN 報表名稱 **/
	public static final String REPORT_TITLE_ESUN = "E.SUN Commercial Bank";// 玉 山 銀 行
	public static final String REPORT_TITLE_UCB1001 = "CUSTOMER INFORMATION";	
	
    /** END 報表名稱 **/
    
    /** BEGIN 報表表頭 **/
    /**  
     * 常數：分行代號：
    **/
    public static final String REPORT_BRANCH = "BRANCH CODE：";
    /**  
     * 常數：頁次：
    **/
    public static final String REPORT_PAGE_NO = "PAGE NO：";
    /**  
     * 常數：中華民國國號
    **/
    public static final String REPORT_NATIONAL = "REPUBLIC OF CHINA";
    /**  
     * 常數：資料日期
    **/
    public static final String REPORT_DATA_DATE = "  DATE：";
    /**  
     * 常數：報表產生時間
    **/
    public static final String REPORT_PRINT_DATE = "REPORT GENERATION TIME：";
    /** END 報表表頭 **/    

    /** BEGIN 報表行列控制 **/
    /**  
     * 常數：報表最大資料列數
    **/
    public static final int REPORT_LINE_PAGE = 54;
    /**  
     * 常數：報表最大資料列數
    **/
    public static final int MAX_LINES = 53;
    /**  
     * 常數：報表分割符號"="
    **/    
    public static final char REPORT_SPLITTER_1 = '=';
    /**  
     * 常數：報表分割符號"-"
    **/    
    public static final char REPORT_SPLITTER_2 = '-';
    /**  
     * 常數：報表分頁控制器"1"
    **/
    public static final char REPORT_PAGE_CTRL = '1';
    /** END 報表行列控制 **/
    
    /** BEGIN 報表表尾 **/
    /**  
     * 常數：保存年限：
    **/
    public static final String REPORT_EXPIRATION = "EXPIRATION DATE：";
    /**  
     * 常數：報表代號：
    **/    
    public static final String REPORT_ID = "REPORT CODE：";
    /**  
     * 常數：經（副）理：
    **/    
    public static final String REPORT_CONSIGNER_MANAGER_1 = " MANAGER(ASSISTANT)：";
    /**  
     * 常數：襄理：
    **/    
    public static final String REPORT_CONSIGNER_MANAGER_2 = " ASSISTANT MANAGER：";
    /**  
     * 常數：會計：
    **/    
    public static final String REPORT_CONSIGNER_ACCOUNTANT = "ACCOUNTANT：";
    /**  
     * 常數：覆核
    **/
    public static final String REPORT_CONSIGNER_AUTHORIZED = "AUTHORIZED：";
    /**  
     * 常數：經辦
    **/
    public static final String REPORT_CONSIGNER_AGENT = "TELLER：";
    /**  
     * 常數：製表單位
    **/
    public static final String REPORT_FACTORY_TITLE = "THE UNIT OF REPORT GENERATE：";
    
    /**  
     * 單位
    **/
    public static final String UNIT = "UNIT：";
    
    /**  
     * 常數：資訊處
    **/
    public static final String REPORT_FACTORY = "INFORMATION TECHNOLEGY DIVISION";
    /**  
     * 常數：資料列印結束
    **/
    public static final String REPORT_END_OF_LINE = "*** THE END OF PRINT ***";
    /**  
     * 常數：本日無資料
    **/
    public static final String REPORT_TODAY_NO_DATA = "NO CASES TODAY!!";
    /** END 報表表尾 **/
    /**
     * 常數：PT
     */
    public static final String PT_PREFIX = "PT ";
    /**
     * 常數：保存期限五年
     */
    public static final String REPORT_FIVE_YEAR = "FIVE";
    /**
     * 常數：保存期限十年
     */
    public static final String REPORT_TEN_YEAR = "TEN";
    /**
     * 常數：保存期限十五年
     */
    public static final String REPORT_FIFTEEN_YEAR = "FIFTEEN";
    /**
     * 常數：保存期限二十年
     */
    public static final String REPORT_TWENTY_YEAR = "TWENTY";
    /**
     * 常數：年
     */
	public static final String TW_YEAR = " YEAR";
	/**
	 * 常數：月
	 */
	public static final String TW_MONTH = " MONTH";
	/**
	 * 常數：日
	 */
	public static final String TW_DAY = " DAY";
	 
	
	/**
	 * 列印期間
	 */
	public static final String PRINT_DATE = "PRINTING PERIOD";
	
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
    /** 
	 * 中華民國的年月日
	 * @return 傳回yy年mm月dd日 字串
	 */    
    public static String getToday(){
        return Integer.toString(Calendar.getInstance().get(Calendar.YEAR) - 1911) + TW_YEAR + ' ' +
        Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1) + TW_MONTH + ' ' + 
        Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + TW_DAY ;
    }
    
    /** 
	 * 西元Anno Domini的年月日
	 * @return 傳回yyyy/mm/dd 字串
	 */  
    public static String getAdToday(){
        return getAdTodayDateTime().substring(0, 10);
    }
    
    /** 
	 * 西元Anno Domini的年月日時分秒
	 * @return 傳回yyyy/mm/dd HH:mm:ss 字串
	 */  
    public static String getAdTodayDateTime(){                
        DateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return formatter.format(new Date());
    }    
}
