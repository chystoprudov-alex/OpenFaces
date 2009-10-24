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

package org.openfaces.taglib.internal.input;

import org.openfaces.taglib.internal.AbstractUIInputTag;
import org.openfaces.component.input.SelectOneRadio;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 3:38:05 PM
 */
public class SelectOneRadioTag  extends AbstractUIInputTag {
    private static final String RENDERER_TYPE = "org.openfaces.SelectOneRadioRenderer";

    public String getComponentType() {
        return SelectOneRadio.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent uiComponent) {
        super.setComponentProperties(facesContext, uiComponent);
        setStringProperty(uiComponent, "accesskey");
        setStringProperty(uiComponent, "tabindex");
        setStringProperty(uiComponent, "title");
        setStringProperty(uiComponent, "dir");
        setStringProperty(uiComponent, "lang");
        setStringProperty(uiComponent, "onselect");
        setBooleanProperty(uiComponent, "disabled");

        setBooleanProperty(uiComponent, "readonly");
        setStringProperty(uiComponent, "border");
        setStringProperty(uiComponent, "layout");

        setStringProperty(uiComponent, "enabledStyle");
        setStringProperty(uiComponent, "enabledClass");
        setStringProperty(uiComponent, "disabledStyle");
        setStringProperty(uiComponent, "disabledClass");
        setStringProperty(uiComponent, "selectedStyle");
        setStringProperty(uiComponent, "selectedClass");
        setStringProperty(uiComponent, "unselectedStyle");
        setStringProperty(uiComponent, "unselectedClass");
        
        setStringProperty(uiComponent, "selectedImageUrl");
        setStringProperty(uiComponent, "unselectedImageUrl");
        setStringProperty(uiComponent, "rolloverSelectedImageUrl");
        setStringProperty(uiComponent, "rolloverUnselectedImageUrl");
        setStringProperty(uiComponent, "pressedSelectedImageUrl");
        setStringProperty(uiComponent, "pressedUnselectedImageUrl");
        setStringProperty(uiComponent, "disabledSelectedImageUrl");
        setStringProperty(uiComponent, "disabledUnselectedImageUrl");
    }

}