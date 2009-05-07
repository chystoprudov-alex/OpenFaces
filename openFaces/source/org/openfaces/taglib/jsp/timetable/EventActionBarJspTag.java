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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.EventActionBarTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

public class EventActionBarJspTag extends AbstractComponentJspTag {
    public EventActionBarJspTag() {
        super(new EventActionBarTag());
    }

    public void setNoteText(ValueExpression noteText) {
        getDelegate().setPropertyValue("noteText", noteText);
    }

    public void setBackgroudnIntensityLevel(ValueExpression backgroudnIntensityLevel) {
        getDelegate().setPropertyValue("backgroundIntensityLevel", backgroudnIntensityLevel);
    }

    public void setActionRolloverBackgroundIntensity(ValueExpression value) {
        getDelegate().setPropertyValue("actionRolloverBackgroundIntensity", value);
    }

    public void setActionPressedBackgroundIntensity(ValueExpression value) {
        getDelegate().setPropertyValue("actionPressedBackgroundIntensity", value);
    }

}