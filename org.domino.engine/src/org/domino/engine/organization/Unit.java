/**
 * 
 */
package org.domino.engine.organization;

import java.util.ArrayList;
import java.util.List;

import org.domino.engine.Application;
import org.domino.engine.Engine;

import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

/**
 * @author Administrator
 *
 */
public class Unit{

	/**
	 * 
	 */
	private String strUnitID = null;

	/**
	 * 
	 */
	private Document docUnit = null;

	/**
	 * 
	 */
	public Unit() {
	}

	/**
	 * 
	 */
	public Unit(Document docUnit) {
		this.docUnit = docUnit;
	}

	/**
	 * 
	 */
	public Unit(String strUnitID) {
		this.strUnitID = strUnitID;
	}

	/**
	 * 获得机构下所有人员（包括子孙机构下人员）
	 * @return
	 * @throws NotesException 
	 */
	public List getAllUnitUser() throws NotesException{
		
		List plist = new ArrayList();
		try{
		List list = new ArrayList();
		list = getAllDescendantUnit();
		for(int i = 0 ; i < list.size() ; i++){
			Unit unitParent = (Unit) list.get(i);
			View view = Application.getApplicationDB().getView("vwAllUnitByUnitIDForBuildHierarchy");
			DocumentCollection dc = view.getAllDocumentsByKey(unitParent.getDocUnit().getItemValueString("UnitID"),true);
			Document doc = dc.getFirstDocument();
			while(doc!=null){
				Person personParent = Person.getPersonByDocument(doc);
				
				//System.out.print(doc.getItemValueString("PersonID"));
				plist.add(personParent);
				doc = dc.getNextDocument(doc);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return plist;
	}

	/**
	 * 获得本机构下人员（不包括再下一级机构下的人员）
	 * @return
	 * @throws NotesException 
	 */
	public List getUnitUser() throws NotesException{
		
		List plist = new ArrayList();
		View view = Application.getApplicationDB().getView("vwAllPersonUnitByUnitIDForChoose");
		DocumentCollection dc = view.getAllDocumentsByKey(strUnitID,true);
		Document doc = dc.getFirstDocument();
		while(doc!=null){
			Person personParent = Person.getPersonByDocument(doc);
			
			plist.add(personParent);
			doc = dc.getNextDocument(doc);
		}
		return plist;
	}

	/**
	 * 获得机构下所有子机构
	 * @return
	 * @throws NotesException 
	 */
	public List<Unit> getAllChildUnit() throws NotesException{
		List<Unit> list = new ArrayList<Unit>();
		DocumentCollection dc = Application.getApplicationDB()
				.getView("vwAllUnitByUnitIDForBuildHierarchy").getDocumentByKey(strUnitID,true).getResponses();
		Document doc = dc.getFirstDocument();
		while(doc!=null){
			Unit unitParent = new Unit();
			unitParent.setDocUnit(doc);
			unitParent.setStrUnitID(doc.getItemValueString("UnitID"));
			list.add(unitParent);
			doc = dc.getNextDocument(doc);			
		}
		return list;
	}

	/**
	 * 获得机构下所有子孙机构
	 * @return
	 * @throws NotesException 
	 */
	public List<Unit> getAllDescendantUnit() throws NotesException{
		List<Unit> list = new ArrayList<Unit>();
		Unit unitParent = new Unit();
		Document doc = Application.getApplicationDB()
				.getView("vwAllUnitByUnitIDForBuildHierarchy").getDocumentByKey(strUnitID,true);
		unitParent.setDocUnit(doc);
		unitParent.setStrUnitID(doc.getItemValueString("UnitID"));
		return getCount(list,unitParent);
	}

	public List<Unit> getCount(List<Unit> listAll,Unit unit) throws NotesException{
		List<Unit> list = new ArrayList<Unit>();
		list = unit.getAllChildUnit();
		listAll.addAll(list);
		for(int i = 0 ; i < list.size() ; i++){
			Unit unitParent = (Unit) list.get(i);
			if(unitParent.getDocUnit().getResponses().getCount()>0){				
				getCount(listAll,unitParent);
			}
		}
		return listAll;
	}

	/**
	 * 获得父机构
	 * @return
	 * @throws NotesException 
	 */
	public Unit getParentUnit() throws NotesException{
		Unit unitParent = new Unit();
		View view = Application.getApplicationDB().getView("vwAllUnitByNoAndParUnitIDForChoose");
		Document doc = view.getDocumentByKey(strUnitID,true);
		if(!"".equals(doc.getItemValueString("ParentUnitID"))){
			unitParent.setDocUnit(view.getDocumentByKey(doc.getItemValueString("ParentUnitID"),true));			
		}
		return unitParent;
	}

	/**
	 * 获得最高机构
	 * @return
	 * @throws NotesException 
	 */
	public Unit getRootUnit() throws NotesException{
		Unit unitParent = new Unit();
		View view = Application.getApplicationDB().getView("vwAllUnitByNoAndParUnitIDForChoose");
		Document doc = view.getDocumentByKey(strUnitID,true);
		unitParent.setDocUnit(doc);
		while(!"".equals(unitParent.getDocUnit().getItemValueString("ParentUnitID"))){
			unitParent = getParentUnit();
		}
		return unitParent;
	}

	/**
	 * 
	 * @return
	 */
	public String getStrUnitID() {
		return strUnitID;
	}

	/**
	 * 
	 * @param strUnitID
	 */
	public void setStrUnitID(String strUnitID) {
		this.strUnitID = strUnitID;
	}

	/**
	 * 
	 * @return
	 */
	public Document getDocUnit() {
		return docUnit;
	}

	/**
	 * 
	 * @param docUnit
	 */
	public void setDocUnit(Document docUnit) {
		this.docUnit = docUnit;
	}


}
