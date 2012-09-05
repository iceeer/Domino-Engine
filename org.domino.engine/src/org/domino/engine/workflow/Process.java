/**
 * 
 */
package org.domino.engine.workflow;

import java.util.HashMap;

/**
 * 流程定义
 * @author admin
 *
 */
public class Process {

	/**
	 * 
	 */
	public Process() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean doBeforeProcessStart(){
		return true;
	}
	
	public boolean doAfterProcessStart(){
		return true;
	}
	
	public boolean doBeforeProcessEnd(){
		return true;
	}
	
	public boolean doAfterProcessEnd(){
		return true;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @param firstNode the firstNode to set
	 */
	public void setFirstNode(Node firstNode) {
		this.firstNode = firstNode;
	}

	/**
	 * @return the firstNode
	 */
	public Node getFirstNode() {
		return firstNode;
	}

	/**
	 * @param nodes 要设置的 nodes
	 */
	public void setNodes(HashMap<String,Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return nodes
	 */
	public HashMap<String,Node> getNodes() {
		return nodes;
	}
	
	public Node getNode(String nodeId){
		return nodes.get(nodeId);
	}
	
	/**
	 * 
	 * @param node
	 */
	public void addNode(Node node){
		this.nodes.put(node.getId(), node);
	}
	
	/**
	 * 
	 * @param node
	 */
	public void removeNode(Node node){
		this.nodes.remove(node);
	}
	
	/**
	 * 
	 * @param id
	 */
	public void removeNode(String id){
		this.nodes.remove(id);
	}

	/**
	 * @param endNode 要设置的 endNode
	 */
	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	/**
	 * @return endNode
	 */
	public Node getEndNode() {
		return endNode;
	}

	/**
	 * 标识
	 */
	private String id = "";
	
	/**
	 * 流程名
	 */
	private String processName = "";
	
	/**
	 * 开始节点
	 */
	private Node firstNode = null;
	
	/**
	 * 结束节点
	 */
	private Node endNode = null;
	
	/**
	 * 
	 */
	private HashMap<String,Node> nodes = new HashMap<String,Node>();
	

}
