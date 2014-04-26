package dy.fixed;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;
import ucb.batch.util.dataSource.ConnectionManager;
import ucb.batch.util.dataSource.DataSourceUtility;

public class FixReport {
	private static final String FD_ID = "FD_ID";
	private static final String CUSTACCTID = "CUSTACCTID";
	private static final String INT_RATE = "INT_RATE";
	private static final String APPLICATION_DATE = "APPLICATION_DATE";
	private static final String PRINCIPAL = "PRINCIPAL";
	
	private static final String CALL_SP_FID = "{call SP_FIX(?,?)}";

	public static void main(String[] args) {
		Connection conn = ConnectionManager.getConnection1();
	    CallableStatement callableStatement = null;
        ResultSet rs = null;
        try {
        	callableStatement = conn.prepareCall(CALL_SP_FID);
        	callableStatement.setInt(1, 702);
    		callableStatement.registerOutParameter(1,  OracleTypes.CURSOR);
    		callableStatement.executeUpdate();
    		rs = (ResultSet) callableStatement.getObject(1);
            while (rs.next()){     

            	System.out.println(rs.getString(FD_ID));
            	System.out.println(rs.getString(CUSTACCTID));
            	System.out.println(rs.getString(INT_RATE));
            	System.out.println(rs.getString(APPLICATION_DATE));
            	System.out.println(rs.getString(PRINCIPAL));
				
               
            }
        } catch (SQLException e) {
        	
			e.printStackTrace();
        }        
        DataSourceUtility.closeResultSet(rs);
        DataSourceUtility.closeStatment(callableStatement);
        DataSourceUtility.closeConnection(conn);
        
	}

}
