package org.domino.engine.menu;

import java.io.IOException;
import java.util.Properties;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.domino.engine.Helper;
import org.domino.engine.foundation.PropertyLoader;


public class MenuRender extends Renderer {

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		try {
			MenuBuilder oMenuBuilder = new MenuBuilder();
			writer.write(oMenuBuilder.getHTML());
		} catch (Exception e) {
			Helper.logError(e);
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		//ResponseWriter writer = context.getResponseWriter();
	}
}