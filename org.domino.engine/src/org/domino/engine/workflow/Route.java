package org.domino.engine.workflow;

public class Route {
	private String id;
	private String routeName;
	private Node startNode;
	private String endNodeId;
	private Process process;

	/**
	 * @param id
	 *            the id to set
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
	 * @param routeName
	 *            the routeName to set
	 */
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	/**
	 * @return the routeName
	 */
	public String getRouteName() {
		return routeName;
	}

	/**
	 * @param startNode
	 *            the startNode to set
	 */
	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	/**
	 * @return the startNode
	 */
	public Node getStartNode() {
		return startNode;
	}


	/**
	 * @return the endNode
	 */
	public Node getEndNode() {
		return getProcess().getNode(endNodeId);
	}

	/**
	 * @param process 要设置的 process
	 */
	public void setProcess(Process process) {
		this.process = process;
	}

	/**
	 * @return process
	 */
	public Process getProcess() {
		return process;
	}
	
	/**
	 * @param endNodeId the endNodeId to set
	 */
	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}

	/**
	 * @return the endNodeId
	 */
	public String getEndNodeId() {
		return endNodeId;
	}

}
