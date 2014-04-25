/*
 * Created on Jul 22, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ucb.batch.util.expressions;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ucb.batch.util.ParameterContext;
//import com.esb.fx.batch.util.SequenceExpression;

/**
 * @author LSC
 *
 */
public class HashtableParameterContext implements ParameterContext {
    
    @SuppressWarnings("unchecked")
    private Map context = new Hashtable();
    /* (non-Javadoc)
     * @see com.esb.fx.batch.util.ParameterContext#setValue(java.lang.String, java.lang.Object)
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.esb.common.util.ExpressionContext#setValue(java.lang.String,
     *      java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void setValue(String name, Object value) {
        context.put(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.esb.common.util.ExpressionContext#getValue(java.lang.String)
     */
    public Object getValue(String name) {
        return context.get(name);
    }
    
    @SuppressWarnings("unchecked")
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	if (context != null) {
    		Set set = context.entrySet();
    		Iterator i = set.iterator();
    		while (i.hasNext()) {
    			Map.Entry entry = (Map.Entry) i.next();
    			sb.append('[').append(entry.getKey()).append(']').append('=').append('[');
    			sb.append(entry.getValue()).append(']').append(';');
    		}
    	}
    	return sb.toString();
    }

}
