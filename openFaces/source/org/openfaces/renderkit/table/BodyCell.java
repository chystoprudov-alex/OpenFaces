/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.table;

import org.openfaces.util.RenderingUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class BodyCell extends TableElement {
    private Object content;
    private List customCells;
    private int span;
    private String style;
    private String styleClass;

    public BodyCell() {
    }

    public BodyCell(TableElement parent) {
        super(parent);
    }

    public void render(FacesContext context, HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("td", null);
        if (span > 1)
            writer.writeAttribute("colspan", String.valueOf(span), null);
        if (style != null || styleClass != null)
            RenderingUtil.writeStyleAndClassAttributes(writer, style, styleClass);
        if (customCells != null)
            BodyRow.writeCustomRowOrCellEvents(writer, customCells);

        if (content != null) {
            if (content instanceof String)
                writer.write(content.toString());
            else if (content instanceof TableElement)
                ((TableElement) content).render(context, null);
            else
                throw new IllegalStateException("Unsupported type of 'content' property: " + content.getClass().getName());
        }
        
        if (additionalContentWriter != null)
            additionalContentWriter.writeAdditionalContent(context);
        writer.endElement("td");
    }

    public void setCustomCells(List customCells) {
        this.customCells = customCells;
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}