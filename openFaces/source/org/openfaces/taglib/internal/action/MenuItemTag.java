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
package org.openfaces.taglib.internal.action;


import org.openfaces.component.action.MenuItem;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Vladimir Kurganov
 */
public class MenuItemTag extends AbstractComponentTag {
    public String getComponentType() {
        return MenuItem.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.MenuItemRenderer";
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent uiComponent) {

        super.setComponentProperties(facesContext, uiComponent);

        setObjectProperty(uiComponent, "value");
        MenuItem menuItem = (MenuItem) uiComponent;
        setActionProperty(facesContext, menuItem);
        setActionListener(facesContext, menuItem);

        setStringProperty(uiComponent, "iconUrl");
        setStringProperty(uiComponent, "disabledIconUrl");
        setStringProperty(uiComponent, "selectedIconUrl");
        setStringProperty(uiComponent, "selectedDisabledIconUrl");

        setStringProperty(uiComponent, "accessKey");

        setStringProperty(uiComponent, "submenuImageUrl");
        setStringProperty(uiComponent, "disabledSubmenuImageUrl");
        setStringProperty(uiComponent, "selectedSubmenuImageUrl");

        setBooleanProperty(uiComponent, "disabled");

        setStringProperty(uiComponent, "disabledStyle");
        setStringProperty(uiComponent, "disabledClass");
        setStringProperty(uiComponent, "selectedStyle");
        setStringProperty(uiComponent, "selectedClass");
        setStringProperty(uiComponent, "contentAreaStyle");
        setStringProperty(uiComponent, "contentAreaClass");
        setStringProperty(uiComponent, "indentAreaStyle");
        setStringProperty(uiComponent, "indentAreaClass");
        setStringProperty(uiComponent, "submenuIconAreaStyle");
        setStringProperty(uiComponent, "submenuIconAreaClass");
        setStringProperty(uiComponent, "selectedDisabledStyle");
        setStringProperty(uiComponent, "selectedDisabledClass");

    }
}