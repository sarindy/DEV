/*
 * Created on Aug 28, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ucb.batch.support.row;

/**
 * @author UCH
 *
 * Branch Data
 */
public class BranchRow {
	private String branchId;
	private String name;
	
	public BranchRow(String branchId, String name) {
		this.branchId = branchId;
		this.name = name;
	}
	/**
	 * @return Returns the branchId.
	 */
	public String getBranchId() {
		return branchId;
	}
	/**
	 * @param branchId The branchId to set.
	 */
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		char left = '[';
		char right = ']';
		StringBuffer sb = new StringBuffer();
		sb.append(left).append(getBranchId()).append(right);
		sb.append(left).append(getName()).append(right);
		return sb.toString();
	}
}
