package com.gbs.labs.xsp.debug;

import com.ibm.xsp.component.UIIncludeComposite;
import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.component.UIInclude;
import com.ibm.xsp.component.UIPanelEx;
import com.ibm.xsp.component.UIPassThroughTag;
import com.ibm.xsp.component.UISelectItemEx;
import com.ibm.xsp.component.UISelectItemsEx;
import com.ibm.xsp.model.DataContextImpl;
import com.ibm.xsp.model.domino.DominoDocumentData;
import com.ibm.xsp.model.domino.DominoViewData;
import com.ibm.xsp.complex.Parameter;
import com.ibm.xsp.acl.*;
import com.ibm.xsp.actions.*;
import com.ibm.xsp.actions.client.*;
import com.ibm.xsp.application.NavigationRule;
import com.ibm.xsp.event.*;
import com.ibm.xsp.converter.*;
import com.ibm.xsp.convert.*;
import com.ibm.xsp.resource.*;
import com.ibm.xsp.validator.*;
import com.ibm.xsp.component.xp.*;

public enum EXspClassTagMap {
	// TODO: We might someday like to replace this with a proper registry
	// IBM says there's a bundle in one of the XSP packages, but hasn't told us where it is yet.

	INCLUDECOMPOSITE(UIIncludeComposite.class,
			"xc:custom"),
	ACL(ACL.class,
			"xp:acl"),
	ACL_ENTRY(ACLEntry.class,
			"xp:aclEntry"),

	// Start server side actions
	ACTION_CREATERESPONSE(CreateResponseAction.class,
			"xp:createResponse"),
	ACTION_CHANGEDOCUMENTMODE(ChangeDocumentModeAction.class,
			"xp:changeDocumentMode"),
	ACTION_CONFIRM(ConfirmAction.class,
			"xp:confirm"),
	ACTION_DELETEDOCUMENT(DeleteDocumentAction.class,
			"xp:deleteDocument"),
	ACTION_DELETESELECTEDDOCUMENTS(DeleteSelectedDocumentsAction.class,
			"xp:deleteSelectedDocuments"),
	ACTION_EXECUTESCRIPT(ExecuteScriptAction.class,
			"xp:executeScript"),
	ACTION_MODIFYFIELD(ModifyFieldAction.class,
			"xp:modifyField"),
	ACTION_OPENPAGE(OpenPageAction.class,
			"xp:openPage"),
	ACTION_SAVE(SaveAction.class,
			"xp:save"),
	ACTION_SAVEDOCUMENT(SaveDocumentAction.class,
			"xp:saveDocument"),
	ACTION_SETCOMPONENTMODE(ComponentSetModeAction.class,
			"xp:setComponentMode"),
	ACTION_SETVALUE(SetValueAction.class,
			"xp:setValue"),
	// End server side actions

	// Start client actions
	ACTION_EXECUTESCRIPTCLIENT(ExecuteScriptClientAction.class,
			"xp:executeClientScript"),
	ACTION_PUBLISHCOMPONENTPROPERTY(ComponentPublishPropertyAction.class,
			"xp:publishValue"),
	ACTION_PUBLISHVIEWCOLUMN(ComponentPublishViewColumnAction.class,
			"xp:publishViewColumn"),
	// End client actions

	// AJAXSERVICE(XspAjaxService.class, "xp:"),
	BUTTON(XspCommandButton.class,
			"xp:button"),
	CALLBACK(XspCallback.class,
			"xp:callback"),
	CHECKBOX(XspInputCheckbox.class,
			"xp:checkBox"),
	CHECKBOXGROUP(XspSelectManyCheckbox.class,
			"xp:checkBoxGroup"),
	COLUMN(XspColumn.class,
			"xp:column"),
	COMBOBOX(XspSelectOneMenu.class,
			"xp:comboBox"),

	// Start converters
	CONVERT_CUSTOM(ConverterMethodBinding.class,
			"xp:customConverter"),
	CONVERT_DATETIME(DateTimeConverter.class,
			"xp:convertDateTime"),
	CONVERT_LIST(ListConverter.class,
			"xp:convertList"),
	CONVERT_MASK(MaskConverter.class,
			"xp:convertMask"),
	CONVERT_NUMBER(NumberConverter.class,
			"xp:convertNumber"),
	// End converters

	DATACONTEXT(DataContextImpl.class,
			"xp:dataContext"),

	DATATABLE(XspDataTableEx.class,
			"xp:dataTable"),
	DATETIMEHELPER(XspDateTimeHelper.class,
			"xp:dateTimeHelper"),
	DIV(XspDiv.class,
			"xp:div"),
	DOCUMENTDATA(DominoDocumentData.class,
			"xp:dominoDocument"),
	EDITBOX(XspInputText.class,
			"xp:inputText"),
	EVENTHANDLER(XspEventHandler.class,
			"xp:eventHandler"),
	FILEDOWNLOAD(XspFileDownload.class,
			"xp:fileDownload"),
	FILEUPLOAD(XspFileUpload.class,
			"xp:fileUpload"),
	FORM(XspForm.class,
			"xp:form"),
	HIDDENINPUT(XspInputHidden.class,
			"xp:inputHidden"),
	IMAGE(XspGraphicImage.class,
			"xp:image"),
	INCLUDEPAGE(UIInclude.class,
			"xp:include"),
	LABEL(XspOutputLabel.class,
			"xp:label"),
	LINEBREAK(XspLineBreak.class,
			"xp:br"),
	LINK(XspOutputLink.class,
			"xp:link"),
	LISTBOXMANY(XspSelectManyListbox.class,
			"xp:listBox"),
	LISTBOXONE(XspSelectOneListbox.class,
			"xp:listBox"),
	MESSAGE(XspMessage.class,
			"xp:message"),
	MESSAGES(XspMessages.class,
			"xp:messages"),
	MULTILINEEDITBOX(XspInputTextarea.class,
			"xp:inputTextarea"),

