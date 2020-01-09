/* $Id: TestConfigurablePluginAttributes.java 179714 2005-06-03 03:53:39Z skitching $
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
import java.util.LinkedList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.digester.Digester;

/**
 * Test cases for functionality which sets what xml attributes specify
 * the plugin class or plugin declaration id.
 */

public class TestConfigurablePluginAttributes extends TestCase {
    /** Standard constructor */
    public TestConfigurablePluginAttributes(String name) { 
        super(name);
    }

    /** Set up instance variables required by this test case. */
    public void setUp() {}

    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(TestConfigurablePluginAttributes.class));

    }

    /** Tear down instance variables required by this test case.*/
    public void tearDown() {}
        
    // --------------------------------------------------------------- Test cases
    
    public void testDefaultBehaviour() throws Exception {
        // tests that by default the attributes used are 
        // named "plugin-class" and "plugin-id"

        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        PluginRules rc = new PluginRules();
        digester.setRules(rc);
        
        PluginDeclarationRule pdr = new PluginDeclarationRule();
        digester.addRule("root/plugin", pdr);
        
        PluginCreateRule widgetPluginRule = new PluginCreateRule(Widget.class);
        digester.addRule("root/widget", widgetPluginRule);
        digester.addSetNext("root/widget", "addWidget");

        PluginCreateRule gadgetPluginRule = new PluginCreateRule(Widget.class);
        digester.addRule("root/gadget", gadgetPluginRule);
        digester.addSetNext("root/gadget", "addGadget");

        MultiContainer root = new MultiContainer();
        digester.push(root);
        
        try {
            digester.parse(
                TestAll.getInputStream(this, "test7.xml"));

        } catch(Exception e) {
            throw e;
        }

        Object child;
        
        List widgets = root.getWidgets();
        assertTrue(widgets != null);
        assertEquals(4, widgets.size());

        assertEquals(TextLabel.class, widgets.get(0).getClass());
        assertEquals(TextLabel.class, widgets.get(1).getClass());
        assertEquals(TextLabel.class, widgets.get(2).getClass());
        assertEquals(TextLabel.class, widgets.get(3).getClass());
        
        List gadgets = root.getGadgets();
        assertTrue(gadgets != null);
        assertEquals(4, gadgets.size());

        assertEquals(TextLabel.class, gadgets.get(0).getClass());
        assertEquals(TextLabel.class, gadgets.get(1).getClass());
        assertEquals(TextLabel.class, gadgets.get(2).getClass());
        assertEquals(TextLabel.class, gadgets.get(3).getClass());
    }
    
    public void testGlobalOverride() throws Exception {
        // Tests that using setDefaultPluginXXXX overrides behaviour for all
        // PluginCreateRule instances. Also tests specifying attributes
        // with "null" for namespace (ie attributes not in any namespace).
        //
        // note that in order not to screw up all other tests, we need
        // to reset the global names after we finish here!

        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        PluginRules rc = new PluginRules();
        digester.setRules(rc);

        rc.setPluginIdAttribute(null, "id");
        rc.setPluginClassAttribute(null, "class");
     
        PluginDeclarationRule pdr = new PluginDeclarationRule();
        digester.addRule("root/plugin", pdr);
        
        PluginCreateRule widgetPluginRule = new PluginCreateRule(Widget.class);
        digester.addRule("root/widget", widgetPluginRule);
        digester.addSetNext("root/widget", "addWidget");

        PluginCreateRule gadgetPluginRule = new PluginCreateRule(Widget.class);
        digester.addRule("root/gadget", gadgetPluginRule);
        digester.addSetNext("root/gadget", "addGadget");

        MultiContainer root = new MultiContainer();
        digester.push(root);
        
        try {
            digester.parse(
                TestAll.getInputStream(this, "test7.xml"));
                
        } catch(Exception e) {
            throw e;
        }

        Object child;
        
        List widgets = root.getWidgets();
        assertTrue(widgets != null);
        assertEquals(4, widgets.size());

        assertEquals(Slider.class, widgets.get(0).getClass());
        assertEquals(Slider.class, widgets.get(1).getClass());
        assertEquals(Slider.class, widgets.get(2).getClass());
        assertEquals(Slider.class, widgets.get(3).getClass());
        
        List gadgets = root.getGadgets();
        assertTrue(gadgets != null);
        assertEquals(4, gadgets.size());

        assertEquals(Slider.class, gadgets.get(0).getClass());
        assertEquals(Slider.class, gadgets.get(1).getClass());
        assertEquals(Slider.class, gadgets.get(2).getClass());
        assertEquals(Slider.class, gadgets.get(3).getClass());
    }
    
    public void testInstanceOverride() throws Exception {
        // Tests that using setPluginXXXX overrides behaviour for only
        // that particular PluginCreateRule instance. Also tests that
        // attributes can be in namespaces.

        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        PluginRules rc = new PluginRules();
        digester.setRules(rc);

        PluginDeclarationRule pdr = new PluginDeclarationRule();
        digester.addRule("root/plugin", pdr);

        // for plugins at pattern "root/widget", use xml attributes "id" and
        // "class" in the custom namespace as the values for plugin id and
        // class, not the default (and non-namespaced) values of 
        // "plugin-id" and "plugin-class".
        PluginCreateRule widgetPluginRule = new PluginCreateRule(Widget.class);
        widgetPluginRule.setPluginIdAttribute(
            "http://jakarta.apache.org/digester/plugins", "id");
        widgetPluginRule.setPluginClassAttribute(
            "http://jakarta.apache.org/digester/plugins", "class");
        digester.addRule("root/widget", widgetPluginRule);
        digester.addSetNext("root/widget", "addWidget");

        PluginCreateRule gadgetPluginRule = new PluginCreateRule(Widget.class);
        digester.addRule("root/gadget", gadgetPluginRule);
        digester.addSetNext("root/gadget", "addGadget");

        MultiContainer root = new MultiContainer();
        digester.push(root);
        
        try {
            digester.parse(
                TestAll.getInputStream(this, "test7.xml"));
        } catch(Exception e) {
            throw e;
        }

        Object child;
        
        List widgets = root.getWidgets();
        assertTrue(widgets != null);
        assertEquals(4, widgets.size());

        assertEquals(TextLabel2.class, widgets.get(0).getClass());
        assertEquals(TextLabel2.class, widgets.get(1).getClass());
        assertEquals(TextLabel2.class, widgets.get(2).getClass());
        assertEquals(TextLabel2.class, widgets.get(3).getClass());
        
        List gadgets = root.getGadgets();
        assertTrue(gadgets != null);
        assertEquals(4, gadgets.size());

        assertEquals(TextLabel.class, gadgets.get(0).getClass());
        assertEquals(TextLabel.class, gadgets.get(1).getClass());
        assertEquals(TextLabel.class, gadgets.get(2).getClass());
        assertEquals(TextLabel.class, gadgets.get(3).getClass());
    }
    
    // inner classes used for testing
    
    public static class MultiContainer {
        private LinkedList widgets = new LinkedList();
        private LinkedList gadgets = new LinkedList();
    
        public MultiContainer() {}
        
        public void addWidget(Widget child) {
            widgets.add(child);
        }
    
        public List getWidgets() {
            return widgets;
        }

        public void addGadget(Widget child) {
            gadgets.add(child);
        }
    
        public List getGadgets() {
            return gadgets;
        }
    }
}
