/* $Id: TestObject.java 155412 2005-02-26 12:58:36Z dirkv $
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

package org.apache.commons.digester.xmlrules;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Test harness object for holding results of digestion.
 *
 * @author David H. Martin - Initial Contribution
 * @author Scott Sanders   - Added ASL, removed external dependencies
 * @author Tim O'Brien - Added bean property to test bean property setter rule
 */
public class TestObject {

    private ArrayList children = new ArrayList();
    private String value = "";
    private Long longValue = new Long(-1L);

    private String property = "";

    private HashMap mapValue = new HashMap();

    private boolean pushed = false;
    
    public TestObject() {
    }

    private static int idx = 0;

    public String toString() {
        String str = value;
        for (Iterator i = children.iterator(); i.hasNext();) {
            str += " " + i.next();
        }
        return str;
    }

    public void add(Object o) {
        children.add(o);
    }

    public void setValue(String val) {
        value = val;
    }

    public void setLongValue(Long val) {
        longValue = val;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setStringValue(String val) {
    }

    public boolean isPushed() {
        return pushed;
    }
    
    public void push() {
        pushed = true;
    }

    public void setMapValue( String name, String value ) {
        this.mapValue.put( name, value );
    }

    public String getMapValue( String name ) {
        return (String) this.mapValue.get( name );
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String pProperty) {
        property = pProperty;
    }
}
