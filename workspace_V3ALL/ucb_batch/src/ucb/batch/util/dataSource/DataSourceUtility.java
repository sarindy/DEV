package ucb.batch.util.dataSource;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author LSC
 * @since 2010-09-25 
 * DatabaseUtility class helps to close database resources. 
 * 1. Close ResultSet
 * 2. Close Statement
 * 3. Close Connection
 */
public class DataSourceUtility {
    
    private static final Logger logger = Logger.getLogger(DataSourceUtility.class);    
    
    /**
     * Close ResultSet
     * @param ResultSet rs
     * @return
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            logger.error("An error occurred closing ResultSet.", ex);
        }
    }
    
    /**
     * Close Statement
     * @param Statement st
     * @return
     */
    public static void closeStatment(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ex) {
            logger.error("An error occurred closing Statement.", ex);
        }
    }

    /**
     * Close Connection
     * @param Connection conn
     * @return
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            logger.error("An error occurred closing connection.", ex);
        }
    }
}
