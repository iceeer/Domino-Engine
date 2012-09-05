/**
 * 
 */
package org.domino.engine.sap;

/**
 * @author iceeer
 * 
 */
public class SAPConnection{

	/**
	 * Remote Function Calls (RFC) 
	 */
	public static final String RFC = "1";

	/**
	 * SAP Business Application Programming Interface (BAPI)
	 */
	public static final String BAPI = "2";

	/**
	 * SAP transactions
	 */
	public static final String TRANSACTION = "3";

	/**
	 * Specifies a unique name that identifies this connection. The maximum
	 * number of characters allowed is 255.
	 */
	private String key = "";

	/**
	 * Specifies the name of the application server you want to log on. Host
	 * names can be regular host names defined in a ‘hosts’ file, an IP address
	 * like 123.123.123.123 or an SAP router address as
	 * /H/hostname/S/port/H/host/S/port/..
	 */
	private String host = "";

	/**
	 * Specifies the SAP system number used for logon, for example "00".
	 */
	private String sysnum = "";

	/**
	 * Specifies the three-digit client number used for logon, for example "800"
	 * for a standard IDES data set.
	 */
	private String client = "";

	/**
	 * the user name used to log on to SAP.
	 */
	private String user = "";

	/**
	 * the Password associated with the above SAP User Name.
	 */
	private String password = "";

	/**
	 * UTF-16 Support ,On selecting this option, this SAP connection will use
	 * SAP unicode (UTF16) connector instead of non unicode connector for its
	 * operations.
	 */
	private boolean enableUTF16 = false;

	/**
	 * Specifies the native language to be used when logging on to SAP R/3. E =
	 * English for SAP R/3 version 3.n or earlier
	 * 
	 * EN = English for SAP R/3 version 6.n or earlier
	 * 
	 * SAP R/3 Release 4x and later uses a two-letter language code. Previous
	 * versions use a one-letter language code.
	 */
	private String lang = "";

	/**
	 * NUMC as Text ,Forces NUMC data type in SAP to arrive as text. When this
	 * is enabled, you cannot map NUMC columns in SAP to Numeric columns in
	 * other connectors. When this is enabled, you may only send text to NUMC
	 * fields in SAP. This includes the Select Statement used in the LEI Direct
	 * Transfer activity, the Constitutional Clause of the LEI Replication
	 * activity and the Select Statement of the LCConnection Execute method of
	 * the LSXLC.
	 */
	private boolean NUMCasText = false;

	/**
	 * DATS as Text,Forces DATS (date) data type in SAP to arrive as text. When
	 * this is enabled, you cannot map DATS columns in SAP to Datetime columns
	 * in other connectors. When this is enabled, you may only send text to DATS
	 * fields in SAP. This includes the Select Statement used in the LEI Direct
	 * Transfer activity, the Constitutional Clause of the LEI Replication
	 * activity and the Select Statement of the LCConnection Execute method of
	 * the LSXLC.
	 */
	private boolean DATSasText = false;

	/**
	 * TIMS as Text,Forces TIMS (time) data type in SAP to arrive as text. When
	 * this is enabled, you cannot map TIMS columns in SAP to Datetime columns
	 * in other connectors. When this is enabled, you may only send text to TIMS
	 * fields in SAP. This includes the Select Statement used in the LEI Direct
	 * Transfer activity, the Constitutional Clause of the LEI Replication
	 * activity and the Select Statement of the LCConnection Execute method of
	 * the LSXLC.
	 */
	private boolean TIMSasText = false;

	/**
	 * connect type
	 */
	private String connection_type = RFC;

	// RFC or BAPI use
	/**
	 * the name of the RFC or BAPI that you want to call, for
	 * example,BAPI_CUSTOMER_GETDETAIL2 .
	 */
	private String function_name = "";

	/**
	 * available tables in the RFC or BAPI
	 */
	private String table_name = "";

	// Transaction use
	/**
	 * the transaction code you want to call using batch mode processing, for
	 * example ME21 or XDO2.
	 */
	private String transaction_code = "";

