package org.jboss.blueprint.sample;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;
import java.util.Map;

/**
 * @author Antoine Sabot-Durand
 */
public interface Foo extends Serializable {
    int getA();

    void setA(int i);

    int getB();

    void setB(int i);

    Bar getBar();

    void setBar(Bar b);

    Currency getCurrency();

    void setCurrency(Currency c);

    Date getDate();

    void setDate(Date d);

    void init();

    void destroy();

    boolean isInitialized();

    boolean isDestroyed();

    void update(Map<String, Object> props);

    Map<String, Object> getProps();
}
