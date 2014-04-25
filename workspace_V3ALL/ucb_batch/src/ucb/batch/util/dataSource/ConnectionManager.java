package ucb.batch.util.dataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ucb.batch.util.Configuration;

/**
 * @author LSC
 * @since 2010-09-25 
 * ConnectionManager class gets a connection from designated database. 
 * Modify it as you wish.
 */
public class ConnectionManager {
    
    private static final Logger logger = Logger.getLogger(ConnectionManager.class);
    private static final String CONFIG_FILENAME = "/batch-config.xml";
    private Configuration config = null;
    private String db1DriverName = null;
    private String db1User = null;
    private String db1Password = null;
    private String db1URI = null;
    private String db2DriverName = null;
    private String db2User = null;
    private String db2Password = null;
    private String db2URI = null;
    private String db3DriverName = null;
    private String db3User = null;
    private String db3Password = null;
    private String db3URI = null;
    private String db4DriverName = null;
    private String db4User = null;
    private String db4Password = null;
    private String db4URI = null;
    private String db5DriverName = null;
    private String db5User = null;
    private String db5Password = null;
    private String db5URI = null;
    private String db6DriverName = null;
    private String db6User = null;
    private String db6Password = null;
    private String db6URI = null;
    
    /* Constructor: Connectino Manager */
    public ConnectionManager(){
        config = new Configuration(CONFIG_FILENAME);        
        db1DriverName = config.getDb1DriverName();
        db1User = config.getDb1User();
        db1Password = config.getDb1Password();
        db1URI = config.getDb1URI();
        
        db2DriverName = config.getDb2DriverName();
        db2User = config.getDb2User();
        db2Password = config.getDb2Password();
        db2URI = config.getDb2URI();
        
        db3DriverName = config.getDb3DriverName();
        db3User = config.getDb3User();
        db3Password = config.getDb3Password();
        db3URI = config.getDb3URI();
        
        db4DriverName = config.getDb4DriverName();
        db4User = config.getDb4User();
        db4Password = config.getDb4Password();
        db4URI = config.getDb4URI();
        
        db5DriverName = config.getDb5DriverName();
        db5User = config.getDb5User();
        db5Password = config.getDb5Password();
        db5URI = config.getDb5URI();
        
        db6DriverName = config.getDb6DriverName();
        db6User = config.getDb6User();
        db6Password = config.getDb6Password();
        db6URI = config.getDb6URI();
    }
    
    private Connection getDataSource1Connection(){
        try{
        	Class.forName(config.getDb1DriverName());
            try{
                Connection conn = DriverManager.getConnection(config.getDb1URI(), config.getDb1User(), config.getDb1Password());
                if (conn != null){
                    return conn;   
                }
            }catch(SQLException sqlEx){
                logger.error(this, sqlEx);
            }
        }catch(ClassNotFoundException cnfEx){
            logger.error(this, cnfEx);
        }
        return null;
    }

    private Connection getDataSource2Connection(){
        try{
            Class.forName(config.getDb2DriverName());
            try{
                Connection conn = DriverManager.getConnection(config.getDb2URI(), config.getDb2User(), config.getDb2Password());
                if (conn != null){
                    return conn;   
                }
            }catch(SQLException sqlEx){
                logger.error(this, sqlEx);
            }
        }catch(ClassNotFoundException cnfEx){
            logger.error(this, cnfEx);
        }
        return null;
    }
    
    private Connection getDataSource3Connection(){
        try{
        	Class.forName(config.getDb3DriverName());
            try{
                Connection conn = DriverManager.getConnection(config.getDb3URI(), config.getDb3User(), config.getDb3Password());
                if (conn != null){
                    return conn;   
                }
            }catch(SQLException sqlEx){
                logger.error(this, sqlEx);
            }
        }catch(ClassNotFoundException cnfEx){
            logger.error(this, cnfEx);
        }
        return null;
    }
    
    private Connection getDataSource4Connection(){
        try{
        	Class.forName(config.getDb4DriverName());
            try{
                Connection conn = DriverManager.getConnection(config.getDb4URI(), config.getDb4User(), config.getDb4Password());
                if (conn != null){
                    return conn;   
                }
            }catch(SQLException sqlEx){
                logger.error(this, sqlEx);
            }
        }catch(ClassNotFoundException cnfEx){
            logger.error(this, cnfEx);
        }
        return null;
    }
    
