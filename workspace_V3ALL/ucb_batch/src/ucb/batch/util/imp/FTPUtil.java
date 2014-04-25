/*
 * Created on Sep 3, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ucb.batch.util.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import ucb.batch.AbstractBatch;
import ucb.batch.util.IFTPUtil;
import ucb.batch.util.XmlParser;

/**
 * @author UCH
 *
 * 實做最基本的FTP工具
 * 1. FTPClient 每做完一個 FTP Command 會呼叫 getReply() 從伺服器取得回應，並且設定 ReplyCode。getReplyCode() 僅是回傳 ReplyCode 而已。
 * 2. setDataTimeout 會設定 FTP 資料串流的 TimeOut 時間，每次開啟 DataConnection，都會使用這個資訊去設定 server.setSoTimeout(__dataTimeout)。
 */
public class FTPUtil extends AbstractBatch implements IFTPUtil {
	private static final String FTP_SPERATOR_CHAR = "/";
	private static final char LINE_STR = '|';
	private static final String FTPUTIL_CLIENT_DISCONNECT_STR = "FTPUtil client.disconnect";
	private static final String FTPUTIL_CLIENT_LOGOUT_STR = "FTPUtil client.logout:";
	private static final String FTPUTIL_CLIENT_LOGIN_STR = "FTPUtil client.login:";
	private static final String FTPUTIL_CLIENT_CHANGEWORKINGDIRECTORY_STR = "FTPUtil client.changeWorkingDirectory:";
	private static final String FTPUTIL_CLIENT_SETFILETYPE_STR = "FTPUtil client.setFileType:";
	private static final String FTPUTIL_SERVER_REPLY_STR = "FTPUtil server reply code:";
	private static final String FTPUTIL_CLIENT_CONNECT_STR = "FTPUtil client connect ";
	private static final String FTPUTIL_CLIENT_DELETEFILE_STR = "FTPUtil client.deleteFile:";
	private static final String FTPUTIL_CLIENT_STOREFILE_STR = "FTPUtil client.storeFile:";
	private static final String FTPUTIL_CLIENT_APPENDFILE_STR = "FTPUtil client.appendFile:";
	private static final String FTPUTIL_CLIENT_RETRIEVEFILE_STR = "FTPUtil client.retrieveFile:";
	private static final String FTPUTIL_CLIENT_MKDIR_STR = "FTPUtil client.makeDirectory:";
	private static final String FTPUTIL_CLIENT_RENAME_STR = "FTPUtil client.rename:";
	private static final String FTPUTIL_DISCONNECT_STR = "FTPUtil disconnect:";
	private static final String FTPUTIL_RETRIEVE_STR = "FTPUtil retrieve:";
	private static final String FTPUTIL_SEND_STR = "FTPUtil send:";
	private static final String FTPUTIL_APPEND_STR = "FTPUtil append:";
	private static final String FTPUTIL_CONNECT_STR = "FTPUtil connect:";
	private static final String FTPUTIL_CCSID_TYPE = "TYPE C ";
	private static final String FTPUTIL_CCSID_950 = "950";
	private static final String FTPUTIL_PASSWORD_MARK = "********";
	private static final String EMPTY_STR = "";
	private static final String ftpConfigFileName = "ftpconfig.xml";

	protected String CCSID;
	protected String workingDirectory;
	protected String ftpServer;
	protected String ftpUser;
	protected String ftpPass;
	protected String localDirectory;
	protected boolean isSend;
	protected FTPClient client = null;
	protected int timeout = TIME_OUT;
	protected int retryTimes = RETRY_TIMES;
	
	public FTPUtil(String beanId) {
	    
	    String fileName = Thread.currentThread().getContextClassLoader().getResource(ftpConfigFileName).getPath();
	    logger.info("FTP Config file name : " + fileName);
	    fileName = fileName.replace("file:", "");
	    fileName = fileName.replace("/ucb_batch.jar!", "");
	    logger.info("FTP Config file name after replaced : " + fileName);
	    XmlParser xmlParser = new XmlParser(fileName, beanId); 
	    ftpServer = xmlParser.getProperty("ftpServer");
	    ftpUser = xmlParser.getProperty("ftpUser");
	    ftpPass= xmlParser.getProperty("ftpPass");
	    workingDirectory = xmlParser.getProperty("workingDirectory");
	    localDirectory = xmlParser.getProperty("localDirectory");
	    isSend = Boolean.parseBoolean(xmlParser.getProperty("isSend"));
	    CCSID = xmlParser.getProperty("CCSID");
	}
	
