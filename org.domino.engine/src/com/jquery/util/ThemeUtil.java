/*
 * © Copyright IBM Corp. 2010
 * © Copyright Declan Sciolla-Lynch 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.jquery.util;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.Version;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.stylekit.StyleKitImpl;
import com.ibm.xsp.stylekit.ThemeControl;



/**
 * Some utility used to deal with themes.
 */
public class ThemeUtil {
    
    
    public static final Version ONEUI_NONE  = new Version(0, 0);    // Not OneUI
    public static final Version ONEUI_V1    = new Version(1, 0);    // OneUI v1 - old, should not be used anymore
    public static final Version ONEUI_V2    = new Version(2, 0);    // OneUI v2
    public static final Version ONEUI_V21   = new Version(2, 1);    // OneUI v2.1 (8.5.3 and above)
    
    // This is temporary for running the app on an 853 server without the 853 specific plug-in
    public static boolean useTheme852 = true;

    private static Map<String,String> OneUIRendererTypes;
    public static String getDefaultRendererType(ThemeControl c, String defaultRenderer) {
        if(useTheme852/*ExtLibUtil.isXPages852()*/) {
            // 852: we assume OneUI
            if (!isOneUITheme(FacesContextEx.getCurrentInstance())){
                // or we could actually check to see if it's OneUI
                return defaultRenderer;
            }
            if(OneUIRendererTypes==null) {
                synchronized(ThemeUtil.class) {
                    if(OneUIRendererTypes==null) {
                        OneUIRendererTypes=buildOneUIMap();
                    }
                }
            }
            String rt = OneUIRendererTypes.get(c.getStyleKitFamily());
            if(StringUtil.isEmpty(rt)) {
                return defaultRenderer;
            }
            return rt;
        } else {
            // Post 852: we assume that the renderer type is forced by a theme
            // So we just set the default renderer here
            return defaultRenderer;
        }
    }

    public static Version getOneUIVersion(FacesContext context) {
        FacesContextEx ctxEx = (FacesContextEx)context;
        Version v = (Version)ctxEx.getAttributes().get("extlib.oneui.Version"); // $NON-NLS-1$
        if(v!=null) {
            return v;
        }
        v = findOneUIVersion(ctxEx);
        ctxEx.getAttributes().put("extlib.oneui.Version", v); // $NON-NLS-1$
        return v;
    }
    private static Version findOneUIVersion(FacesContextEx ctxEx) {
        for(StyleKitImpl st = (StyleKitImpl)ctxEx.getStyleKit(); st!=null; st=st.getParent()) {
            if(st.getName().startsWith("oneui")) { // $NON-NLS-1$
                String oneui = st.getName();
                if(oneui.equals("oneui")) { // $NON-NLS-1$
                    return ONEUI_V1;
                }
                if(oneui.equals("oneuiv2") || oneui.startsWith("oneuiv2_")) { // $NON-NLS-1$ $NON-NLS-2$
                    return ONEUI_V2;
                }
                if(oneui.equals("oneuiv2.1") || oneui.startsWith("oneuiv2.1_")) { // $NON-NLS-1$ $NON-NLS-2$
                    return ONEUI_V21;
                }
            }
        }
        // OneUI is not enabled
        return ONEUI_NONE;
    }

    public static boolean isOneUITheme(FacesContextEx context) {
        return getOneUIVersion(context)!=ONEUI_NONE;
    }
    
    public static boolean isOneUIVersion(FacesContext context, int major, int minor) {
        Version v = getOneUIVersion((FacesContextEx)context);
        if(v.getMajor()==major && v.getMinor()==minor) {
            return true;
        }
        return false;
    }
    
    public static boolean isOneUIVersionAtLeast(FacesContext context, int major, int minor) {
        Version v = getOneUIVersion((FacesContextEx)context);
        if(v.getMajor()>major) {
            return true;
        }
        if(v.getMajor()==major) {
            if(v.getMinor()>=minor) {
                return true;
            }
        }
        return false;
    }
    
    
    private static Map<String,String> buildOneUIMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("ApplicationLayout","com.ibm.xsp.extlib.OneUIApplicationLayout"); // $NON-NLS-1$ $NON-NLS-2$
        
        map.put("Outline.Navigator","com.ibm.xsp.extlib.OneUIOutlineMenu"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Outline.BreadCrumbs","com.ibm.xsp.extlib.OneUIBreadCrumbs"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Outline.SortLinks","com.ibm.xsp.extlib.OneUISortLinks"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Outline.LinksList","com.ibm.xsp.extlib.OneUILinksList"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Outline.Separator","com.ibm.xsp.extlib.OneUISeparator"); // $NON-NLS-1$ $NON-NLS-2$
        
