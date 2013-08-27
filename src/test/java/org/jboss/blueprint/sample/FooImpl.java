/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jboss.blueprint.sample;

import java.util.Currency;
import java.util.Date;
import java.util.Map;

public class FooImpl implements Foo {

    private int a;
    private int b;
    private Bar bar;
    private Currency currency;
    private Date date;

    public boolean initialized;
    public boolean destroyed;
    private Map<String, Object> props;

    @Override
    public int getA() {
        return a;
    }

    @Override
    public void setA(int i) {
        a = i;
    }

    @Override
    public int getB() {
        return b;
    }

    @Override
    public void setB(int i) {
        b = i;
    }

    @Override
    public Bar getBar() {
        return bar;
    }

    @Override
    public void setBar(Bar b) {
        bar = b;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(Currency c) {
        currency = c;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date d) {
        date = d;
    }

    public String toString() {
        return a + " " + b + " " + bar + " " + currency + " " + date;
    }

    @Override
    public void init() {
        System.out.println("======== Initializing Foo =========");
        initialized = true;
    }

    @Override
    public void destroy() {
        System.out.println("======== Destroying Foo =========");
        destroyed = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void update(Map<String, Object> props) {
        this.props = props;
    }

    @Override
    public Map<String, Object> getProps() {
        return props;
    }

}

