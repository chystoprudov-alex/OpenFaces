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
package org.openfaces.taglib.jsp.ajax;

import org.openfaces.taglib.internal.ajax.SilentSessionExpirationTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Eugene Goncharov
 */
public class SilentSessionExpirationJspTag extends AbstractComponentJspTag {
    public SilentSessionExpirationJspTag() {
        super(new SilentSessionExpirationTag());
    }

    public void setRedirectLocation(ValueExpression redirectLocation) {
        getDelegate().setPropertyValue("redirectLocation", redirectLocation);
    }
}
