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
package org.openfaces.component.panel;

import org.openfaces.component.CaptionArea;
import org.openfaces.component.CompoundComponent;
import org.openfaces.component.ExpansionToggleButton;
import org.openfaces.component.LoadingMode;
import org.openfaces.util.ValueBindings;
import org.openfaces.event.StateChangeListener;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RenderingUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * The FoldingPanel component is a container for other components that can be expanded
 * and collapsed by the user. The component supports different folding directions to reveal
 * its content and provides various options to customize its appearance. Plus, there are
 * several ways to control the content loading (using Ajax).
 * 
 * @author Kharchenko
 */
public class FoldingPanel extends AbstractPanelWithCaption implements CompoundComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.FoldingPanel";
    public static final String COMPONENT_FAMILY = "org.openfaces.FoldingPanel";

    private LoadingMode loadingMode;
    private Boolean expanded;

    private String onstatechange;

    private FoldingDirection foldingDirection;

    private Boolean focusable;
    private String focusedClass;
    private String focusedStyle;

    private String focusedCaptionStyle;
    private String focusedCaptionClass;
    private String focusedContentStyle;
    private String focusedContentClass;

    public FoldingPanel() {
        setRendererType("org.openfaces.FoldingPanelRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean isFocusable() {
        return ValueBindings.get(this, "focusable", focusable, true);
    }

    public String getFocusedStyle() {
        return ValueBindings.get(this, "focusedStyle", focusedStyle);
    }

    public void setFocusedStyle(String focusedStyle) {
        this.focusedStyle = focusedStyle;
    }

    public String getFocusedClass() {
        return ValueBindings.get(this, "focusedClass", focusedClass);
    }

    public void setFocusedClass(String focusedClass) {
        this.focusedClass = focusedClass;
    }


    public String getFocusedCaptionStyle() {
        return ValueBindings.get(this, "focusedCaptionStyle", focusedCaptionStyle);
    }

    public void setFocusedCaptionStyle(String focusedCaptionStyle) {
        this.focusedCaptionStyle = focusedCaptionStyle;
    }

    public String getFocusedCaptionClass() {
        return ValueBindings.get(this, "focusedCaptionClass", focusedCaptionClass);
    }

    public void setFocusedCaptionClass(String focusedCaptionClass) {
        this.focusedCaptionClass = focusedCaptionClass;
    }

    public String getFocusedContentStyle() {
        return ValueBindings.get(this, "focusedContentStyle", focusedContentStyle);
    }

    public void setFocusedContentStyle(String focusedContentStyle) {
        this.focusedContentStyle = focusedContentStyle;
    }

    public String getFocusedContentClass() {
        return ValueBindings.get(this, "focusedContentClass", focusedContentClass);
    }

    public void setFocusedContentClass(String focusedContentClass) {
        this.focusedContentClass = focusedContentClass;
    }

    public boolean isExpanded() {
        return ValueBindings.get(this, "expanded", expanded, true);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getOnstatechange() {
        return ValueBindings.get(this, "onstatechange", onstatechange);
    }

    public void setOnstatechange(String onstatechange) {
        this.onstatechange = onstatechange;
    }

    public FoldingDirection getFoldingDirection() {
        return ValueBindings.get(this, "foldingDirection", foldingDirection,
                FoldingDirection.DOWN, FoldingDirection.class);
    }

    public void setFoldingDirection(FoldingDirection foldingDirection) {
        this.foldingDirection = foldingDirection;
    }

    public void addStateChangeListener(StateChangeListener listener) {
        addFacesListener(listener);
    }

    public StateChangeListener[] getStateChangeListeners() {
        return (StateChangeListener[]) getFacesListeners(StateChangeListener.class);
    }

    public void removeStateChangeListener(StateChangeListener listener) {
        removeFacesListener(listener);
    }

    public LoadingMode getLoadingMode() {
        return ValueBindings.get(this, "loadingMode", loadingMode, LoadingMode.AJAX, LoadingMode.class);
    }

    public void setLoadingMode(LoadingMode loadingMode) {
        this.loadingMode = loadingMode;
    }

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, expanded, onstatechange, foldingDirection, loadingMode,
                focusable, focusedStyle, focusedClass};
    }

    public void restoreState(FacesContext context, Object object) {
        Object[] values = (Object[]) object;
        int i = 0;
        super.restoreState(context, values[i++]);
        expanded = (Boolean) values[i++];
        onstatechange = (String) values[i++];
        foldingDirection = (FoldingDirection) values[i++];
        loadingMode = (LoadingMode) values[i++];
        focusable = (Boolean) values[i++];
        focusedStyle = (String) values[i++];
        focusedClass = (String) values[i++];
    }

    public void processRestoreState(FacesContext context, Object state) {
        Object ajaxState = AjaxUtil.retrieveAjaxStateObject(context, this);
        super.processRestoreState(context, ajaxState != null ? ajaxState : state);
    }

    public void processDecodes(FacesContext context) {
        if (!isRendered()) return;
        for (UIComponent facet : getFacets().values()) {
            facet.processDecodes(context);
        }
        for (CaptionArea captionArea : ComponentUtil.findChildrenWithClass(this, CaptionArea.class)) {
            captionArea.processDecodes(context);
        }
        if (isContentPreloaded()) {
            for (UIComponent child : getChildren()) {
                child.processDecodes(context);
            }
        }
        try {
            decode(context);
        }
        catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    public void processValidators(FacesContext context) {
        if (!isRendered()) return;
        for (UIComponent facet : getFacets().values()) {
            facet.processValidators(context);
        }
        for (CaptionArea captionArea : ComponentUtil.findChildrenWithClass(this, CaptionArea.class)) {
            captionArea.processValidators(context);
        }
        if (isContentPreloaded()) {
            for (UIComponent child : getChildren()) {
                child.processValidators(context);
            }
        }
    }

    public void processUpdates(FacesContext context) {
        if (!isRendered()) return;
        for (Object o : getFacets().values()) {
            UIComponent facet = (UIComponent) o;
            facet.processUpdates(context);
        }
        for (CaptionArea captionArea : ComponentUtil.findChildrenWithClass(this, CaptionArea.class)) {
            captionArea.processUpdates(context);
        }
        if (isContentPreloaded()) {
            for (UIComponent child : getChildren()) {
                child.processUpdates(context);
            }
        }
        if (expanded != null && ValueBindings.set(this, "expanded", expanded))
            expanded = null;
    }

    private boolean isContentPreloaded() {
        return Boolean.TRUE.equals(getAttributes().get("_contentPreloaded_"));
    }

    public void createSubComponents(FacesContext context) {
        CaptionArea captionArea = (CaptionArea) RenderingUtil.getOrCreateFacet(
                context, this, CaptionArea.COMPONENT_TYPE, "_defaultButtonsArea", CaptionArea.class);
        ComponentUtil.createChildComponent(context, captionArea, ExpansionToggleButton.COMPONENT_TYPE, "toggle");
    }

}