package ucb.batch;

import org.apache.log4j.Logger;

/**
 * @author LSC
 * @since 2010-09-25 This is an abstract base class for EFS batch project.
 *        Modify it as you wish.
 */
public abstract class AbstractBatch {

    protected Logger logger = null;
    
    protected AbstractBatch() {
        logger = Logger.getLogger(this.getClass().getName());
        logger.info(this.getClass().toString() + ": start.");
    }

    protected String getMethodName(StackTraceElement ste[]) {
        String methodName = "";
        boolean flag = false;
        for (StackTraceElement s : ste) {
            if (flag) {
                methodName = s.getMethodName();
                break;
            }
            flag = s.getMethodName().equals("getStackTrace");
        }
        return this.getClass().toString() + "." + methodName;
    }
}
