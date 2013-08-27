package org.jboss.blueprint.cdi.extension;

import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;
import org.osgi.service.blueprint.container.BlueprintContainer;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionTarget;
import java.util.logging.Logger;

/**
 * @author Antoine Sabot-Durand
 */
public class BlueprintContextualLifecycle<T> implements ContextualLifecycle<T> {

    private static Logger logger = Logger.getLogger(BlueprintContextualLifecycle.class.getName());
    private final BlueprintContainer blueprintContainer;
    private final String beanId;
    private final InjectionTarget<T> injectionTarget;

    public BlueprintContextualLifecycle(InjectionTarget<T> injectionTarget, BlueprintContainer blueprintContainer, String beanId) {
        super();
        this.injectionTarget = injectionTarget;
        this.blueprintContainer = blueprintContainer;
        this.beanId = beanId;
    }

    @Override
    public T create(Bean<T> bean, CreationalContext<T> creationalContext) {
        logger.info("Instantiate bean with ID: " + beanId);
        T instance;

        try {
            instance = (T) blueprintContainer.getComponentInstance(beanId);
        } catch (ClassCastException e) {
            throw new IllegalStateException("Registered CDI bean and Blueprint Bean:" + beanId + " don't have the same class", e);
        }
        injectionTarget.inject(instance, creationalContext);
        injectionTarget.postConstruct(instance);
        return instance;
    }

    @Override
    public void destroy(Bean<T> bean, T instance, CreationalContext<T> creationalContext) {
        logger.info("Destruction of an instance for bean:" + beanId);
    }
}
