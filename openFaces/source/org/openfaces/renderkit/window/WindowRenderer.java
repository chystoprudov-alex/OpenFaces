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
package org.openfaces.renderkit.window;

import org.openfaces.component.window.AbstractWindow;
import org.openfaces.component.window.Window;
import org.openfaces.renderkit.ComponentWithCaptionRenderer;
import org.openfaces.util.RenderingUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class WindowRenderer extends AbstractWindowRenderer {
    protected void encodeContentPane(FacesContext context, AbstractWindow abstractWindow) throws IOException {
        Window win = (Window) abstractWindow;
        String clientId = win.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", win);
        writer.writeAttribute("id", clientId + MIDDLE_AREA_SUFFIX, null);
        RenderingUtil.writeStyleAndClassAttributes(writer, win.getContentStyle(), win.getContentClass(), getDefaultContentClass());
        ComponentWithCaptionRenderer.renderChildren(context, abstractWindow);
        writer.endElement("div");
    }

    protected String getDefaultContentClass() {
        return "o_window_content";
    }


    protected String getDefaultTableClass() {
        return "o_window_table";
    }

}