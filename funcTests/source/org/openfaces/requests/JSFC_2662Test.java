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
package org.openfaces.requests;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.ElementInspector;
import org.openfaces.test.OpenFacesTestCase;

/**
 * @author Darya Shumilina
 */
public class JSFC_2662Test extends OpenFacesTestCase {

    //todo: test is not completed; there is problem with 'end' key pressing in selenium
    @Test
    public void testUnexpectedAlertByKeyboardActions() throws InterruptedException {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/requests/JSFC-2662.jsf");

        ElementInspector suggestionField = element("formID:degree");
        suggestionField.setCursorPosition(0);
        suggestionField.keyDown(40);
        suggestionField.keyDown(40);

        //press 'enter'
        suggestionField.keyPress(13);
        suggestionField.setCursorPosition(5);
        //press 'delete' button

        suggestionField.keyPress(46);
        //press 'end' button
        suggestionField.keyPress(35);
        assertFalse(selenium.isAlertPresent());
    }

}