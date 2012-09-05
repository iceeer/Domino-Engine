package org.domino.engine.labelbox;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class LabelBoxRenderer extends Renderer {

	@Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("style", "border:blue solid thin", null);
        writer.writeText("This is an Box.", null);
        writer.startElement("hr", null);
        writer.endElement("hr");
    }
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }
}