	public synchronized boolean retrieveDirectly(String fileName) {
		boolean isOk = false;
		logger.info(new StringBuffer(FTPUTIL_CONNECT_STR).append(isOk = connect()).toString()); // 連線
		if (isOk) {
			logger.info(new StringBuffer(FTPUTIL_RETRIEVE_STR).append(isOk = retrieve(fileName)).toString());
		}
		if (isOk) {
			logger.info(new StringBuffer(FTPUTIL_DISCONNECT_STR).append(disconnect()).toString()); //斷線
		}
		return isOk;
	}
	
	public synchronized boolean retrieve(String fileName) {
		boolean isOk = false;
		try {
			if (client.isConnected()) {
				FileOutputStream fos = new FileOutputStream(getLocalDirectory() + fileName); //建立Output Stream
				// 存放檔案
				isOk = client.retrieveFile(fileName, fos);
				logger.info(new StringBuffer(FTPUTIL_CLIENT_RETRIEVEFILE_STR)
						.append(isOk)
						.append(LINE_STR).append(fileName).toString());
				handleReplyCode(isOk);
				fos.flush();
				fos.close(); //關閉檔案
			}
		} catch (IOException ioe) {
			isOk = false;
			logger.error(ioe);
		}
		return isOk;
	}
	
	public synchronized boolean sendDirectly(String fileName) {
		return sendDirectly(fileName, fileName);
	}
	
	public synchronized boolean send(String fileName) {
		return send(fileName, fileName);
	}
	
	public synchronized boolean sendDirectly(String srcFile, String destFile) {
		boolean isOk = false;
		logger.info(new StringBuffer(FTPUTIL_CONNECT_STR).append(isOk = connect()).toString()); // 連線
		if (isOk) {
			logger.info(new StringBuffer(FTPUTIL_SEND_STR).append(isOk = send(srcFile, destFile)).toString());
		}
		if (isOk) {
			logger.info(new StringBuffer(FTPUTIL_DISCONNECT_STR).append(disconnect()).toString()); //斷線
		}				
		return isOk;
	}
	
	public synchronized boolean send(String srcFile, String destFile) {
		boolean isOk = false;
		try {
			if (client.isConnected()) {
				FileInputStream fis = new FileInputStream(getLocalDirectory() + srcFile); //建立InputStream
				// 存放檔案
				isOk = client.storeFile(destFile, fis);
				logger.info(new StringBuffer(FTPUTIL_CLIENT_STOREFILE_STR)
						.append(isOk)
						.append(LINE_STR).append(srcFile).append(LINE_STR).append(destFile).toString());
				handleReplyCode(isOk);
				fis.close(); //關閉檔案	
			}
		} catch (IOException ioe) {
			isOk = false;
			logger.error(ioe);
		}
		return isOk;
	}

	public synchronized boolean appendDirectly(String appendFileName, String sourceFileName) {
		boolean isOk = false;
		logger.info(new StringBuffer(FTPUTIL_CONNECT_STR).append(isOk = connect()).toString()); // 連線
		if (isOk) {
			logger.info(new StringBuffer(FTPUTIL_APPEND_STR).append(isOk = append(appendFileName, sourceFileName)).toString());
		}
		if (isOk) {
			logger.info(new StringBuffer(FTPUTIL_DISCONNECT_STR).append(disconnect()).toString()); //斷線
		}
		return isOk;
	}
	
	public synchronized boolean append(String appendFileName, String sourceFileName) {
		boolean isOk = false;
		
		try {
			if (client.isConnected()) {
				FileInputStream fis = new FileInputStream(getLocalDirectory() + sourceFileName); //建立InputStream
				// 存放檔案
				isOk = client.appendFile(appendFileName, fis);
				logger.info(new StringBuffer(FTPUTIL_CLIENT_APPENDFILE_STR)
						.append(isOk)
						.append(LINE_STR).append(appendFileName)
						.append(LINE_STR).append(sourceFileName).toString());
				handleReplyCode(isOk);
				fis.close(); //關閉檔案
				
						
			}
		} catch (IOException ioe) {
			isOk = false;
			logger.error(ioe);
		}
		return isOk;
	}
	
