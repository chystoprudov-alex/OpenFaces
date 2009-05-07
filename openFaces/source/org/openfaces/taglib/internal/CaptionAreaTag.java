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
package org.openfaces.taglib.internal;

import org.openfaces.component.CaptionArea;
import org.openfaces.component.HorizontalAlignment;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * @author Dmitry Pikhulya
 */
public class CaptionAreaTag extends AbstractComponentTag {
    public String getComponentType() {
        return CaptionArea.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.CaptionAreaRenderer";
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setEnumerationProperty(component, "alignment", HorizontalAlignment.class);
    }
}