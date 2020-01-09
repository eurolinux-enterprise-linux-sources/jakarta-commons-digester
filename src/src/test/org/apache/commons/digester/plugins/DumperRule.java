/* $Id: DumperRule.java 155412 2005-02-26 12:58:36Z dirkv $
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

import org.xml.sax.Attributes;
import org.apache.commons.digester.Rule;

/**
 * Demonstrates the behaviour of the Delegate interface.
 */
public class DumperRule extends Rule {
    public void begin(String namespace, String name, Attributes attributes)
    throws Exception {
        System.out.print("<");
        System.out.print(name);
        
        int nAttributes = attributes.getLength();
        for(int i=0; i<nAttributes; ++i) {
            String key = attributes.getQName(i);
            String value = attributes.getValue(i);
            System.out.print(" ");
            System.out.print(key);
            System.out.print("=");
            System.out.print("'");
            System.out.print(value);
            System.out.print("'");
        }
        System.out.println(">");
    }
    
    public void body(String namespace, String name, String text)
    throws Exception {
        System.out.print(text);
    }
    
    public void end(String namespace, String name)
    throws Exception {
        System.out.print("</");
        System.out.print(name);
        System.out.println(">");
    }
}
