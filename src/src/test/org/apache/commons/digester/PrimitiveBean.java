/* $Id: PrimitiveBean.java 180258 2005-06-06 08:25:55Z skitching $
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
 * A simple bean with primitive properties.
 * At the moment only need a boolean property.
 * Feel free to add others later.
 *
 * @author robert burrell donkin
 */
public class PrimitiveBean {
    
    private boolean booleanValue;
    private boolean setBooleanCalled;
    
    public PrimitiveBean() {}
    
    public boolean getBoolean() {
        return booleanValue;
    }
    
    public boolean getSetBooleanCalled() {
        return setBooleanCalled;
    }

    public void setBoolean(boolean booleanValue) {
        this.booleanValue = booleanValue;
        setBooleanCalled = true;
    }
    
    public void testSetBoolean(String ignored, boolean booleanValue) {
        setBoolean(booleanValue);
    }
}
