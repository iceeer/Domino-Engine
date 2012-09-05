/**
 * 
 */
package org.domino.engine.workflow;

import lotus.domino.Document;

import org.domino.engine.Helper;

/**
 * @author admin
 *
 */
public class NodeActivity {
	
	private String activityId="";
	
	private String operator="";
	
	private Node node = null;
	
	private String documentId = "";

	
	public NodeActivity(){
		activityId = Helper.buildUniqueID();
	}
	
	public NodeActivity(String activityId,String operator){
		this.activityId = activityId;
		this.operator = operator;
	}
	
	/**
	 * @param operator 要设置的 operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return operator
	 */
	public String getOperator() {
		return operator;
	}

	
	public Node getNode(){
		return node;
	}

	/**
	 * @param activityId 要设置的 activityId
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return activityId
	 */
	public String getActivityId() {
		return activityId;
	}

	/**
	 * @param node 要设置的 node
	 */
	public void setNode(Node node) {
		this.node = node;
	}
	
	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	/**
	 * @return the documentId
	 */
	public String getDocumentId() {
		return documentId;
	}
	
}
