/* $Id: RulesBaseTestCase.java 155412 2005-02-26 12:58:36Z dirkv $
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


package org.apache.commons.digester;


import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>Test Case for the RulesBase matching rules.
 * Most of this material was original contained in the digester test case
 * but was moved into this class so that extensions of the basic matching rules
 * behaviour can extend this test case.
 * </p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 155412 $ $Date: 2005-02-27 01:58:36 +1300 (Sun, 27 Feb 2005) $
 */

public class RulesBaseTestCase extends TestCase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The digester instance we will be processing.
     */
    protected Digester digester = null;

    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public RulesBaseTestCase(String name) {

        super(name);

    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        digester = new Digester();
        digester.setRules(createMatchingRulesForTest());

    }

    /**
     * <p> This should be overriden by subclasses.
     *
     * @return the matching rules to be tested.
     */
    protected Rules createMatchingRulesForTest() {
        return new RulesBase();
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(RulesBaseTestCase.class));

    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        digester = null;

    }



    // ------------------------------------------------ Individual Test Methods

    /**
     * Basic test for rule creation and matching.
     */
    public void testRules() {

        // clear any existing rules
        digester.getRules().clear();

        // perform tests
        List list = null;

        assertEquals("Initial rules list is empty",
                0, digester.getRules().match("a").size());
        digester.addSetProperties("a");
        assertEquals("Add a matching rule",
                1, digester.getRules().match(null, "a").size());
        digester.addSetProperties("b");
        assertEquals("Add a non-matching rule",
                1, digester.getRules().match(null, "a").size());
        digester.addSetProperties("a/b");
        assertEquals("Add a non-matching nested rule",
                1, digester.getRules().match(null, "a").size());
        digester.addSetProperties("a/b");
        assertEquals("Add a second matching rule",
                2, digester.getRules().match(null, "a/b").size());

        // clean up
        digester.getRules().clear();

    }

    /**
     * <p>Test matching rules in {@link RulesBase}.</p>
     *
     * <p>Tests:</p>
     * <ul>
     * <li>exact match</li>
     * <li>tail match</li>
     * <li>longest pattern rule</li>
     * </ul>
     */
    public void testRulesBase() {

        // clear any existing rules
        digester.getRules().clear();

        assertEquals("Initial rules list is empty",
                0, digester.getRules().rules().size());

        // We're going to set up
        digester.addRule("a/b/c/d", new TestRule("a/b/c/d"));
        digester.addRule("*/d", new TestRule("*/d"));
        digester.addRule("*/c/d", new TestRule("*/c/d"));

        // Test exact match
        assertEquals("Exact match takes precedence 1",
                1, digester.getRules().match(null, "a/b/c/d").size());
        assertEquals("Exact match takes precedence 2",
                "a/b/c/d",
                ((TestRule) digester.getRules().match(null, "a/b/c/d").iterator().next()).getIdentifier());

        // Test wildcard tail matching
        assertEquals("Wildcard tail matching rule 1",
                1, digester.getRules().match(null, "a/b/d").size());
        assertEquals("Wildcard tail matching rule 2",
                "*/d",
                ((TestRule) digester.getRules().match(null, "a/b/d").iterator().next()).getIdentifier());

        // Test the longest matching pattern rule
        assertEquals("Longest tail rule 1",
                1, digester.getRules().match(null, "x/c/d").size());
        assertEquals("Longest tail rule 2",
                "*/c/d",
                ((TestRule) digester.getRules().match(null, "x/c/d").iterator().next()).getIdentifier());

        // Test wildcard tail matching at the top level,
        // i.e. the wildcard is nothing
        digester.addRule("*/a", new TestRule("*/a"));
        assertEquals("Wildcard tail matching rule 3",
                     1,
                     digester.getRules().match(null,"a").size());

        assertEquals("Wildcard tail matching rule 3 (match too much)",
                     0,
                     digester.getRules().match(null,"aa").size());
        // clean up
        digester.getRules().clear();

    }

    /**
     * Test basic matchings involving namespaces.
     */
    public void testBasicNamespaceMatching() {

        List list = null;
        Iterator it = null;

        // clear any existing rules
        digester.getRules().clear();

        assertEquals("Initial rules list is empty",
                0, digester.getRules().rules().size());

        // Set up rules
        digester.addRule("alpha/beta/gamma", new TestRule("No-Namespace"));
        digester.addRule("alpha/beta/gamma", new TestRule("Euclidean-Namespace", "euclidean"));


        list = digester.getRules().rules();

        // test that matching null namespace brings back namespace and non-namespace rules
        list = digester.getRules().match(null, "alpha/beta/gamma");

        assertEquals("Null namespace match (A)", 2, list.size());

        it = list.iterator();
        assertEquals("Null namespace match (B)", "No-Namespace", ((TestRule) it.next()).getIdentifier());
        assertEquals("Null namespace match (C)", "Euclidean-Namespace", ((TestRule) it.next()).getIdentifier());



        // test that matching euclid namespace brings back namespace and non-namespace rules
        list = digester.getRules().match("euclidean", "alpha/beta/gamma");

        assertEquals("Matching namespace match (A)", 2, list.size());

        it = list.iterator();
        assertEquals("Matching namespace match (B)", "No-Namespace", ((TestRule) it.next()).getIdentifier());
        assertEquals("Matching namespace match (C)", "Euclidean-Namespace", ((TestRule) it.next()).getIdentifier());



        // test that matching another namespace brings back only non-namespace rule
        list = digester.getRules().match("hyperbolic", "alpha/beta/gamma");

        assertEquals("Non matching namespace match (A)", 1, list.size());

        it = list.iterator();
        assertEquals("Non matching namespace match (B)", "No-Namespace", ((TestRule) it.next()).getIdentifier());

        // clean up
        digester.getRules().clear();

    }

    /**
     * Rules must always be returned in the correct order.
     */
    public void testOrdering() {

        // clear any existing rules
        digester.getRules().clear();

        assertEquals("Initial rules list is empty",
                0, digester.getRules().rules().size());

        // Set up rules
        digester.addRule("alpha/beta/gamma", new TestRule("one"));
        digester.addRule("alpha/beta/gamma", new TestRule("two"));
        digester.addRule("alpha/beta/gamma", new TestRule("three"));

        // test that rules are returned in set order
        List list = digester.getRules().match(null, "alpha/beta/gamma");

        assertEquals("Testing ordering mismatch (A)", 3, list.size());

        Iterator it = list.iterator();
        assertEquals("Testing ordering mismatch (B)", "one", ((TestRule) it.next()).getIdentifier());
        assertEquals("Testing ordering mismatch (C)", "two", ((TestRule) it.next()).getIdentifier());
        assertEquals("Testing ordering mismatch (D)", "three", ((TestRule) it.next()).getIdentifier());

        // clean up
        digester.getRules().clear();

    }
    
    /** Tests the behaviour when a rule is added with a trailing slash*/
    public void testTrailingSlash() {
        // clear any existing rules
        digester.getRules().clear();

        assertEquals("Initial rules list is empty",
                0, digester.getRules().rules().size());

        // Set up rules
        digester.addRule("alpha/beta/gamma/", new TestRule("one"));
        digester.addRule("alpha/beta/", new TestRule("two"));
        digester.addRule("beta/gamma/alpha", new TestRule("three"));

        // test that rules are returned in set order
        List list = digester.getRules().match(null, "alpha/beta/gamma");

        assertEquals("Testing number of matches", 1, list.size());

        Iterator it = list.iterator();
        assertEquals("Testing ordering (A)", "one", ((TestRule) it.next()).getIdentifier());

        // clean up
        digester.getRules().clear();  
    }
}