	/**
	 * the screens and their fields needed to call this transaction. Use this
	 * only when an R/3 transaction is the target. These define the transaction
	 * screens.
	 * 
	 * The field definitions for each Screen used is defined by the program name
	 * with a leading ‘$’ and the Dynpro number as a value, this is followed by
	 * the input field names. If a field is assigned a value, the field is
	 * assumed to be constant. Blank lines are ignored.
	 * 
	 * For example, for XD02, the list would be as follows:
	 * 
	 * $SAPMF20D = 0101
	 * 
	 * RF02D-KUNNR
	 * 
	 * RF02D-D0110=X
	 * 
	 * BCD_OKCODE=/00, and so on.
	 * 
	 * This is only a partial list.
	 */
	private String screen_field = "";

	/**
	 * Max number of connnections
	 */
	private int max_connection = 1;

	/**
	 * Debug level, 0 mean not debug
	 */
	private int debug_level = 0;

	/**
	 * 
	 */
	public SAPConnection() {
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param sysnum the sysnum to set
	 */
	public void setSystemNumber(String sysnum) {
		this.sysnum = sysnum;
	}

	/**
	 * @return the sysnum
	 */
	public String getSystemNumber() {
		return sysnum;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param enableUTF16 the enableUTF16 to set
	 */
	public void setEnableUTF16(boolean enableUTF16) {
		this.enableUTF16 = enableUTF16;
	}

	/**
	 * @return the enableUTF16
	 */
	public boolean isEnableUTF16() {
		return enableUTF16;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param nUMCasText the nUMCasText to set
	 */
	public void setNUMCasText(boolean nUMCasText) {
		NUMCasText = nUMCasText;
	}

	/**
	 * @return the nUMCasText
	 */
	public boolean isNUMCasText() {
		return NUMCasText;
	}

	/**
	 * @param dATSasText the dATSasText to set
	 */
	public void setDATSasText(boolean dATSasText) {
		DATSasText = dATSasText;
	}

	/**
	 * @return the dATSasText
	 */
	public boolean isDATSasText() {
		return DATSasText;
	}

	/**
	 * @param tIMSasText the tIMSasText to set
	 */
	public void setTIMSasText(boolean tIMSasText) {
		TIMSasText = tIMSasText;
	}

	/**
	 * @return the tIMSasText
	 */
	public boolean isTIMSasText() {
		return TIMSasText;
	}

	/**
	 * @param connection_type the connection_type to set
	 */
	public void setConnectionType(String connection_type) {
		this.connection_type = connection_type;
	}

	/**
	 * @return the connection_type
	 */
	public String getConnectionType() {
		return connection_type;
	}

	/**
	 * @param function_name the function_name to set
	 */
	public void setFunctionName(String function_name) {
		this.function_name = function_name;
	}

	/**
	 * @return the function_name
	 */
	public String getFunctionName() {
		return function_name;
	}

	/**
	 * @param table_name the table_name to set
	 */
	public void setTableName(String table_name) {
		this.table_name = table_name;
	}

	/**
	 * @return the table_name
	 */
	public String getTableName() {
		return table_name;
	}

	/**
	 * @param transaction_code the transaction_code to set
	 */
	public void setTransactionCode(String transaction_code) {
		this.transaction_code = transaction_code;
	}

	/**
	 * @return the transaction_code
	 */
	public String getTransactionCode() {
		return transaction_code;
	}

	/**
	 * @param screen_field the screen_field to set
	 */
	public void setScreenField(String screen_field) {
		this.screen_field = screen_field;
	}

	/**
	 * @return the screen_field
	 */
	public String getScreenField() {
		return screen_field;
	}

	/**
	 * @param max_connection the max_connection to set
	 */
	public void setMaxConnection(int max_connection) {
		this.max_connection = max_connection;
	}

	/**
	 * @return the max_connection
	 */
	public int getMaxConnection() {
		return max_connection;
	}

	/**
	 * @param debug_level the debug_level to set
	 */
	public void setDebugLevel(int debug_level) {
		this.debug_level = debug_level;
	}

	/**
	 * @return the debug_level
	 */
	public int getDebugLevel() {
		return debug_level;
	}

}
