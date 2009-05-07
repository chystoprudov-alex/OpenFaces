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
package org.openfaces.util;

import org.openfaces.component.CompoundComponent;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ComponentUtil {
    private static final String AUTO_ID_PREFIX = "j_id";

    private ComponentUtil() {
    }


    public static Object[] anyArrayToObjectArray(Object array) {
        Class componentType = array.getClass().getComponentType();
        if (!componentType.isPrimitive())
            return (Object[]) array;
        Object[] dstArray;
        int arrayLength = Array.getLength(array);
        dstArray = new Object[arrayLength];
        for (int i = 0; i < arrayLength; i++)
            dstArray[i] = Array.get(array, i);
        return dstArray;
    }

    public static String referenceIdToClientId(FacesContext context, UIComponent component, String refId) {
        if (refId == null)
            return refId;

        UIComponent focusedComponent;
        try {
            focusedComponent = component.findComponent(refId);
        } catch (IllegalArgumentException iae) {
            // refId can refer to non-JSF components with "::" symbols as part of Id, which results in an exception
            // from findComponent (see QKS-6242 - interoperation of DropDownField with Focus component).
            focusedComponent = null;
        }

        if (focusedComponent != null) {
            refId = focusedComponent.getClientId(context);
        } else {
            if (refId.indexOf(NamingContainer.SEPARATOR_CHAR) == 0)
                refId = refId.substring(1);
        }
        return refId;
    }


    public static Object setRequestMapValue(String requestMapKey, Object value) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        return requestMap.put(requestMapKey, value);
    }


    public static <T extends UIComponent> T getChildWithClass(UIComponent parent, Class<T> childClass, String defaultIdSuffix) {
        T component = findChildWithClass(parent, childClass);
        if (component != null)
            return component;
        try {
            component = childClass.newInstance();
            component.setId(parent.getId() + RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + defaultIdSuffix);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        parent.getChildren().add(component);

        return component;
    }

    public static <T extends UIComponent> T findChildWithClass(UIComponent parent, Class<T> childClass) {
        List childrenWithClass = findChildrenWithClass(parent, childClass);
        if (childrenWithClass.size() == 0)
            return null;
        return (T) childrenWithClass.get(0);
    }

    public static <T extends UIComponent> List<T> findChildrenWithClass(UIComponent parent, Class<T> childClass) {
        List<T> result = new ArrayList<T>();
        List<UIComponent> children = parent.getChildren();
        for (UIComponent child : children) {
            if (childClass.isAssignableFrom(child.getClass()))
                result.add((T) child);
        }
        return result;
    }

    public static boolean isComponentIdSpecified(UIComponent component) {
        String id = component.getId();
        if (id == null)
            return false;
        if (!id.startsWith(AUTO_ID_PREFIX) || id.length() <= AUTO_ID_PREFIX.length())
            return true;

        String suffix = id.substring(AUTO_ID_PREFIX.length());
        try {
            Integer.parseInt(suffix);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     *
     * This method create new output text component and add it as facet to parent component
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param parent Method will search fo facet in this component or create it, if needed
     * @param idSuffix The suffix identifying the {@link javax.faces.component.html.HtmlOutputText} to be returned
     * @param text The text in output text field
     * @return created or existed output text component
     */
    public static HtmlOutputText composeHtmlOutputText(FacesContext context, UIComponent parent, String idSuffix, String text) {
        HtmlOutputText outputText = (HtmlOutputText) RenderingUtil.getOrCreateFacet(context, parent,
                HtmlOutputText.COMPONENT_TYPE, idSuffix, HtmlOutputText.class);
        outputText.setValue(text);
        return outputText;
    }

    /**
     *
     * This method add child to parent component
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param parent Method will create child for this component
     * @param componentType The class for child creation
     * @param idSuffix The suffix identifying the child {@link javax.faces.component.UIComponent} to be returned
     * @return created child
     */
    public static UIComponent createChildComponent(
            FacesContext context, UIComponent parent, String componentType, String idSuffix) {
        String childId = generateIdWithSuffix(parent, idSuffix);
        UIComponent component = createComponent(context, childId, componentType);
        parent.getChildren().add(component);
        return component;
    }

    /**
     *
     * This method create components with given name and class and create, if needed, its subcomponents
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param id The id identifying the {@link javax.faces.component.UIComponent} to be returned
     * @param componentType The component type for which to create and return a new {@link javax.faces.component.UIComponent} instance
     * @return
     */
    public static UIComponent createComponent(FacesContext context, String id, String componentType) {
        Application application = context.getApplication();
        UIComponent component = application.createComponent(componentType);
        component.setId(id);
        if (component instanceof CompoundComponent)
            ((CompoundComponent) component).createSubComponents(context);
        return component;
    }

    /**
     *
     * This method create new command button component and add it as facet to parent component
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param parent Method will search fo facet in this component or create it, if needed
     * @param idSuffix The suffix identifying the {@link javax.faces.component.html.HtmlCommandButton} to be returned
     * @param text The text on command button
     * @return created or existed command button component
     */
    public static HtmlCommandButton createButtonFacet(FacesContext context, UIComponent parent, String idSuffix, String text) {
        HtmlCommandButton prevBtn = (HtmlCommandButton) RenderingUtil.getOrCreateFacet(
                context, parent, HtmlCommandButton.COMPONENT_TYPE, idSuffix, HtmlCommandButton.class);
        prevBtn.setValue(text);
        return prevBtn;
    }

    /**
     * Generate id on base of component id.
     *
     * @param baseComponent The component, which id will be used for generating
     * @param idSuffix The suffix, which will be added to component id
     * @return generated id
     */
    public static String generateIdWithSuffix(UIComponent baseComponent, String idSuffix) {
        generateIdIfNotSpecified(baseComponent); // null id may have place when creating a component programmatically (especially in JSF RI 1.2, where id is not assigned in getClientId automatically)
        String result = baseComponent.getId() + RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + idSuffix;
        return result;
    }

    /**
     * Create {@link javax.faces.component.html.HtmlOutputText} component with given text
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param text The text to be set in output text
     * @param escape Flag indicating that characters that are sensitive in HTML and XML markup must be escaped.
     * @return the created {@link javax.faces.component.html.HtmlOutputText}
     */
    public static HtmlOutputText createOutputText(FacesContext context, String text, boolean escape) {
        HtmlOutputText outputText = (HtmlOutputText) context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
        outputText.setValue(text);
        outputText.setEscape(escape);
        return outputText;
    }

    /**
     *
     * Create {@link javax.faces.component.html.HtmlOutputText} component with given text
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param text The text to be set in output text
     * @return the created {@link javax.faces.component.html.HtmlOutputText}
     */
    public static HtmlOutputText createOutputText(FacesContext context, String text) {
        return createOutputText(context, text, true);
    }

    /**
     *
     * Return the closest form for component
     *
     * @param component The component, which form we obtain
     * @return the nearest enclosing form for component
     */
    public static UIForm getEnclosingForm(UIComponent component) {
        UIComponent result = component;
        while (result != null) {
            if (result instanceof UIForm) {
                return (UIForm) result;
            }
            result = result.getParent();
        }
        return null;
    }

    public static UIForm findForm(UIComponent component) {
        while (component != null) {
            if (component instanceof UIForm)
                return (UIForm) component;
            component = component.getParent();
        }
        return null;
    }

    /**
     *
     * Check component id and generate it, if nessesary
     *
     * @param component The component for id generation
     */
    public static void generateIdIfNotSpecified(UIComponent component) {
        if (component.getId() != null)
            return;
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        component.setId(viewRoot.createUniqueId());
    }

    /**
     * Find child component by its suffix
     *
     * @see #generateIdWithSuffix
     * @param component The parent component to search in
     * @param idSuffix The suffix identifying the {@link javax.faces.component.UIComponent} to be returned
     * @return the found {@link javax.faces.component.UIComponent}, or <code>null</code> if the component was not found.
     */
    public static UIComponent getChildBySuffix(UIComponent component, String idSuffix) {
        String childId = generateIdWithSuffix(component, idSuffix);
        return component.findComponent(childId);
    }
}