    private Connection getDataSource5Connection(){
        try{
        	Class.forName(config.getDb5DriverName());
            try{
                Connection conn = DriverManager.getConnection(config.getDb5URI(), config.getDb5User(), config.getDb5Password());
                if (conn != null){
                    return conn;   
                }
            }catch(SQLException sqlEx){
                logger.error(this, sqlEx);
            }
        }catch(ClassNotFoundException cnfEx){
            logger.error(this, cnfEx);
        }
        return null;
    }
    
    private Connection getDataSource6Connection(){
        try{
        	Class.forName(config.getDb6DriverName());
            try{
                Connection conn = DriverManager.getConnection(config.getDb6URI(), config.getDb6User(), config.getDb6Password());
                if (conn != null){
                    return conn;   
                }
            }catch(SQLException sqlEx){
                logger.error(this, sqlEx);
            }
        }catch(ClassNotFoundException cnfEx){
            logger.error(this, cnfEx);
        }
        return null;
    }
    
    public static Connection getConnection1(){
        ConnectionManager connectionManager = new ConnectionManager();
        return connectionManager.getDataSource1Connection();
    }
    
    public static Connection getConnection2(){
        ConnectionManager connectionManager = new ConnectionManager();
        return connectionManager.getDataSource2Connection();
    }
    
    public static Connection getConnection3(){
        ConnectionManager connectionManager = new ConnectionManager();
        return connectionManager.getDataSource3Connection();
    }
    
    public static Connection getConnection4(){
        ConnectionManager connectionManager = new ConnectionManager();
        return connectionManager.getDataSource4Connection();
    }
    
    public static Connection getConnection5(){
        ConnectionManager connectionManager = new ConnectionManager();
        return connectionManager.getDataSource5Connection();
    }
    
    public static Connection getConnection6(){
        ConnectionManager connectionManager = new ConnectionManager();
        return connectionManager.getDataSource6Connection();
    }
    
    /**
     * @return the db1DriverName
     */
    public String getDb1DriverName() { return db1DriverName; }

    /**
     * @return the db1User
     */
    public String getDb1User() { return db1User; }

    /**
     * @return the db1Password
     */
    public String getDb1Password() { return db1Password; }

    /**
     * @return the db1URI
     */
    public String getDb1URI() { return db1URI; }
    
    /**
     * @return the db2DriverName
     */
    public String getDb2DriverName() { return db2DriverName; }

    /**
     * @return the db2User
     */
    public String getDb2User() { return db2User; }

    /**
     * @return the db2Password
     */
    public String getDb2Password() { return db2Password; }

    /**
     * @return the db2URI
     */
    public String getDb2URI() { return db2URI; }

    /**
     * @return the db3DriverName
     */
    public String getDb3DriverName() { return db3DriverName; }

    /**
     * @return the db3User
     */
    public String getDb3User() { return db3User; }

    /**
     * @return the db3Password
     */
    public String getDb3Password() { return db3Password; }

    /**
     * @return the db3URI
     */
    public String getDb3URI() { return db3URI; }
    
    /**
     * @return the db4DriverName
     */
    public String getDb4DriverName() { return db4DriverName; }

    /**
     * @return the db4User
     */
    public String getDb4User() { return db4User; }

    /**
     * @return the db4Password
     */
    public String getDb4Password() { return db4Password; }

    /**
     * @return the db4URI
     */
    public String getDb4URI() { return db4URI; }
    
    /**
     * @return the db5DriverName
     */
    public String getDb5DriverName() { return db5DriverName; }

    /**
     * @return the db5User
     */
    public String getDb5User() { return db5User; }

    /**
     * @return the db5Password
     */
    public String getDb5Password() { return db5Password; }

    /**
     * @return the db5URI
     */
    public String getDb5URI() { return db5URI; }
    
    /**
     * @return the db6DriverName
     */
    public String getDb6DriverName() { return db6DriverName; }

    /**
     * @return the db6User
     */
    public String getDb6User() { return db6User; }

    /**
     * @return the db6Password
     */
    public String getDb6Password() { return db6Password; }

    /**
     * @return the db6URI
     */
    public String getDb6URI() { return db6URI; }
    
}