        map.put("Container.Widget","com.ibm.xsp.extlib.OneUIWidgetContainer"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Container.List.Inline","com.ibm.xsp.extlib.OneUIInlineList"); // $NON-NLS-1$ $NON-NLS-2$
        
        map.put("Pager.Sizes","com.ibm.xsp.extlib.data.OneUIPagerSizes"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Pager.Expand","com.ibm.xsp.extlib.data.OneUIPagerExpand"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Pager.Detail","com.ibm.xsp.extlib.data.OneUIPagerDetail"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Pager.AddRows","com.ibm.xsp.extlib.data.OneUIPagerAddRows"); // $NON-NLS-1$ $NON-NLS-2$
        
        map.put("DataIterator.DataView","com.ibm.xsp.extlib.data.OneUICustomView"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("DataIterator.ForumView","com.ibm.xsp.extlib.data.OneUIForumView"); // $NON-NLS-1$ $NON-NLS-2$
        
        map.put("FormLayout.FormTable","com.ibm.xsp.extlib.data.OneUIFormTable"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("FormLayout.ForumPost","com.ibm.xsp.extlib.data.OneUIForumPost"); // $NON-NLS-1$ $NON-NLS-2$
        
        map.put("Dialog.Modal","com.ibm.xsp.extlib.OneUIDialog"); // $NON-NLS-1$ $NON-NLS-2$
        //map.put("Dialog.Tooltip","com.ibm.xsp.extlib.data.OneUIForumPost");
        map.put("DialogBar","com.ibm.xsp.extlib.OneUIDialogButtonBar"); // $NON-NLS-1$ $NON-NLS-2$
        
        map.put("Picker.Name","com.ibm.xsp.extlib.OneUINamePicker"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Picker.Value","com.ibm.xsp.extlib.OneUIValuePicker"); // $NON-NLS-1$ $NON-NLS-2$
        
        map.put("Dojo.Form.ListTextBox","com.ibm.xsp.extlib.dojoext.form.OneUIListTextBox"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Dojo.Form.NameTextBox","com.ibm.xsp.extlib.dojoext.form.OneUINameTextBox"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Dojo.Form.LinkSelect","com.ibm.xsp.extlib.dojoext.form.OneUILinkSelect"); // $NON-NLS-1$ $NON-NLS-2$
        map.put("Dojo.Form.ImageSelect","com.ibm.xsp.extlib.dojoext.form.OneUIImageSelect"); // $NON-NLS-1$ $NON-NLS-2$

        return map;
    }
    
/*  
    public static String _getOneUIRendererType(UIComponent component) {
        FacesContextEx context = FacesContextEx.getCurrentInstance();
        // Check if the current theme is OneUI
        if(componentType!=null) {
            // OneUI based themes
            if(isOneUITheme(context)) {
                // Application layout
                if(componentType.equals(UIApplicationLayout.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.OneUIApplicationLayout"; 
                }
                // Outline
                if(componentType.equals(UIOutlineNavigator.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.OneUIOutlineMenu"; 
                }
                if(componentType.equals(UIOutlineBreadCrumbs.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.OneUIBreadCrumbs"; 
                }
                if(componentType.equals(UIOutlineSortLinks.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.OneUISortLinks"; 
                }
                if(componentType.equals(UIOutlineLinksList.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.OneUILinksList"; 
                }
                if(componentType.equals(UISeparator.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.OneUISeparator"; 
                }
                // Container
                if(componentType.equals(UIWidgetContainer.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.OneUIWidgetContainer"; 
                }
                if(componentType.equals(UIList.COMPONENT_TYPE)) {
                    return UIList.RENDERER_TYPE; // Nothing particular for OneUI 
                }
                if(componentType.equals(UIListInline.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.OneUIInlineList"; 
                }
                // Pagers
                if(componentType.equals(UIPagerSizes.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.data.OneUIPagerSizes"; 
                }
                if(componentType.equals(UIPagerExpand.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.data.OneUIPagerExpand"; 
                }
                if(componentType.equals(UIPagerDetail.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.data.OneUIPagerDetail"; 
                }
                if(componentType.equals(UIPagerAddRows.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.data.OneUIPagerAddRows"; 
                }
                // Views
                if(componentType.equals(UIDataView.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.data.OneUICustomView"; 
                }
                if(componentType.equals(UIForumView.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.data.OneUIForumView"; 
                }
                // Form
                if(componentType.equals(UIFormTable.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.data.OneUIFormTable"; 
                }
                if(componentType.equals(UIForumPost.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.data.OneUIForumPost"; 
                }
                // Tag cloud
                if(componentType.equals(UITagCloud.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.TagCloud"; 
                }
                // Extended Dojo controls
                if(componentType.equals(UIDojoExtListTextBox.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.dojoext.form.OneUIListTextBox"; 
                }
                if(componentType.equals(UIDojoExtNameTextBox.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.dojoext.form.OneUINameTextBox"; 
                }
                if(componentType.equals(UIDojoExtLinkSelect.COMPONENT_TYPE)) {
                    return "com.ibm.xsp.extlib.dojoext.form.OneUILinkSelect"; 
                }
            }
            
            // Default themes
            if(componentType.equals(UIApplicationLayout.COMPONENT_TYPE)) {
                return "com.ibm.xsp.extlib.OneUIApplicationLayout"; 
            }
            if(componentType.equals(UIOutlineNavigator.COMPONENT_TYPE)) {
                return "com.ibm.xsp.extlib.OneUIOutlineMenu"; 
            }
            if(componentType.equals(UIOutlineBreadCrumbs.COMPONENT_TYPE)) {
                return "com.ibm.xsp.extlib.OneUIBreadCrumbs"; 
            }
            if(componentType.equals(UIWidgetContainer.COMPONENT_TYPE)) {
                return "com.ibm.xsp.extlib.WidgetContainer"; 
            }
            if(componentType.equals(UISeparator.COMPONENT_TYPE)) {
                return "com.ibm.xsp.extlib.Separator"; 
            }
            if(componentType.equals(UIDojoExtListTextBox.COMPONENT_TYPE)) {
                return "com.ibm.xsp.extlib.dojoext.form.ListTextBox"; 
            }
            if(componentType.equals(UIDojoExtNameTextBox.COMPONENT_TYPE)) {
                return "com.ibm.xsp.extlib.dojoext.form.NameTextBox"; 
            }
            if(componentType.equals(UIDojoExtLinkSelect.COMPONENT_TYPE)) {
                return "com.ibm.xsp.extlib.dojoext.form.LinkSelect"; 
            }
        }
        return null;
    }
*/
}