/* $Id: NamedBean.java 155412 2005-02-26 12:58:36Z dirkv $
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


/**
 * Very simple test bean
 *
 * @author Robert Burrell Donkin
 * @version $Revision: 155412 $ $Date: 2005-02-27 01:58:36 +1300 (Sun, 27 Feb 2005) $
 */

public class NamedBean {
    
    private String name = "**UNSET**";
    
    public NamedBean() {}
    
    public NamedBean(String name) {}
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void test(String name, String ignored) {
        setName(name);
    }
    
    public String toString() {
        return "NamedBean[" + getName() + "]";
    }
}