	NAVIGATIONRULE(NavigationRule.class,
			"xp:navigationRule"),

	OUTPUTSCRIPT(XspOutputScript.class,
			"xp:scriptBlock"),
	PAGER(XspPager.class,
			"xp:pager"),
	PAGERCONTROL(XspPagerControl.class,
			"xp:pagerControl"),
	PANEL(UIPanelEx.class,
			"xp:panel"),
	PARAGRAPH(XspParagraph.class,
			"xp:paragraph"),

	PARAMETER(Parameter.class,
			"xp:parameter"),

	PASSTHRU(UIPassThroughTag.class,
			"xp:br"),
	PLATFORMEVENT(XspPlatformEvent.class,
			"xp:platformEvent"),
	RADIOBUTTON(XspSelectOneRadio.class,
			"xp:radio"),
	RADIOBUTTONGROUP(XspInputRadio.class,
			"xp:radioGroup"),
	REPEAT(XspDataIterator.class,
			"xp:repeat"),

	// Start resources
	RESOURCE_BUNDLE(BundleResource.class,
			"xp:bundle"),
	RESOURCE_DOJOMODULE(DojoModuleResource.class,
			"xp:dojoModule"),
	RESOURCE_LINK(LinkResource.class,
			"xp:linkResource"),
	RESOURCE_METADATA(MetaDataResource.class,
			"xp:metaData"),
	RESOURCE_SCRIPT(ScriptResource.class,
			"xp:script"),
	RESOURCE_STYLESHEET(StyleSheetResource.class,
			"xp:styleSheet"),
	// End resources

	RICHTEXT(XspInputRichText.class,
			"xp:inputRichText"),
	SECTION(XspSection.class,
			"xp:section"),
	SELECTITEM(UISelectItemEx.class,
			"xp:selectItem"),
	SELECTITEMS(UISelectItemsEx.class,
			"xp:selectItems"),
	SPAN(XspSpan.class,
			"xp:span"),
	TABBEDPANEL(XspTabbedPanel.class,
			"xp:tabbedPanel"),
	TABPANEL(XspTabPanel.class,
			"xp:tabPanel"),
	TABLE(XspTable.class,
			"xp:table"),
	TABLECELL(XspTableCell.class,
			"xp:td"),
	TABLEROW(XspTableRow.class,
			"xp:tr"),
	TEXT(XspOutputText.class,
			"xp:text"),
	TYPEAHEAD(XspTypeAhead.class,
			"xp:typeAhead"),

	// Start validates
	VALIDATE_CONSTRAINT(ConstraintValidator.class,
			"xp:validateConstraint"),
	VALIDATE_DATETIMERANGE(DateTimeRangeValidator.class,
			"xp:validateDateTimeRange"),
	VALIDATE_DOUBLERANGE(DoubleRangeValidatorEx2.class,
			"xp:validateDoubleRange"),
	VALIDATE_EXPRESSION(ExpressionValidator.class,
			"xp:validateExpression"),
	VALIDATE_LENGTH(LengthValidatorEx.class,
			"xp:validateLength"),
	VALIDATE_LONGRANGE(LongRangeValidatorEx2.class,
			"xp:validateLongRange"),
	VALIDATE_MODULUSSELFCHECK(ModulusSelfCheckValidator.class,
			"xp:validateModulusSelfCheck"),
	VALIDATE_REQUIRED(RequiredValidator.class,
			"xp:validateRequired"),
	VALIDATOR(ValidatorImpl.class,
			"xp:validator"),
	// End validates

	VALUECHANGELISTENERS(ValueChangeListenerImpl.class,
			"xp:valueChangeListener"),
	VIEWCOLUMN(XspViewColumn.class,
			"xp:viewColumn"),
	VIEWCOLUMNHEADER(XspViewColumnHeader.class,
			"xp:viewColumnHeader"),
	VIEWDATA(DominoViewData.class,
			"xp:dominoView"),
	VIEWPANEL(XspViewPanel.class,
			"xp:viewPanel"),
	VIEWROOT(UIViewRootEx2.class,
			"xp:view"),
	VIEWTITLE(XspViewTitle.class,
			"viewTitle");

	private String	xpTag;
	private Class	XspClass;

	private EXspClassTagMap (Class XspClass, String tag) {
		this.xpTag = tag;
		this.XspClass = XspClass;
	}

	public Class<?> getXspClass () {
		return this.XspClass;
	}

	public String getTag () {
		return this.xpTag;
	}

	public String getTag (Object parmObj) {
		if (this.getXspClass().isInstance(parmObj)) {
			if (parmObj instanceof UIPassThroughTag) {
				return ((UIPassThroughTag) parmObj).getTag();
			} else {
				return this.getTag();
			}
		} else {
			return null;
		}
	}

}
