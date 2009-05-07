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
package org.openfaces.component.table;

import org.openfaces.util.RenderingUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class DynamicTableColumn extends TableColumn implements DynamicColumn {
    public static final String COMPONENT_TYPE = "org.openfaces.DynamicTableColumn";
    public static final String COMPONENT_FAMILY = "org.openfaces.DynamicTableColumn";

    private TableColumns columns;
    private Object colData;
    private Object prevVarValue;
    private int colIndex;

    public DynamicTableColumn() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setColumns(TableColumns columns) {
        this.columns = columns;
    }

    public void setColData(Object colData) {
        this.colData = colData;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    protected AbstractTable getTable() {
        return columns.getTable();
    }

    public UIComponent getHeader() {
        return columns.getHeader();
    }

    public void setHeader(UIComponent header) {
        columns.setHeader(header);
    }

    public UIComponent getFooter() {
        return columns.getFooter();
    }

    public void setFooter(UIComponent footer) {
        columns.setFooter(footer);
    }

    public void declareContextVariables() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        String var = columns.getVar();
        prevVarValue = requestMap.get(var);
        requestMap.put(var, colData);
        columns.setColumnIndex(colIndex);
    }

    public void undeclareContextVariables() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        requestMap.put(columns.getVar(), prevVarValue);
        prevVarValue = null;
        columns.setColumnIndex(-1);
    }

    public void encodeBegin(FacesContext context) throws IOException {
        declareContextVariables();
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context) throws IOException {
        RenderingUtil.renderChildren(context, columns);
    }

    public void encodeEnd(FacesContext context) throws IOException {
        undeclareContextVariables();
    }

    public List getChildrenForProcessing() {
        return columns.getChildren();
    }

    public Map getFacetsForProcessing() {
        return columns.getFacets();
    }

    public void processDecodes(FacesContext context) {
        declareContextVariables();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processDecodes(context);
        }
        undeclareContextVariables();
    }

    private Collection<UIComponent> getFacetCollection() {
        ArrayList<UIComponent> facets = new ArrayList<UIComponent>();
        UIComponent header = getHeader();
        UIComponent footer = getFooter();
        UIComponent subHeader = getSubHeader();
        if (header != null)
            facets.add(header);
        if (footer != null)
            facets.add(footer);
        if (subHeader != null)
            facets.add(subHeader);
        return facets;
    }

    public void processValidators(FacesContext context) {
        declareContextVariables();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processValidators(context);
        }
        undeclareContextVariables();
    }

    public void processUpdates(FacesContext context) {
        declareContextVariables();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processUpdates(context);
        }
        undeclareContextVariables();
    }

}