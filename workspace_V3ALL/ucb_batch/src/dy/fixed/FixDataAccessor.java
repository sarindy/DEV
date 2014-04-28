package dy.fixed;

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

public class FixDataAccessor extends AbstractBatch {
	private static final String FD_ID = "FD_ID";
	private static final String CUSTACCTID = "CUSTACCTID";
	private static final String INT_RATE = "INT_RATE";
	private static final String APPLICATION_DATE = "APPLICATION_DATE";
	private static final String PRINCIPAL = "PRINCIPAL";
	
	private static final String CALL_SP_FID = "{call SP_FIX(?,?)}";
	
	public List<FixRow> getReportQuery(ParameterContext context){
		List<FixRow> arrayList = new ArrayList<FixRow>(); 
	    Connection conn = ConnectionManager.getConnection1();
	    CallableStatement callableStatement = null;
        ResultSet rs = null;
        try {
        	callableStatement = conn.prepareCall(CALL_SP_FID);
        	callableStatement.setString(1, "");
    		callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
    		callableStatement.executeUpdate();
    		rs = (ResultSet) callableStatement.getObject(1);
            while (rs.next()){     
            	FixRow row = new FixRow();
            	row.setFdId(rs.getString(FD_ID));
            	row.setCustAcctID(rs.getString(CUSTACCTID));
            	row.setIntRate(rs.getString(INT_RATE));
            	row.setApplicationDate(rs.getString(APPLICATION_DATE));
            	row.setPrincipal(rs.getString(PRINCIPAL));
            	
				
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

