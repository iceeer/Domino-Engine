package kahrl.rwf;
/** Description: 
 *  This class is used by the DocRevision class.
 *  @see class DocRevision
 *
 * @author:  Phillip Kahrl
**/

public class DocRevisionItem {

private String name;
private String text;
/**
DocRevisionItem constructor
@param n - The name of this DocRevisionItem
@param v - The text value of this DocRevisionItem.
**/
public DocRevisionItem(String n, String v){
	name = n;
	text = v;
	}

public String getName(){return name;}
/**  returns the name of this DocRevisionItem
@return  String containing the name of this DocRevisionItem.
**/
public String getText(){return text; }
/**  returns the text value of this DocRevisionItem
@return  String containing the text value of this DocRevisionItem.
**/

/**  Sets the text value of this DocRevisionItem
@param val - String containing the new text value for this DocRevisionItem.
**/
public void setText(String val){text = val;}



}