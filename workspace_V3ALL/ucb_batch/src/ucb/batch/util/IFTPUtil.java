/*
 * Created on Sep 3, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ucb.batch.util;

import java.io.OutputStream;

/**
 * @author UCH
 *
 * FTP最基本的介面
 */
public interface IFTPUtil {
	/**
	 * Default TimeOut 的時間：五分鐘
	 */
	public static final int TIME_OUT = 5 * 60 * 1000;
	/**
	 * 登入失敗時，預設的重試次數
	 */
	public static final int RETRY_TIMES = 2;
	/**
	 * 取得遠端的工作目錄
	 * @return FTP端的工作目錄，如/QDLS/DOCRPT
	 */
	public String getWorkingDirectory();
	/**
	 * 取得本地端存放檔案的路徑，包含最後的路徑分隔符號
	 * @return 本地端存放檔案的目錄路徑，如C:/report/
	 */
	public String getLocalDirectory();
	/**
	 * 連線
	 */
	public boolean connect();
	/**
	 * 斷線
	 */
	public boolean disconnect();
	/**
	 * 將檔案從FTP伺服器下載至本地端，會蓋掉原本已經存在的檔案。
	 * 在傳送前，會先呼叫connect()，傳送完成，會呼叫disconnect()。
	 * @param fileName	檔案名稱，完整的檔名為getLocalDirectory() + fileName 
	 * @return 是否傳送成功
	 */
	public boolean retrieveDirectly(String fileName);
	/**
	 * 將檔案從FTP伺服器下載至本地端，會蓋掉原本已經存在的檔案。
	 * 在傳送前，需要先呼叫connect()，傳送後，如果想要斷線，需要呼叫disconnect()。
	 * @param fileName	檔案名稱，完整的檔名為getLocalDirectory() + fileName 
	 * @return 是否傳送成功
	 */
	public boolean retrieveFile(String fileName, OutputStream fileOutputStream);
	/**
	 * 將檔案從FTP伺服器下載轉成OutputStream。
	 * @param fileName	檔案名稱，完整的檔名為getLocalDirectory() + fileName 
	 * @return 是否傳送成功
	 */
	public boolean retrieve(String fileName);	
	/**
	 * 將檔案傳送至FTP伺服器，會蓋掉原本已經存在的檔案。
	 * 在傳送前，會先呼叫connect()，傳送完成，會呼叫disconnect()。
	 * @param fileName	檔案名稱，完整的檔名為getLocalDirectory() + fileName 
	 * @return 是否傳送成功
	 */
	public boolean sendDirectly(String fileName);
	/**
	 * 將檔案傳送至FTP伺服器，會蓋掉原本已經存在的檔案。
	 * 在傳送前，需要先呼叫connect()，傳送後，如果想要斷線，需要呼叫disconnect()。
	 * @param fileName	檔案名稱，完整的檔名為getLocalDirectory() + fileName 
	 * @return 是否傳送成功
	 */
	public boolean send(String fileName);
	/**
	 * 將檔案傳送至FTP伺服器，會蓋掉原本已經存在的檔案。
	 * 在傳送前，會先呼叫connect()，傳送完成，會呼叫disconnect()。
	 * @param srcFile 來源檔案名稱，完整的檔名為getLocalDirectory() + srcFile，檔案名稱可包含路徑。
	 * @param destFile 來源檔案名稱，完整的檔名為getLocalDirectory() + destFile，檔案名稱可包含路徑。
	 * @return 是否傳送成功
	 */
	public boolean sendDirectly(String srcFile, String destFile);
	/**
	 * 將檔案傳送至FTP伺服器，會蓋掉原本已經存在的檔案。
	 * 在傳送前，需要先呼叫connect()，傳送後，如果想要斷線，需要呼叫disconnect()。
	 * @param srcFile 來源檔案名稱，完整的檔名為getLocalDirectory() + srcFile，檔案名稱可包含路徑。
	 * @param destFile 來源檔案名稱，完整的檔名為getLocalDirectory() + destFile，檔案名稱可包含路徑。
	 * @return 是否傳送成功
	 */
	public boolean send(String srcFile, String destFile);
	/**
	 * 將檔案傳送至FTP伺服器，附加至原本的檔案後面。
	 * 在傳送前，會先呼叫connect()，傳送完成，會呼叫disconnect()。
	 * @param appendFileName	遠端FTP伺服器上的檔案名稱 
	 * @param sourceFileName	本地端的檔案名稱，完整的檔名為getLocalDirectory() + sourceFileName 
	 * @return 是否傳送成功
	 */
	public boolean appendDirectly(String appendFileName, String sourceFileName);
	/**
	 * 將檔案傳送至FTP伺服器，附加至原本的檔案後面。
	 * 在傳送前，需要先呼叫connect()，傳送後，如果想要斷線，需要呼叫disconnect()。
	 * @param appendFileName	遠端FTP伺服器上的檔案名稱 
	 * @param sourceFileName	本地端的檔案名稱，完整的檔名為getLocalDirectory() + sourceFileName 
	 * @return 是否傳送成功
	 */
	public boolean append(String appendFileName, String sourceFileName);
	/**
	 * 檢查檔案是否存在FTP伺服器上。
	 * 在檢查前，需要先呼叫connect()，檢查後，如果想要斷線，需要呼叫disconnect()。
	 * @param fileName 要檢查的檔案名稱
	 * @return 存在則傳回true。
	 */
	public boolean isExist(String fileName);
	/**
	 * 將檔案從FTP伺服器刪除。
	 * 在刪除前，需要先呼叫connect()，刪除後，如果想要斷線，需要呼叫disconnect()。
	 * @param fileName 要刪除的檔案名稱
	 * @return 是否成功刪除。
	 */
	public boolean delete(String fileName);
	/**
	 * 將檔案從目錄中刪除。
	 * @param fileName 要刪除的檔案名稱
	 * @return 是否成功刪除。
	 */
	public boolean deleteFile(String fileName);
	/**
	 * 建立檔案目錄
	 * @param pathname 檔案目錄路徑，可以為絕對路徑，或是相對路徑。資料夾以 / 分隔。
	 * @return 如果目錄不存在，就建立；如果給的路徑有好幾層，會先從最上層開始檢核，目錄不存在，就建立。
	 * 建立成功，或目錄己存在，傳回 true。目錄建立失敗，傳回 false 。
	 */
	public boolean makeDirectory(String pathname);
	
