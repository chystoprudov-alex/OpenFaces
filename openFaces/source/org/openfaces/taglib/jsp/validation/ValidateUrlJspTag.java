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
package org.openfaces.taglib.jsp.validation;

import org.openfaces.taglib.internal.validation.ValidateUrlTag;
import org.openfaces.taglib.jsp.AbstractCustomValidatorJspTag;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ValidateUrlJspTag extends AbstractCustomValidatorJspTag {

    public ValidateUrlJspTag() {
        super(new ValidateUrlTag(), "org.openfaces.URL");
    }

}