package ucb.batch.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author LSC
 * @since 2010-09-25 Configuration class gets properties from a resource file.
 *        Modify it as you wish.
 */
public class Configuration {

    private static final Logger logger = Logger.getLogger(Configuration.class);

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
    private String operationCenter = null;
    private String branchListFilePath = null;
    private String countryCode = null;
    
    public Configuration(String configFileName) {
        SAXBuilder builder = new SAXBuilder();
        InputStream is = this.getClass().getResourceAsStream(configFileName);
        Document doc;
        try {
            doc = builder.build(is);
            Element root = doc.getRootElement();
            /* Database properties */
            db1DriverName = root.getChild("DatabaseConfig1").getChildTextTrim("dbDriverName");
            db1User = root.getChild("DatabaseConfig1").getChildTextTrim("dbUser");
            db1Password = root.getChild("DatabaseConfig1").getChildTextTrim("dbPassword");
            db1URI = root.getChild("DatabaseConfig1").getChildTextTrim("dbURI");
            db2DriverName = root.getChild("DatabaseConfig2").getChildTextTrim("dbDriverName");
            db2User = root.getChild("DatabaseConfig2").getChildTextTrim("dbUser");
            db2Password = root.getChild("DatabaseConfig2").getChildTextTrim("dbPassword");
            db2URI = root.getChild("DatabaseConfig2").getChildTextTrim("dbURI");
            db3DriverName = root.getChild("DatabaseConfig3").getChildTextTrim("dbDriverName");
            db3User = root.getChild("DatabaseConfig3").getChildTextTrim("dbUser");
            db3Password = root.getChild("DatabaseConfig3").getChildTextTrim("dbPassword");
            db3URI = root.getChild("DatabaseConfig3").getChildTextTrim("dbURI");
            db4DriverName = root.getChild("DatabaseConfig4").getChildTextTrim("dbDriverName");
            db4User = root.getChild("DatabaseConfig4").getChildTextTrim("dbUser");
            db4Password = root.getChild("DatabaseConfig4").getChildTextTrim("dbPassword");
            db4URI = root.getChild("DatabaseConfig4").getChildTextTrim("dbURI");
            db5DriverName = root.getChild("DatabaseConfig5").getChildTextTrim("dbDriverName");
            db5User = root.getChild("DatabaseConfig5").getChildTextTrim("dbUser");
            db5Password = root.getChild("DatabaseConfig5").getChildTextTrim("dbPassword");
            db5URI = root.getChild("DatabaseConfig5").getChildTextTrim("dbURI");
            db6DriverName = root.getChild("DatabaseConfig6").getChildTextTrim("dbDriverName");
            db6User = root.getChild("DatabaseConfig6").getChildTextTrim("dbUser");
            db6Password = root.getChild("DatabaseConfig6").getChildTextTrim("dbPassword");
            db6URI = root.getChild("DatabaseConfig6").getChildTextTrim("dbURI");
            /* Operation Center properties */
            operationCenter = root.getChild("OperationCenterConfig").getChildTextTrim("operationCenter");
            /* Branch List File path properties */
            branchListFilePath = root.getChild("BranchListFileConfig").getChildTextTrim("BranchListFilePath");
            /* Country Code properties */
            countryCode = root.getChild("CountryCodeConfig").getChildTextTrim("countryCode");
        } catch (JDOMException eJDom) {
            logger.error(this, eJDom);
        } catch (IOException e) {
            logger.error(this, e);
        }

    }

    public String toString() {
        ReflectionToStringBuilder tsb = new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        return tsb.toString();
    }
    
    public String getDb1DriverName() {return db1DriverName;}
    public String getDb1User() {return db1User;}
    public String getDb1Password() {return db1Password;}
    public String getDb1URI() {return db1URI;}
    public String getDb2DriverName() {return db2DriverName;}
    public String getDb2User() {return db2User;}
    public String getDb2Password() {return db2Password;}
    public String getDb2URI() {return db2URI;}
    public String getDb3DriverName() {return db3DriverName;}
    public String getDb3User() {return db3User;}
    public String getDb3Password() {return db3Password;}
    public String getDb3URI() {return db3URI;}
    public String getDb4DriverName() {return db4DriverName;}
    public String getDb4User() {return db4User;}
    public String getDb4Password() {return db4Password;}
    public String getDb4URI() {return db4URI;}
    public String getDb5DriverName() {return db5DriverName;}
    public String getDb5User() {return db5User;}
    public String getDb5Password() {return db5Password;}
    public String getDb5URI() {return db5URI;}
    public String getDb6DriverName() {return db6DriverName;}
    public String getDb6User() {return db6User;}
    public String getDb6Password() {return db6Password;}
    public String getDb6URI() {return db6URI;}
    public String getOperationCenter() {return operationCenter;}
    public String getBranchListFilePath() {return branchListFilePath;}
    public String getCountryCode() {return countryCode;}
    
}
