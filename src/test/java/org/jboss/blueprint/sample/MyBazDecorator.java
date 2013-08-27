package org.jboss.blueprint.sample;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author Antoine Sabot-Durand
 */

@Decorator
public abstract class MyBazDecorator implements Baz {

    private static Logger logger = Logger.getLogger(MyBazDecorator.class.getName());

    @Inject
    @Delegate
    @Any
    private Baz delegate;


    @Override
    public int getValue() {
        return delegate.getValue() + 10;
    }
}
