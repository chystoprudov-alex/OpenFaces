/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.internal.timetable;

import org.openfaces.component.timetable.EventPreview;
import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.VerticalAlignment;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class EventPreviewTag extends AbstractComponentTag {
    public String getComponentType() {
        return EventPreview.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        EventPreview eventPreview = (EventPreview) component;
        setStringProperty(eventPreview, "eventNameStyle");
        setStringProperty(eventPreview, "eventNameClass");
        setStringProperty(eventPreview, "eventDescriptionStyle");
        setStringProperty(eventPreview, "eventDescriptionClass");
        setBooleanProperty(eventPreview, "escapeEventName");
        setBooleanProperty(eventPreview, "escapeEventDescription");

        setEnumerationProperty(eventPreview, "horizontalAlignment", HorizontalAlignment.class);
        setEnumerationProperty(eventPreview, "verticalAlignment", VerticalAlignment.class);

        setStringProperty(eventPreview, "horizontalDistance");
        setStringProperty(eventPreview, "verticalDistance");

        setIntProperty(eventPreview, "showingDelay");
    }
}
