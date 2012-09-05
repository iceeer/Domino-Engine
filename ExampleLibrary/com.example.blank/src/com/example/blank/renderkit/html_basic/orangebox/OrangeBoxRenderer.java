package com.example.blank.renderkit.html_basic.orangebox;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class OrangeBoxRenderer extends Renderer {

	@Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("style", "border:orange solid thin", null);
        writer.writeText("This is an Orange Box.", null);
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
