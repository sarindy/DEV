/*
 * Created on Aug 12, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ucb.batch.support.dataAccessor.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ucb.batch.AbstractBatch;
import ucb.batch.support.row.BranchRow;
import ucb.batch.util.dataSource.ConnectionManager;
import ucb.batch.util.dataSource.DataSourceUtility;

/**
 * @author UCH
 * @since 2008-08-12
 * @modified
 * 2010-09-26
 *
 * Get Branch Data
 */
public class BranchDataAccessor extends AbstractBatch {

	private static final String CALL_SP_GET_EFBRANCH = "{call SP_GET_EFBRANCH()}";
	private static final String CALL_SP_EFSYNC_BRANCH = "{call SP_EFSYNC_BRANCH()}";
	private static final String NAME = "NAME";
	private static final String BRANCH_ID = "BRANCH_ID";
	/**
	 * Get Branch Data
	 */
	
	public List<BranchRow> getBranches() {
	    
	    List<BranchRow> arrayList = new ArrayList<BranchRow>();
	    Connection conn = ConnectionManager.getConnection1();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(CALL_SP_GET_EFBRANCH);
            rs = st.executeQuery();
            while (rs.next()) {
                arrayList.add(new BranchRow(rs.getString(BRANCH_ID).trim(), rs.getString(NAME).trim()));
            }
        } catch (SQLException e) {
            logger.error(this, e);
        }        
        DataSourceUtility.closeResultSet(rs);
        DataSourceUtility.closeStatment(st);
        DataSourceUtility.closeConnection(conn);
        
        return arrayList;
	}
	
	/**
	 * 
	 */
	public boolean syncBranches() throws Exception {
	    
	    boolean returnValue = false;
        Connection conn = ConnectionManager.getConnection1();
        PreparedStatement st = null;
        //try {
            st = conn.prepareStatement(CALL_SP_EFSYNC_BRANCH);
            returnValue = !st.execute();
        //} catch (SQLException e) {
        //    logger.error(this, e);
        //}
        DataSourceUtility.closeStatment(st);
        DataSourceUtility.closeConnection(conn);
        
        return returnValue;
	}
}
