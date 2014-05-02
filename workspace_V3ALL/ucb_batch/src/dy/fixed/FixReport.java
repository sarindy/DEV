package dy.fixed;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;
import ucb.batch.util.dataSource.ConnectionManager;
import ucb.batch.util.dataSource.DataSourceUtility;

public class FixReport {
	private static final String NAME = "NAME";
	private static final String DESCRIPTION = "DESCRIPTION";
	
	
	private static final String CALL_SP_FID = "{call GETACCTCODEBYACCTID(?,?)}";

	public static void main(String[] args) {
		Connection conn = ConnectionManager.getConnection1();
	    CallableStatement callableStatement = null;
        ResultSet rs = null;
        try {
        	callableStatement = conn.prepareCall(CALL_SP_FID);
        	callableStatement.setInt(1, 268);
    		callableStatement.registerOutParameter(2,  OracleTypes.CURSOR);
    		callableStatement.executeUpdate();
    		rs = (ResultSet) callableStatement.getObject(2);
            while (rs.next()){     

            	System.out.println(rs.getString(NAME));
            	System.out.println(rs.getString(DESCRIPTION));
            	
				
               
            }
        } catch (SQLException e) {
        	
			e.printStackTrace();
        }        
        DataSourceUtility.closeResultSet(rs);
        DataSourceUtility.closeStatment(callableStatement);
        DataSourceUtility.closeConnection(conn);
        
	}

}
