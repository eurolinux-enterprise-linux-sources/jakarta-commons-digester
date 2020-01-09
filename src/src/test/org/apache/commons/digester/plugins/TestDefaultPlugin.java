/* $Id: TestDefaultPlugin.java 179714 2005-06-03 03:53:39Z skitching $
 *
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 


package org.apache.commons.digester.plugins;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.digester.Digester;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.NoOpLog;

/**
 * Test cases for the use of default plugin classes.
 */

import org.xml.sax.SAXParseException;

public class TestDefaultPlugin extends TestCase {
    /** Standard constructor */
    public TestDefaultPlugin(String name) { 
        super(name);
    }

    /** Set up instance variables required by this test case. */
    public void setUp() {}

    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(TestDefaultPlugin.class));

    }

    /** Tear down instance variables required by this test case.*/
    public void tearDown() {}
        
    // --------------------------------------------------------------- Test cases
    
    public void testDefaultPlugins1() throws Exception {
        // * tests that when a PluginCreateRule is defined with a default
        //   class, that the default class is instantiated when no class
        //   or id is specified in the xml file.
        Digester digester = new Digester();
        PluginRules rc = new PluginRules();
        digester.setRules(rc);
        
        PluginCreateRule pcr = new PluginCreateRule(Widget.class, TextLabel.class);
        digester.addRule("root/widget", pcr);
        digester.addSetNext("root/widget", "addChild");

        Container root = new Container();
        digester.push(root);
        
        try {
            digester.parse(
                TestAll.getInputStream(this, "test2.xml"));
        }
        catch(Exception e) {
            throw e;
        }
        
        Object child;
        List children = root.getChildren();
        assertTrue(children != null);
        assertEquals(3, children.size());
        
        child = children.get(0);
        assertTrue(child != null);
        assertEquals(TextLabel.class, child.getClass());
        TextLabel label1 = (TextLabel) child;
        assertEquals("label1", label1.getLabel());
        
        child = children.get(1);
        assertTrue(child != null);
        assertEquals(TextLabel.class, child.getClass());
        TextLabel label2 = (TextLabel) child;
        assertEquals("label2", label2.getLabel());
        
        child = children.get(2);
        assertTrue(child != null);
        assertEquals(Slider.class, child.getClass());
        Slider slider1 = (Slider) child;
        assertEquals("slider1", slider1.getLabel());
    }

    public void testDefaultPlugins2() throws Exception {
        // * tests that when there is no default plugin, it is an error
        //   not to have one of plugin-class or plugin-id specified
        Digester digester = new Digester();
        PluginRules rc = new PluginRules();
        digester.setRules(rc);
        
        PluginCreateRule pcr = new PluginCreateRule(Widget.class);
        digester.addRule("root/widget", pcr);
        digester.addSetNext("root/widget", "addChild");

        Container root = new Container();
        digester.push(root);
        
        Exception exception = null;
        Log oldLog = digester.getLogger();
        try {
            digester.setLogger(new NoOpLog());
            digester.parse(
                TestAll.getInputStream(this, "test2.xml"));
        }
        catch(Exception e) {
            exception = e;
        }
        finally {
            digester.setLogger(oldLog);
        }
        
        assertTrue(exception != null);
        assertEquals(SAXParseException.class, exception.getClass());
        assertEquals(
            PluginInvalidInputException.class, 
            ((SAXParseException)exception).getException().getClass());
    }

    public void testDefaultPlugins3() throws Exception {
        // * tests that the default plugin must implement or extend the
        //   plugin base class.
        Digester digester = new Digester();
        PluginRules rc = new PluginRules();
        digester.setRules(rc);

        PluginCreateRule pcr = new PluginCreateRule(Widget.class, Object.class);
        digester.addRule("root/widget", pcr);
        digester.addSetNext("root/widget", "addChild");

        Container root = new Container();
        digester.push(root);
        
        Exception exception = null;
        Log oldLog = digester.getLogger();
        try {
            digester.setLogger(new NoOpLog());
            digester.parse(
                TestAll.getInputStream(this, "test2.xml"));
        }
        catch(Exception e) {
            exception = e;
        }
        finally {
            digester.setLogger(oldLog);
        }
        
        assertTrue(exception != null);
        assertEquals(SAXParseException.class, exception.getClass());
        assertEquals(
            PluginConfigurationException.class, 
            ((SAXParseException)exception).getException().getClass());
    }
}