	/**
	 * 取回指定目錄下所有檔名
	 * @return 指定目錄下所有檔名
	 */
	public String[] listNames();
	
	public String getFtpPass();
	/**
	 * 設定登入 FTP 伺服器的使用者密碼
	 * @param ftpPass
	 */
	public void setFtpPass(String ftpPass);
	public String getFtpServer();
	/** 
	 * 設定FTP 伺服器位址
	 * @param ftpServer 
	 */
	public void setFtpServer(String ftpServer);
	public String getFtpUser();
	/** 
	 * 設定登入 FTP 伺服器的使用者名稱
	 * @param ftpUser 
	 */
	public void setFtpUser(String ftpUser);
	/**
	 * 設定本地端檔案暫存的目錄
	 * @param localDirectory 本地端檔案暫存的目錄
	 */
	public void setLocalDirectory(String localDirectory);
	/**
	 * 設定 FTP 伺服器的工作目錄
	 * @param workingDirectory 工作目錄
	 */
	public void setWorkingDirectory(String workingDirectory);
	/**
	 * 設定連線時，Timeout 的時間
	 * 未設定時，為 TIME_OUT
	 * @param timeout 毫秒
	 */
	public void setTimeout(int timeout);
	/**
	 * 設定登入失敗時，重試的次數
	 * 未設定時，為 RETRY_TIMES
	 * @param retryTimes 登入失敗後，要重試幾次
	 */
	public void setRetryTimes(int retryTimes);
	/**
	 * 更改ftp檔案名稱.
	 * @param fromFileName 原始檔案名稱.
	 * @param toFileName 更改成檔案名稱.
	 * @return 是否成功更改ftp檔案名稱。
	 */
	public boolean rename(String fromFileName, String toFileName);	
}