	/**
	 * @param isOk
	 */
	private void handleReplyCode(boolean isOk) {
		if (!isOk) {
			logger.info(new StringBuffer(FTPUTIL_SERVER_REPLY_STR)
				.append(client.getReplyCode()).toString());
		}
	}

	public synchronized boolean isExist(String fileName) {
		String pathPart = getPathPart(fileName); // 取得目錄路徑的部份		
		String filePart = getFilePart(fileName); // 取得檔案或最後一層資料夾的部份
		boolean isExist = false;
		
		if (EMPTY_STR.equals(filePart)) {
			return isExist;
		}		
		try {
			if (client.isConnected()) {
				
				String[] files = null;
				if (EMPTY_STR.equals(pathPart)) { //只檢查工作目錄下是否有該筆檔案或目錄
					files = client.listNames();
				} else {
					files = client.listNames(pathPart);
				}
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						if (files[i] != null && files[i].endsWith(filePart)) {
							isExist = true;
							break;
						}
					}
				}
			} // end if
		} catch (IOException ioe) {
			logger.error(ioe);
		}
		return isExist;
	}
	
	public synchronized boolean delete(String fileName) {
		boolean isOk = false;
		try {
			if (client.isConnected()) {
				isOk = client.deleteFile(fileName);
				logger.info(new StringBuffer(FTPUTIL_CLIENT_DELETEFILE_STR)
						.append(isOk)
						.append(LINE_STR).append(fileName).toString());
				handleReplyCode(isOk);
				
			}
		} catch (IOException ioe) {
			isOk = false;
			logger.error(ioe);
		}
		return isOk;
	}
	
	//Delete Files - filename
	public synchronized boolean deleteFile(String fileName) {
	    return (new File(getLocalDirectory() + fileName)).delete();
	}
	
	public synchronized boolean makeDirectory(String pathname) {
		if (pathname == null || (pathname.startsWith(FTP_SPERATOR_CHAR) && pathname.length() < 2)) {
			return false;
		}
		boolean isOk = false;
		boolean isAbsolutePath = pathname.startsWith(FTP_SPERATOR_CHAR); // 是否為絕對路徑, '/'開頭
		
		String[] paths = null; // 將路徑拆解
		StringBuffer currentPath = new StringBuffer();
		if (isAbsolutePath) { // 如果是絕對路徑
			paths = pathname.substring(1, pathname.length()).split(FTP_SPERATOR_CHAR);
			currentPath.append(FTP_SPERATOR_CHAR);
		} else {
			paths = pathname.split(FTP_SPERATOR_CHAR);
		}
		
		try {
			if (client.isConnected()) {
				for (int i = 0; i < paths.length; i++) {
					currentPath.append(paths[i]);
					boolean isExist = isExist(currentPath.toString());
					if (!isExist) {
						isOk = client.makeDirectory(currentPath.toString());
						logger.info(new StringBuffer(FTPUTIL_CLIENT_MKDIR_STR)
								.append(isOk)
								.append(LINE_STR).append(pathname).toString());
						if (!isOk) {
							logger.info(new StringBuffer(FTPUTIL_SERVER_REPLY_STR)
								.append(client.getReplyCode()).toString());
						}
					} else {
						isOk = true;
					}
					currentPath.append('/');
				}
			}
		} catch (IOException ioe) {
			isOk = false;
			logger.error(ioe);
		}
		return isOk;
	}
	
	public synchronized boolean connect() {
		boolean isOk = false;
		int connCount = 1; // 連線次數
		while (!isOk && connCount - 1 <= retryTimes) {
			client = new FTPClient();
			client.setDefaultTimeout(timeout); // 設定 time out 的時間
			client.setDataTimeout(timeout); // 設定資料串流的 time out 的時間。
			try {
				client.connect(ftpServer); //連線
				logger.info(new StringBuffer(FTPUTIL_CLIENT_CONNECT_STR).append(ftpServer)
						.append(LINE_STR).append(connCount).toString());
				// 登入
				logger.info(new StringBuffer(FTPUTIL_CLIENT_LOGIN_STR)
						.append(isOk = client.login(ftpUser, ftpPass))
						.append(LINE_STR).append(ftpUser).append(LINE_STR).append(FTPUTIL_PASSWORD_MARK).toString());
				int reply = client.getReplyCode();
				logger.info(new StringBuffer(FTPUTIL_SERVER_REPLY_STR).append(reply));
				if (!FTPReply.isPositiveCompletion(reply)) { //回應錯誤，斷線。
					isOk = false;
					client.disconnect();
				}
			
				// 設定檔案傳輸型態
				if (this.CCSID != null && FTPUTIL_CCSID_950.equalsIgnoreCase(this.CCSID)) {
					if (FTPReply.isPositiveCompletion(reply) && client.isConnected()) {
						logger.info(new StringBuffer(FTPUTIL_CLIENT_SETFILETYPE_STR) // 設定傳檔型態
								.append(client.sendCommand(FTPUTIL_CCSID_TYPE + FTPUTIL_CCSID_950)) // 設定 CCSID 950
								.append((isOk = client.setFileType(FTPClient.LOCAL_FILE_TYPE)))
								.append(LINE_STR).append(FTPClient.LOCAL_FILE_TYPE).toString());
						handleReplyCode(isOk);
					}			    
				} else {
					if (FTPReply.isPositiveCompletion(reply) && client.isConnected()) {
						logger.info(new StringBuffer(FTPUTIL_CLIENT_SETFILETYPE_STR) // 設定傳檔型態
								.append((isOk = client.setFileType(FTPClient.BINARY_FILE_TYPE)))
								.append(LINE_STR).append(FTPClient.BINARY_FILE_TYPE).toString());
						handleReplyCode(isOk);
					}   
				}			    
			
				if (isOk) { // 變更工作目錄。
					logger.info(new StringBuffer(FTPUTIL_CLIENT_CHANGEWORKINGDIRECTORY_STR)
							.append((isOk = client.changeWorkingDirectory(workingDirectory)))
							.append(LINE_STR).append(workingDirectory).toString());
					handleReplyCode(isOk);
				}
			} catch (SocketException e) {
				isOk = false;
				logger.error(e);
			} catch (IOException e) {
				isOk = false;
				logger.error(e);
			} // end try
			connCount++;
		} // end while
		
		return isOk;
	}

	public synchronized boolean disconnect() {
		boolean isOk = false;
		if (client == null) {
		    return isOk;
		}
	    try {
	    	if (client.isConnected()) {
	    		logger.info(new StringBuffer(FTPUTIL_CLIENT_LOGOUT_STR)
						.append((isOk = client.logout())).toString());
	    	}
		} catch (IOException ex) {
			isOk = false;
			logger.error(ex);
		} finally {
			if (client.isConnected()) {
				try {
					client.disconnect();
					logger.info(FTPUTIL_CLIENT_DISCONNECT_STR);
					isOk = true;
				} catch (IOException e) {
					isOk = false;
					logger.error(e);
				}
			}
		}
		client = null;
		
		return isOk;
	}
	
	public synchronized String[] listNames(){
	    boolean isOk = false;
	    String[] fileNames = null;
		logger.info(new StringBuffer(FTPUTIL_CONNECT_STR).append(isOk = connect()).toString()); // 連線
		if (isOk) {
		    try{
		        fileNames = client.listNames();
		    }catch(IOException ex){		        
		    }		    
		}
		if (isOk) {
			logger.info(new StringBuffer(FTPUTIL_DISCONNECT_STR).append(disconnect()).toString()); //斷線
		}
		return fileNames;
	}
	
	public boolean retrieveFile(String fileName, OutputStream fileOutputStream){
	    boolean isOk = false;
		logger.info(new StringBuffer(FTPUTIL_CONNECT_STR).append(isOk = connect()).toString()); // 連線
		if (isOk) {
		    try{
		        isOk = client.retrieveFile(fileName, fileOutputStream);
		    }catch(IOException ex){		        
		    }		    
		}
		if (isOk) {
			logger.info(new StringBuffer(FTPUTIL_DISCONNECT_STR).append(disconnect()).toString()); //斷線
		}
		return isOk;
	}
	
	public String getFtpPass() {
		return ftpPass;
	}
	public synchronized void setFtpPass(String ftpPass) {
		this.ftpPass = ftpPass;
	}
	public String getFtpServer() {
		return ftpServer;
	}
	public synchronized void setFtpServer(String ftpServer) {
		this.ftpServer = ftpServer;
	}
	public String getFtpUser() {
		return ftpUser;
	}
	public synchronized void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}
	public String getLocalDirectory() {
		return new File(localDirectory).getAbsolutePath() + File.separatorChar;
	}
	public synchronized void setLocalDirectory(String localDirectory) {
		this.localDirectory = localDirectory;
	}
	public String getWorkingDirectory() {
		return workingDirectory;
	}
	public synchronized void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}	
    public String getCCSID() {
        return CCSID;
    }
	public synchronized void setCCSID(String ccsid) {
	    this.CCSID = ccsid;
	}
	public synchronized void setTimeout(int timeout) {
		if (timeout > -1) {
			this.timeout = timeout;
		}
	}
	public synchronized void setRetryTimes(int retryTimes) {
		if (retryTimes > -1) {
			this.retryTimes = retryTimes;
		}
	}

	/**
	 * 取得 FilePath 的路徑部份
	 * @param filePath
	 * @return
	 */
	private static String getPathPart(String filePath) {
		String newFilePath = correntFilePath(filePath);
		
		int lastIndex =  newFilePath.lastIndexOf(FTP_SPERATOR_CHAR);
		int firstIndex = newFilePath.indexOf(FTP_SPERATOR_CHAR);
		
		if (newFilePath.startsWith(FTP_SPERATOR_CHAR) && firstIndex == lastIndex) { // 包含根目錄，但只有一層
			return newFilePath.substring(0, 1); 
		} else if (lastIndex > 0) {
			return newFilePath.substring(0, lastIndex); 
		} else {
			return "";
		}
	}
	
	/**
	 * 如果為正確的 File Path，則回傳該 File Path，最後有/會去掉。
	 * 如果為不正確的 File Path，則回傳空白
	 * @param filePath
	 * @return
	 */
	private static String correntFilePath(String filePath) {
		boolean isOk = false;
		String tmpFilePath = "";
		StringBuffer currentPath = new StringBuffer();
		if (filePath == null || filePath.length() < 1 || (filePath.startsWith(FTP_SPERATOR_CHAR) && filePath.length() < 2)) {
			return EMPTY_STR;
		}
		if (filePath.startsWith(FTP_SPERATOR_CHAR)) {
		    tmpFilePath = filePath.substring(1);
			currentPath.append(FTP_SPERATOR_CHAR);
		}
		String[] paths = tmpFilePath.split(FTP_SPERATOR_CHAR);
		for (int i = 0; i < paths.length; i++) { // 檢查path的每段長度是否都大於零
			if (paths[i].length() > 0) {
				isOk = true;
				currentPath.append(paths[i]).append(FTP_SPERATOR_CHAR);
			} else {
				isOk = false;
				break;
			}
		}
		if (isOk) {
			return currentPath.deleteCharAt(currentPath.length() - 1).toString();
		} else {
			return EMPTY_STR;
		}
	}
	
	/**
	 * 取得 FilePath 的檔案或最後一層目錄部份
	 * @param filePath
	 * @return
	 */
	private static String getFilePart(String filePath) {
		String newFilePath = correntFilePath(filePath);
		
		int lastIndex =  newFilePath.lastIndexOf(FTP_SPERATOR_CHAR);
		
		if (lastIndex >= 0) {
			return newFilePath.substring(lastIndex + 1, newFilePath.length()); 
		} else {
			return newFilePath;
		}
	}
	
	/**
	 * 更改ftp檔案名稱.
	 * @param fromFileName 原始檔案名稱.
	 * @param toFileName 更改成檔案名稱.
	 * @return 是否成功更改ftp檔案名稱。
	 */
	public boolean rename(String fromFileName, String toFileName){
		boolean isOk = false;
		try {
		    logger.info(new StringBuffer(FTPUTIL_CONNECT_STR).append(isOk = connect()).toString()); // 連線
			if (isOk && client.isConnected()) {
				isOk = client.rename(fromFileName, toFileName);
				logger.info(new StringBuffer(FTPUTIL_CLIENT_RENAME_STR)
						.append(isOk)
						.append(LINE_STR).append("from:"+fromFileName)
						.append(" to:"+toFileName).toString());
				handleReplyCode(isOk);
				
			}
		} catch (IOException ioe) {
			isOk = false;
			logger.error(ioe);
		} finally {
		    logger.info(new StringBuffer(FTPUTIL_DISCONNECT_STR).append(disconnect()).toString()); //斷線
		}
		return isOk; 
	}
	
	public synchronized void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }
    public boolean getIsSend() {
        return this.isSend;
    }
}
