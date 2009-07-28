package org.openfaces.renderkit.input;

import javax.faces.context.FacesContext;

import org.openfaces.component.input.SelectBooleanCheckbox;
import org.openfaces.util.ResourceUtil;

public class SelectBooleanCheckboxImageManager {

    public static final String DEFAULT_SELECTED_IMAGE = "selectedCheckbox.gif";
    public static final String DEFAULT_UNSELECTED_IMAGE = "unselectedCheckbox.gif";
    public static final String DEFAULT_UNDEFINED_IMAGE = "undefinedCheckbox.gif";

    public static final String DEFAULT_ROLLOVER_SELECTED_IMAGE = "rolloverSelectedCheckbox.gif";
    public static final String DEFAULT_ROLLOVER_UNSELECTED_IMAGE = "rolloverUnselectedCheckbox.gif";
    public static final String DEFAULT_ROLLOVER_UNDEFINED_IMAGE = "rolloverUndefinedCheckbox.gif";

    public static final String DEFAULT_PRESSED_SELECTED_IMAGE = "pressedSelectedCheckbox.gif";
    public static final String DEFAULT_PRESSED_UNSELECTED_IMAGE = "pressedUnselectedCheckbox.gif";
    public static final String DEFAULT_PRESSED_UNDEFINED_IMAGE = "pressedUndefinedCheckbox.gif";

    public static final String DEFAULT_DISABLED_SELECTED_IMAGE = "disabledSelectedCheckbox.gif";
    public static final String DEFAULT_DISABLED_UNSELECTED_IMAGE = "disabledUnselectedCheckbox.gif";
    public static final String DEFAULT_DISABLED_UNDEFINED_IMAGE = "disabledUndefinedCheckbox.gif";


    public static boolean hasImages(SelectBooleanCheckbox checkbox) {
        return
            isSpecified(checkbox.getSelectedImageUrl())
            || isSpecified(checkbox.getUnselectedImageUrl())
            || isSpecified(checkbox.getUndefinedImageUrl())
            || isSpecified(checkbox.getRolloverSelectedImageUrl())
            || isSpecified(checkbox.getRolloverUnselectedImageUrl())
            || isSpecified(checkbox.getRolloverUndefinedImageUrl())
            || isSpecified(checkbox.getPressedSelectedImageUrl())
            || isSpecified(checkbox.getPressedUnselectedImageUrl())
            || isSpecified(checkbox.getPressedUndefinedImageUrl())
            || isSpecified(checkbox.getDisabledSelectedImageUrl())
            || isSpecified(checkbox.getDisabledUnselectedImageUrl())
            || isSpecified(checkbox.getDisabledUndefinedImageUrl())
            ;
    }

    public static String getCurrentImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        if (checkbox.isDisabled()) {
            if (checkbox.isDefined()) {
                if (checkbox.isSelected()) {
                    return getDisabledSelectedImageUrl(context, checkbox);
                } else {
                    return getDisabledUnselectedImageUrl(context, checkbox);
                }
            } else {
                return getDisabledUndefinedImageUrl(context, checkbox);
            }
        } else {
            if (checkbox.isDefined()) {
                if (checkbox.isSelected()) {
                    return getSelectedImageUrl(context, checkbox);
                } else {
                    return getUnselectedImageUrl(context, checkbox);
                }
            } else {
                return getUndefinedImageUrl(context, checkbox);
            }
        }
    }

    public static String getSelectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getSelectedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_SELECTED_IMAGE);
    }

    public static String getUnselectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getUnselectedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_UNSELECTED_IMAGE);
    }

    public static String getUndefinedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getUndefinedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_UNDEFINED_IMAGE);
    }

    public static String getRolloverSelectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getRolloverSelectedImageUrl(),
                checkbox.getSelectedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_ROLLOVER_SELECTED_IMAGE);
    }

    public static String getRolloverUnselectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getRolloverUnselectedImageUrl(),
                checkbox.getUnselectedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_ROLLOVER_UNSELECTED_IMAGE);
    }

    public static String getRolloverUndefinedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getRolloverUndefinedImageUrl(),
                checkbox.getUndefinedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_ROLLOVER_UNDEFINED_IMAGE);
    }

    public static String getPressedSelectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getPressedSelectedImageUrl(),
                checkbox.getRolloverSelectedImageUrl(),
                checkbox.getSelectedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_PRESSED_SELECTED_IMAGE);
    }

    public static String getPressedUnselectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getPressedUnselectedImageUrl(),
                checkbox.getRolloverUnselectedImageUrl(),
                checkbox.getUnselectedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_PRESSED_UNSELECTED_IMAGE);
    }

    public static String getPressedUndefinedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getPressedUndefinedImageUrl(),
                checkbox.getRolloverUndefinedImageUrl(),
                checkbox.getUndefinedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_PRESSED_UNDEFINED_IMAGE);
    }

    public static String getDisabledSelectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getDisabledSelectedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_DISABLED_SELECTED_IMAGE);
    }

    public static String getDisabledUnselectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getDisabledUnselectedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_DISABLED_UNSELECTED_IMAGE);
    }

    public static String getDisabledUndefinedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getDisabledUndefinedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_DISABLED_UNDEFINED_IMAGE);
    }


    private static String firstSpecified(String... strings) {
        for (String string : strings) {
            if (isSpecified(string)) {
                return string;
            }
        }

        return null;
    }

    private static boolean isSpecified(String string) {
        return string != null && string.length() > 0;
    }

}