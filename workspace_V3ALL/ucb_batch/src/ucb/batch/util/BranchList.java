/*
 * Created on Aug 28, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ucb.batch.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ucb.batch.AbstractBatch;
import ucb.batch.support.dataAccessor.imp.BranchDataAccessor;
import ucb.batch.support.row.BranchRow;

/**
 * @author UCH
 * 
 * 分行相關資料
 */
public class BranchList extends AbstractBatch {

    private final String CONFIG_FILENAME = "/batch-config.xml";
	private static final String CORP_CENTER_PREFIX = "C5";
	private static final String BRANCH_FILE_PATH = "BRANCH";
	private static final String FILE_FORMAT_ERROR = "檔案格式有問題。";
	private BranchDataAccessor branchDao = new BranchDataAccessor();
	private HashMap<String, BranchRow> branches = null; // 存放取得的分行

	public BranchList() {
	}	
	
	/**
	 * 同步FXDB.DTFX_BRANCH和FXDB.DTFX_EF_BRANCH。
	 * CQV300000141@HJS 將SQL Exception throw出去，由外面判斷
	 * @throws Exception 
	 */
	public void sync() throws Exception {
		branchDao.syncBranches();
	}

	/**
	 * 重新抓取資料庫的分行資訊，產生分行資料檔。
	 *  
	 */
	public void reGenFile() {
		File file = new File(getFilePath());
		try {
			genFile(file);
		} catch (IOException ioe) {
		    logger.error(this, ioe);
		}
	}

	/**
	 * 初始化內部所含的HashMap，如果分行檔存在，就從分行檔讀取。 如果分行檔不存在，就從資料庫抓，並重新產生一個檔案。
	 */
	private void initList() {
		File file = new File(getFilePath());
		if (file.exists()) {
			try {
				getListFromFile(file);
			} catch (IOException ioe) {
			    logger.error(this, ioe);
			}
		} else {
			try {
				genFile(file);
			} catch (IOException ioe) {
			    logger.error(this, ioe);
			}
		}
	}

	/**
	 * 從CSV檔案中，讀取分行的資料。
	 * 
	 * @param file
	 *            存放分行資料的檔案，檔案路徑 + 檔名。
	 * @throws IOException
	 */	
    private void getListFromFile(File file) throws IOException {
		if (branches != null) {
			branches.clear();
		} else {
			branches = new HashMap<String, BranchRow>();
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.length() > 1) {
				String[] data = line.split(String.valueOf(','));
				if (data.length < 2) {
					throw new IOException(FILE_FORMAT_ERROR);
				}
				branches.put(data[0], new BranchRow(data[0], data[1]));
			} // end if
		} // end while
		br.close();
	}

	/**
	 * 從資料庫中取出分行的資料，然後將資料以CSV的格式，寫入檔案中。
	 * 
	 * @param file
	 *            檔案路徑 + 檔名
	 * @throws IOException
	 */
    private void genFile(File file) throws IOException {
		if (branches != null) {
			branches.clear();
		} else {
			branches = new HashMap<String, BranchRow>();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		List<BranchRow> branchList = branchDao.getBranches();
		int size = branchList.size();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			BranchRow row = (BranchRow) branchList.get(i);
			branches.put(row.getBranchId(), row);
			sb.append(row.getBranchId()).append(String.valueOf(',')).append(row.getName())
					.append(Formatter.NEW_LINE);
			bw.write(sb.toString());
			sb.delete(0, sb.length());
		}
		bw.flush();
		bw.close();
	}

	/**
	 * 取得檔案存放的位置
	 * 
	 * @return 檔案路徑 + 檔名
	 */
	private String getFilePath() {
	    Configuration config = new Configuration(CONFIG_FILENAME);
	    String branchListFilePath = config.getBranchListFilePath() + BRANCH_FILE_PATH;
	    logger.info("Branch List File Path : " + branchListFilePath);
	    return branchListFilePath;
	}

	/**
	 * 取得分行資訊組成的List
	 * 
	 * @return List of BranchRow
	 */
	public List<BranchRow> getList() {
		if (branches == null) {
			initList();
		}
		return new ArrayList<BranchRow>(branches.values());
	}
	
	/**
	 * 取得沒有企金中心的分行代號組成的分行資訊組成的List
	 * 
	 * @return List of BranchRow
	 */
	public List<BranchRow> getNoCorpCenterList() {
		if (branches == null) {
			initList();
		}
		List<BranchRow> list = new ArrayList<BranchRow>(branches.values());
		List<BranchRow> newList = new ArrayList<BranchRow>(list.size());
		int size = list.size();
		for (int i = 0; i < size; i++) {
			BranchRow row = (BranchRow) list.get(i);
			if (!row.getBranchId().startsWith(CORP_CENTER_PREFIX)) {
				newList.add(row);
			}
		}
		return newList;
	}

	/**
	 * 根據分行代號，取得該分行的資料
	 * 
	 * @param branchId
	 *            分行代號
	 * @return 分行資料
	 */
	public BranchRow getBranch(String branchId) {
		if (branches == null) {
			initList();
		}
		return (BranchRow) branches.get(branchId);
	}

	/**
	 * 根據分行代號取得分行名稱
	 * 
	 * @param branchId
	 *            分行代號
	 * @return 分行名稱
	 */
	public String getName(String branchId) {
		if (branches == null) {
			initList();
		}
		BranchRow row = (BranchRow) branches.get(branchId);
		return (row != null) ? row.getName() : null;
	}

	/**
	 * 根據分行名稱取得分行代號
	 * 
	 * @param name
	 *            分行名稱
	 * @return 分行代號
	 */
	public String getBranchId(String name) {
		if (branches == null) {
			initList();
		}
		List<BranchRow> list = new ArrayList<BranchRow>(branches.values());
		int size = list.size();
		for (int i = 0; i < size; i++) {
			BranchRow row = (BranchRow) list.get(i);
			if (name != null && name.equals(row.getName())) {
				return row.getBranchId();
			}
		}
		return null;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		char left = '[';
		char right = ']';
		char semicolon = ';';
		
		List<BranchRow> list = getList();
		if (list != null) {
			Iterator<BranchRow> i = list.iterator();
			while (i.hasNext()) {
				sb.append(left).append(i.next()).append(right);
			}
			sb.append(semicolon);
		}
		
		return sb.toString();
	}
}
