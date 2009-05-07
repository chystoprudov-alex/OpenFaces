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
package org.openfaces.component.reloadcomponents;

import org.junit.Test;
import org.openfaces.test.ElementInspector;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.openfaces.InputTextInspector;
import org.openfaces.test.openfaces.LoadingMode;

/**
 * @author Ilya Musihin
 */
public class ReloadComponentsTest extends OpenFacesTestCase {
    @Test
    public void testActionPhase() {
        testAppFunctionalPage("/components/reloadcomponents/reloadComponents.jsf");
        element("form1:resetBtn").clickAndWait(LoadingMode.AJAX);
        ElementInspector counter = element("form1:counter");
        counter.assertText("0");

        // check operation with <h:commandButton> -- a special implementation case
        element("form1:btn1").clickAndWait(LoadingMode.AJAX);
        counter.assertText("1"); // check direct embedding
        element("form1:btn2").clickAndWait(LoadingMode.AJAX);
        counter.assertText("2"); // check attaching with "for" attribute
        element("form1:btn3").clickAndWait(LoadingMode.AJAX);
        counter.assertText("3"); // standalone invokation

        // check operation with <h:commandButton> -- a special implementation case
        element("form1:link1").clickAndWait(LoadingMode.AJAX);
        counter.assertText("4"); // check direct embedding
        element("form1:link2").clickAndWait(LoadingMode.AJAX);
        counter.assertText("5"); // check attaching with "for" attribute
        element("form1:link3").clickAndWait(LoadingMode.AJAX);
        counter.assertText("6"); // standalone invokation

        // check operation with <h:inputText> -- a non-default event, requestDelay

        InputTextInspector inputText = inputText("form1:input1");
        inputText.typeKeys("1");
        sleep(100);
        inputText.typeKeys("2");
        waitForAjax();
        counter.assertText("7"); // check direct embedding
        inputText = inputText("form1:input2");
        inputText.typeKeys("1");
        sleep(100);
        inputText.typeKeys("2");
        waitForAjax();
        counter.assertText("8"); // check attaching with "for" attribute
        inputText = inputText("form1:input3");
        inputText.typeKeys("1");
        sleep(100);
        inputText.typeKeys("2");
        waitForAjax();
        counter.assertText("9"); // standalone invokation

        // check operation with <h:graphicImage> -- a case for a typical JSF component without special implementation cases
        element("form1:image1").clickAndWait(LoadingMode.AJAX);
        counter.assertText("10"); // check direct embedding
        element("form1:image2").clickAndWait(LoadingMode.AJAX);
        counter.assertText("11"); // check attaching with "for" attribute
        element("form1:image3").clickAndWait(LoadingMode.AJAX);
        counter.assertText("12"); // standalone invokation

        // check operation with HTML tags
        element("div2").clickAndWait(LoadingMode.AJAX);
        counter.assertText("13"); // check attaching with "for" attribute
        element("div3").clickAndWait(LoadingMode.AJAX);
        counter.assertText("14"); // standalone invokation
    }
}