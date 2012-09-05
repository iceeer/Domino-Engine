/**
 * 
 */
package org.domino.engine.workflow;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author iceeer
 * 
 */
public class Node {

	/**
	 * 开始环节|0
	 */
	public static final int START_NODE_TYPE = 0;

	/**
	 * 结束环节|999
	 */
	public static final int END_NODE_TYPE = 9999;

	/**
	 * 人工环节|1
	 */
	public static final int NORMAL_NODE_TYPE = 1;

	private String id = "";
	private String nodeName = "";
	private int nodeType = -1;
	private Process process = null;

	private HashMap<String, Route> routes = new HashMap<String, Route>();
	private ArrayList<Route> routeList = new ArrayList<Route>();

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
	 * @param nodeName
	 *            the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeType
	 *            the nodeType to set
	 */
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * @return the nodeType
	 */
	public int getNodeType() {
		return nodeType;
	}

	/**
	 * @param process
	 *            the process to set
	 */
	public void setProcess(Process process) {
		this.process = process;
	}

	/**
	 * @return the process
	 */
	public Process getProcess() {
		return process;
	}

	/**
	 * @param routes
	 *            要设置的 routes
	 */
	public void setRoutes(HashMap<String, Route> routes) {
		this.routes = routes;
	}

	/**
	 * @return routes
	 */
	public HashMap<String, Route> getRoutes() {
		return routes;
	}
	
	/**
	 * 
	 * @param route
	 */
	public void addRoute(Route route){
		routes.put(route.getId(), route);
		routeList.add(route);
	}
	
	/**
	 * 
	 * @param route
	 */
	public void removeRoute(Route route){
		routes.remove(route);
		routeList.remove(route);
	}

	/**
	 * @param routeList
	 *            要设置的 routeList
	 */
	public void setRouteList(ArrayList<Route> routeList) {
		this.routeList = routeList;
	}

	/**
	 * @return routeList
	 */
	public ArrayList<Route> getRouteList() {
		return routeList;
	}

}
