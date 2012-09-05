package kahrl.rwf;
import lotus.domino.*;
import java.util.*;
/** Description: 
 *  This class is used to track field change events in context document for use by the Subscription class.
 *  @see class Subscription
 *
 * @author:  Phillip Kahrl
**/

public class DocRevision {

private Vector items=new Vector(0);
private static String SUFFIX = "_OLD";

/** 
	* Empty default constructor for DocRevision.
**/
public DocRevision(){
	
	}
/**
Writes current values of fields of interest to new fields with the suffix added "_OLD" so that they can be stored in order to determine
if fields have changed in the document next time it is saved.
@param  d - The context document.
**/
public static void writeLastRevision(Document d)throws RWFException {
	try{
		Vector fields = Subscription.getSubscriptionFieldsByForm(d.getItemValueString("Form"), d.getParentDatabase());
		for(Enumeration e=fields.elements(); e.hasMoreElements(); ){
			String fn = (String)e.nextElement();
			if(d.hasItem(fn)){
				d.replaceItemValue(fn + SUFFIX, d.getItemValue(fn));
			}
			}
	}catch(NotesException e){e.printStackTrace();}
	}
/**
Assigns a new string value to a named DocRevisionItem
@param name, the name of the DocRevisonItem, if no item with this name exists one will be created.
@param val, the value to assign to the DocRevisionItem.
**/
public void replaceItemValue(String name, String val){
	if(hasItem(name)){
		DocRevisionItem dri = getFirstItem(name);
		dri.setText(val);
		}
	else{
		items.addElement(new DocRevisionItem(name, val));
		}
	}
/**
Determines if this DocRevision has a DocRevision item of a given name.
@param itemName -  The name of the DocRevision item to look for.
@return boolean true if the DocRevision contains a DocRevisionItem of the passed name, false otherwise.
**/
public boolean hasItem(String itemName){
	boolean tmp = false;
	LOOP:for(Enumeration e=items.elements(); e.hasMoreElements(); ){
		DocRevisionItem dri = (DocRevisionItem)e.nextElement();
		if(dri.getName().equalsIgnoreCase(itemName)){
			tmp = true;
			break LOOP;
			}
		}
		return tmp;
	}

/** Determines which fields of interest have changed in a context document since the last time it was saved.
@return A Vector of strings of field names for fields that have changed value since the last save.
@param itemNames a Vector of strings, comprising a list of fields to search to see if they have been changed.
@param doc The context document.
**/
public static Vector itemsChanged(Vector itemNames, Document doc) throws RWFException{
	Vector tmp = new Vector(0);
	DocRevision previousVersion = DocRevision.getLastRevision(doc);
	for(Enumeration e=itemNames.elements(); e.hasMoreElements(); ){
		try{
			String itemName = (String)e.nextElement();
			if(doc.hasItem(itemName)){
				Item presentItem = doc.getFirstItem(itemName);
				String presentVal = presentItem.getText();
				String previousVal = previousVersion.getItemValueString(itemName);
				/// Empty fields are evaluated as null
				/// Avoid the null pointer exception
				if(presentVal == null){presentVal = "";}
				if(previousVal == null){previousVal = "";}
				///
				if(!(presentVal.equals(previousVal))){
					tmp.addElement(itemName);}
			}			
			}catch(NotesException ex){
				ex.printStackTrace();
				return tmp;
				}
		}
	return tmp;
	}
/**
	This will return a DocRevision object by looking for fieldnames with the _OLD suffix in the current document
	fields with the suffix are set each time the document is saved by writeLastRevision()
**/

public static DocRevision getLastRevision(Document d){
	
	DocRevision tmp = new DocRevision();
	try{
		for(Enumeration e=d.getItems().elements(); e.hasMoreElements(); ){
			Item item = (Item)e.nextElement();
			String itemName = item.getName();
			if((itemName.toUpperCase().indexOf(SUFFIX) != -1) && (itemName.substring(itemName.length()-4, itemName.length()).equalsIgnoreCase(SUFFIX))){
				String itemText = item.getText();
				if(item.getText() == null){itemText="";}
				String prefix = itemName.substring(0, itemName.length() - 4);
				tmp.replaceItemValue(prefix, itemText);
			}
		}
	}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}
/**
Returns the string value of a named DocRevision Item from the current Doc Revision.
@ return The string value of the named item, if the DocRevision doesn't contain the item a null is returned.
@ param  itemName, String name of the item.
**/
public String getItemValueString(String itemName){
	String tmp = null;
	for(Enumeration e = items.elements(); e.hasMoreElements() ; ){
		DocRevisionItem dri= (DocRevisionItem)e.nextElement() ;
		if(dri.getName().equalsIgnoreCase(itemName)){
			tmp = dri.getText();
			}
		}
	if(tmp == null){
	
		//throw new RWFException("RevisionDoc item not found for item name:" + itemName); 
		}
	return tmp;
	}
/**
Returns all of the DocRevison items for this DocRevision
@ return A Vector of all DocRevision items for this DocRevision
**/
public Vector getItems(){return items;}
/**
Returns the first  DocRevisionItem object found with the given name in this DocRevision
@ return A DocRevisonItem of the given name, null if the this DocRevision object does not already contain an item with the given name.
**/
public DocRevisionItem getFirstItem(String itemName){
	DocRevisionItem tmp = null;
	LOOP:for(Enumeration e = items.elements(); e.hasMoreElements() ; ){
		DocRevisionItem dri= (DocRevisionItem)e.nextElement();
		if(dri.getName().equalsIgnoreCase(itemName)){
			tmp = dri;
			break LOOP;
			}
	}
return tmp;
}

}
