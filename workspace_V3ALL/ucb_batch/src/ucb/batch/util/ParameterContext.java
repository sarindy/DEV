package ucb.batch.util;

/**
 * @author LSC
 * @since 2008-07-22
 * 
 */
public interface ParameterContext {
	public void setValue(String name, Object value);

	/**
	 * 取得參數值
	 * 
	 * 
	 * @param name
	 *            參數名稱
	 * @return 參數值
	 */
	public Object getValue(String name);

}
