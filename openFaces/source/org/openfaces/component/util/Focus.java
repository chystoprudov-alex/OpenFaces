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
package org.openfaces.component.util;

import org.openfaces.util.ValueBindings;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

/**
 *
 * The Focus component is a non-visual component that controls the focus on the page.
 * With Focus, you can specify the component that is focused when the page is loaded.
 * You can also set that the focus is saved between page submissions.
 * 
 * @author Dmitry Pikhulya
 */
public class Focus extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.Focus";
    public static final String COMPONENT_FAMILY = "org.openfaces.Focus";

    private String focusedComponentId;
    private Boolean autoSaveFocus;

    public Focus() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getFocusedComponentId() {
        return ValueBindings.get(this, "focusedComponentId", focusedComponentId);
    }

    public void setFocusedComponentId(String focusedComponentId) {
        this.focusedComponentId = focusedComponentId;
    }

    public boolean getAutoSaveFocus() {
        return ValueBindings.get(this, "autoSaveFocus", autoSaveFocus, true);
    }

    public void setAutoSaveFocus(boolean autoSaveFocus) {
        this.autoSaveFocus = autoSaveFocus;
    }

    public void encodeBegin(FacesContext context) throws IOException {
        String componentId = ComponentUtil.referenceIdToClientId(context, this, getFocusedComponentId());

        ResponseWriter writer = context.getResponseWriter();

        String clientId = getClientId(context);
        RenderingUtil.renderHiddenField(writer, clientId, componentId);

        RenderingUtil.renderInitScript(context,
                new ScriptBuilder().initScript(context, this, "O$.initFocus", getAutoSaveFocus()).semicolon(),
                new String[]{ResourceUtil.getUtilJsURL(context)}
        );
    }

    public void decode(FacesContext context) {
        super.decode(context);
        if (!getAutoSaveFocus())
            return;
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String focusedComponentId = (String) requestMap.get(getClientId(context));
        this.focusedComponentId = focusedComponentId != null && focusedComponentId.length() > 0 ? ":" + focusedComponentId : null;

    }

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, focusedComponentId, autoSaveFocus};
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        focusedComponentId = (String) stateArray[1];
        autoSaveFocus = (Boolean) stateArray[2];
    }

    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (focusedComponentId != null && ValueBindings.set(this, "focusedComponentId", focusedComponentId))
            focusedComponentId = null;
    }

}