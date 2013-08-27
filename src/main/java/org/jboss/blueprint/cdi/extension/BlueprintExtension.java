package org.jboss.blueprint.cdi.extension;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.aries.blueprint.container.BlueprintContainerImpl;
import org.apache.deltaspike.core.api.literal.ApplicationScopedLiteral;
import org.apache.deltaspike.core.api.literal.DefaultLiteral;
import org.apache.deltaspike.core.api.literal.DependentScopeLiteral;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.ServiceMetadata;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Antoine Sabot-Durand
 */
public class BlueprintExtension implements Extension {

    private static Logger logger = Logger.getLogger(BlueprintExtension.class.getName());
    private final URL url = getClass().getClassLoader().getResource("test.xml");
    private BlueprintContainer container;
    private Multimap<Class<?>, BeanMetadata> blueprintBeanMap = HashMultimap.create(); //associating class with all beans
    // metadata implementing this class

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bd) {
        logger.info("Starting CDI blueprint bridge");
        try {
            container = new BlueprintContainerImpl(getClass().getClassLoader(), getBlueprintResources());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize Blueprint Container", e);
        }

        logger.info("analyzing Blueprint managed beans");
        ComponentMetadata meta;
        for (String id : container.getComponentIds()) {
            logger.info("working on component " + id);
            meta = container.getComponentMetadata(id);


            if (meta instanceof BeanMetadata) {
                logger.info("Component is a Bean");
                BeanMetadata beanMeta = (BeanMetadata) meta;
                if (beanMeta.getClassName() != null) {
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(beanMeta.getClassName());
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException("Unable to find class " + beanMeta.getClassName(), e);
                    }
                    blueprintBeanMap.put(clazz, beanMeta);
                }
            } else if (meta instanceof ServiceMetadata) {
                logger.info("Component is a Service");
            } else {
                logger.info("Component is Generic with class: " + meta.getClass());
            }
        }
        logger.info(blueprintBeanMap.toString());

    }

    public void processAT(@Observes ProcessAnnotatedType<?> pat) {
        AnnotatedType<?> at = pat.getAnnotatedType();
        if (blueprintBeanMap.containsKey(at.getBaseType()) && !at.isAnnotationPresent(javax.decorator.Decorator.class)) {
            logger.info("Found Blueprint Bean for Annotated Type: " + at.toString());
            pat.veto(); //we avoid registering BP bean automatically in CDI, we'll do  it in AfterBeanDiscovery phase
        }

    }

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {

        logger.info("*** Registering blueprint beans as CDI beans ***");

        for (Class clazz : blueprintBeanMap.keySet()) {
            logger.info("== working on bean of class: " + clazz + " ==");

            Collection<BeanMetadata> beanMetadatas = blueprintBeanMap.get(clazz);
            for (BeanMetadata meta : beanMetadatas) {

                AnnotatedTypeBuilder atb = new AnnotatedTypeBuilder<Object>();
                atb.setJavaClass(clazz)
                        .addToClass(new BeanIdLitteral(meta.getId()));

                if (beanMetadatas.size() == 1) {
                    atb.addToClass(new DefaultLiteral());
                }

                if (meta.getScope() == BeanMetadata.SCOPE_PROTOTYPE) {
                    logger.info("Dependent");
                    atb.addToClass(new DependentScopeLiteral());
                } else {
                    logger.info("Singleton");
                    atb.addToClass(new ApplicationScopedLiteral());
                }

                AnnotatedType at = atb.create();
                BeanBuilder<?> bb = new BeanBuilder<Object>(bm);

                bb.readFromType(at)
                        .passivationCapable(true)
                        .beanLifecycle(new BlueprintContextualLifecycle(bm.createInjectionTarget(at), container,
                                meta.getId()));


                abd.addBean(bb.create());


                logger.info("- Registered bean: " + meta.getId() + " -");
            }

        }

    }

    public void afterDeploymentValidation(@Observes AfterDeploymentValidation adv, BeanManager bm) {
        logger.info("Finishing Blueprint CDI bridge initialization");
    }

    private List<Type> getAncestors(Class<?> clazz) {
        List<Type> res = new ArrayList<Type>();
        res.add(clazz);

        if (clazz.getSuperclass() != null) {
            res.addAll(getAncestors(clazz.getSuperclass()));
        }
        return res;

    }

    private List<URL> getBlueprintResources() {
        return Arrays.asList(url);
    }
}
