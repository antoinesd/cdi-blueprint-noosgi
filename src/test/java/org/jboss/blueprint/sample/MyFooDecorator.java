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
public abstract class MyFooDecorator implements Foo {

    private static Logger logger = Logger.getLogger(MyFooDecorator.class.getName());

    @Inject
    @Delegate
    @Any
    private Foo delegate;


    @Override
    public int getB() {
        logger.info("In FOO decorator");
        return delegate.getA() + 5;
    }
}
