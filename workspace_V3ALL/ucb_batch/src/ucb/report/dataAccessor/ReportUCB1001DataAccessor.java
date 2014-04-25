package ucb.report.dataAccessor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import ucb.batch.AbstractBatch;
import ucb.batch.util.ParameterContext;
import ucb.batch.util.dataSource.ConnectionManager;
import ucb.batch.util.dataSource.DataSourceUtility;
import ucb.report.row.UCB1001Row;

/**
 * @author Shuan Tsai
 * @since 2014-1-23
 */
public class ReportUCB1001DataAccessor extends AbstractBatch {
	private static final String CUST_KEY_CHAR = "CUST_KEY_CHAR";
	private static final String CUST_KEY_NUM = "CUST_KEY_NUM";
	private static final String NAME = "NAME";
	private static final String NRIC = "NRIC";
	private static final String DOB = "DOB";
	private static final String ZIP_CODE = "ZIP_CODE";
	private static final String SEX = "SEX";
	private static final String H_PHONE = "H_PHONE";
	private static final String O_PHONE = "O_PHONE";
	private static final String FAX = "FAX";
	private static final String OCCUPATION = "OCCUPATION";
	private static final String CALL_SP_CUST_INFO = "{call SP_CUST_INFO(?)}";
	
	public List<UCB1001Row> getReportUCB1001QueryResult(ParameterContext context) {
	    
	    List<UCB1001Row> arrayList = new ArrayList<UCB1001Row>(); 
	    Connection conn = ConnectionManager.getConnection1();
	    CallableStatement callableStatement = null;
        ResultSet rs = null;
        try {
        	callableStatement = conn.prepareCall(CALL_SP_CUST_INFO);
    		callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
    		callableStatement.executeUpdate();
    		rs = (ResultSet) callableStatement.getObject(1);
            while (rs.next()){     
            	UCB1001Row row = new UCB1001Row();
            	row.setCustKeyChar(rs.getString(CUST_KEY_CHAR));
            	row.setCustKeyNum(rs.getString(CUST_KEY_NUM));
            	row.setName(rs.getString(NAME));
            	row.setNric(rs.getString(NRIC));
				row.setDob(rs.getString(DOB));
				row.setZipCode(rs.getString(ZIP_CODE));
				row.setSex(rs.getString(SEX));
				row.sethPhone(rs.getString(H_PHONE));
				row.setoPhone(rs.getString(O_PHONE));
				row.setFax(rs.getString(FAX));
				row.setOcupation(rs.getString(OCCUPATION));
				
                arrayList.add(row);
            }
        } catch (SQLException e) {
        	logger.error("UCB1001 DataBase Connection Error");
			e.printStackTrace();
        }        
        DataSourceUtility.closeResultSet(rs);
        DataSourceUtility.closeStatment(callableStatement);
        DataSourceUtility.closeConnection(conn);
        logger.info(getMethodName(Thread.currentThread().getStackTrace()) + ": end. records:" + arrayList.size() + ".");
        
        return arrayList;
    }
}
