package org.domino.engine.sap;

import java.util.Map;

import junit.framework.TestCase;

import org.domino.engine.Helper;
import org.junit.Assert;

import com.sap.mw.jco.*;

public class SAPConnectionTest extends TestCase {

	/**
	 * 
	 */
	public void setUp() {
	}

	/**
	 * 
	 */
	public void tearDown() {

	}

	public void testSAPConnection_Accuracy1() {
		SAPConnection oSAPConnection = new SAPConnection();
		Assert.assertNotNull(oSAPConnection);
	}

	public void testSAPConnection_Accuracy2() {
		IRepository repository = null;
		JCO.Client client = null;
		IFunctionTemplate ftemplate = null;
		JCO.Function function = null;
		JCO.Table it_tab = null;

		SAPConnection oSAPConnection = new SAPConnection();
		oSAPConnection.setKey("R3");
		oSAPConnection.setHost("10.1.11.54");
		oSAPConnection.setSystemNumber("00");
		oSAPConnection.setClient("400");
		oSAPConnection.setUser("YXRFCUSER");
		oSAPConnection.setPassword("YXERP2010");
		oSAPConnection.setLang("ZH");
		oSAPConnection.setMaxConnection(1);

		oSAPConnection.setFunctionName("ZFUC_OA_TC_CHANGE");
		oSAPConnection.setTableName("ITAB");
		try {
			try {
				JCO.addClientPool(oSAPConnection.getKey(),
						oSAPConnection.getMaxConnection(),
						oSAPConnection.getClient(), oSAPConnection.getUser(),
						oSAPConnection.getPassword(), oSAPConnection.getLang(),
						oSAPConnection.getHost(),
						oSAPConnection.getSystemNumber());//Creating Connection Pool 

				repository = JCO.createRepository("MYRepository",
						oSAPConnection.getKey());//Creating Repository

				ftemplate = repository.getFunctionTemplate(oSAPConnection
						.getFunctionName());//Creating Function Template 
				if (ftemplate != null) {
					function = ftemplate.getFunction();//Creating Function
					client = JCO.getClient(oSAPConnection.getKey());//Getting Connection
					// JCO.ParameterList input =
					// function.getImportParameterList();
					// ParameterList input =
					// function.getTableParameterList();
					it_tab = function.getTableParameterList().getTable(
							oSAPConnection.getTableName());

					if (client != null) {
						client.execute(function);//Executing Function 
						Assert.assertNotNull(client);
					}
				}
			} catch (com.sap.mw.jco.JCO.Exception e) {
				Helper.logError(e);
			} finally {
				JCO.removeClientPool(oSAPConnection.getKey());//Destroying Connection Pool 
			}

		} catch (Exception e) {
			Helper.logError(e);
		} finally {
			// Release the client to the pool
			if (client != null) {
				JCO.releaseClient(client);
			}

		}
	}

	public void testSAPConnection_Accuracy3() {
		IRepository repository = null;
		JCO.Client client = null;
		IFunctionTemplate ftemplate = null;
		JCO.Function function = null;
		JCO.Table it_tab = null;

		SAPConnection oSAPConnection = new SAPConnection();
		oSAPConnection.setKey("R3");
		oSAPConnection.setHost("10.1.11.54");
		oSAPConnection.setSystemNumber("00");
		oSAPConnection.setClient("400");
		oSAPConnection.setUser("YXRFCUSER");
		oSAPConnection.setPassword("YXERP2010");
		oSAPConnection.setLang("ZH");
		oSAPConnection.setMaxConnection(1);

		oSAPConnection.setFunctionName("RFC_READ_TABLE");
		oSAPConnection.setTableName("ITAB");
		try {
			try {
				JCO.addClientPool(oSAPConnection.getKey(),
						oSAPConnection.getMaxConnection(),
						oSAPConnection.getClient(), oSAPConnection.getUser(),
						oSAPConnection.getPassword(), oSAPConnection.getLang(),
						oSAPConnection.getHost(),
						oSAPConnection.getSystemNumber());

				repository = JCO.createRepository("MYRepository",
						oSAPConnection.getKey());

				ftemplate = repository.getFunctionTemplate(oSAPConnection
						.getFunctionName());
				if (ftemplate != null) {
					function = ftemplate.getFunction();
					client = JCO.getClient(oSAPConnection.getKey());
					// Fill in input parameters
					JCO.ParameterList input = function.getImportParameterList();

					//Providing Inputs 
					input.setValue("|", "DELIMITER");//定义输出数据分割符
					input.setValue("ZWFHRPAT001_D", "QUERY_TABLE");//SAP数据表名称
					
					JCO.Structure sOPTIONS =  input.getStructure("OPTIONS"); //筛选条件 where
					sOPTIONS.setValue("FLAG='Y'", "TEXT");//筛选条件，一行一个
					
					JCO.Structure sFIELDS =  input.getStructure("FIELDS");//所需字段集合
					sFIELDS.setValue("SEARK", "FIELDNAME");//所需字段名称

					// Call the remote system
					client.execute(function);

					JCO.Structure sDATA = function.getExportParameterList()
							.getStructure("DATA");//所需输出字段数据
					Assert.assertNotSame(sDATA.getString("WA"), "");//所需字段数据，字段间以’|’分割
					
					System.out.println("Data: "
							+ sDATA.getString("WA"));// Print return message
					
					/*
					// Get table containing the orders
					JCO.Table sales_orders = function.getTableParameterList()
							.getTable("SALES_ORDERS");

					// Print results
					if (sales_orders.getNumRows() > 0) {
						// Loop over all rows
						do {
							System.out.println("-----------------------");
							// Loop over all columns in the current row
							for (JCO.FieldIterator e = sales_orders.fields(); e
									.hasMoreElements();) {
								JCO.Field field = e.nextField();
								System.out.println(field.getName() + ":\t"
										+ field.getString());
							}// for
						} while (sales_orders.nextRow());
					} else {
						System.out.println("No results found");
					}// if
					*/
				} else {
					Helper.logError("Function not found in SAP system."
							+ oSAPConnection.getFunctionName());
				}
			} catch (com.sap.mw.jco.JCO.Exception e) {
				Helper.logError(e);
			} finally {
				JCO.removeClientPool(oSAPConnection.getKey());
			}

		} catch (Exception e) {
			Helper.logError(e);
		} finally {
			// Release the client to the pool
			if (client != null) {
				JCO.releaseClient(client);
			}

		}
	}
}
