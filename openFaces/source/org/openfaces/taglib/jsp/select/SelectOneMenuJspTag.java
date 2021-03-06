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
package org.openfaces.taglib.jsp.select;

import org.openfaces.taglib.internal.select.SelectOneMenuTag;
import org.openfaces.taglib.jsp.input.DropDownFieldJspTagBase;

import javax.el.ValueExpression;

public class SelectOneMenuJspTag extends DropDownFieldJspTagBase {

    public SelectOneMenuJspTag() {
        super(new SelectOneMenuTag());
    }

    public void setFieldStyle(ValueExpression fieldStyle) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.setFieldStyle(fieldStyle);
    }

    public void setRolloverFieldStyle(ValueExpression rolloverFieldStyle) {
        super.setRolloverFieldStyle(rolloverFieldStyle);
    }

    public void setFieldClass(ValueExpression fieldClass) {
        super.setFieldClass(fieldClass);
    }

    public void setRolloverFieldClass(ValueExpression rolloverFieldClass) {
        super.setRolloverFieldClass(rolloverFieldClass);
    }


    public void setButtonClass(ValueExpression buttonClass) {
        super.setButtonClass(buttonClass);
    }

    public void setRolloverButtonClass(ValueExpression rolloverButtonClass) {
        super.setRolloverButtonClass(rolloverButtonClass);
    }

    public void setButtonAlignment(ValueExpression buttonAlignment) {
        super.setButtonAlignment(buttonAlignment);
    }

    public void setButtonImageUrl(ValueExpression buttonImageUrl) {
        super.setButtonImageUrl(buttonImageUrl);
    }

    public void setButtonStyle(ValueExpression buttonStyle) {
        super.setButtonStyle(buttonStyle);
    }

    public void setRolloverButtonStyle(ValueExpression rolloverButtonStyle) {
        super.setRolloverButtonStyle(rolloverButtonStyle);
    }

    public void setPressedButtonStyle(ValueExpression pressedButtonStyle) {
        super.setPressedButtonStyle(pressedButtonStyle);
    }

    public void setPressedButtonClass(ValueExpression pressedButtonClass) {
        super.setPressedButtonClass(pressedButtonClass);
    }

    public void setDisabledButtonClass(ValueExpression disabledButtonClass) {
        super.setDisabledButtonClass(disabledButtonClass);
    }

    public void setDisabledButtonImageUrl(ValueExpression disabledButtonImageUrl) {
        super.setDisabledButtonImageUrl(disabledButtonImageUrl);
    }

    public void setDisabledButtonStyle(ValueExpression disabledButtonStyle) {
        super.setDisabledButtonStyle(disabledButtonStyle);
    }

    public void setDisabledFieldClass(ValueExpression disabledFieldClass) {
        super.setDisabledFieldClass(disabledFieldClass);
    }

    public void setDisabledFieldStyle(ValueExpression disabledFieldStyle) {
        super.setDisabledFieldStyle(disabledFieldStyle);
    }

}
