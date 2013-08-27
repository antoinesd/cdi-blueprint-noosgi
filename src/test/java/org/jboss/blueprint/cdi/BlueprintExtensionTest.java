package org.jboss.blueprint.cdi;

import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.blueprint.cdi.extension.BeanId;
import org.jboss.blueprint.cdi.extension.BeanIdLitteral;
import org.jboss.blueprint.cdi.extension.BlueprintContextualLifecycle;
import org.jboss.blueprint.cdi.extension.BlueprintExtension;
import org.jboss.blueprint.sample.*;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.FileNotFoundException;

/**
 * @author Antoine Sabot-Durand
 */

@RunWith(Arquillian.class)
public class BlueprintExtensionTest {

    @Inject
    @BeanId("foo")
    Foo foo;

    @Inject
    Baz baz;

    @Deployment
    public static Archive<?> createTestArchive() throws FileNotFoundException {

        JavaArchive[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.apache.deltaspike.core:deltaspike-core-impl"
                        , "org.apache.aries.blueprint:org.apache.aries.blueprint.noosgi"
                        , "com.google.guava:guava")

                .withTransitivity().as(JavaArchive.class);


        WebArchive ret = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClasses(BlueprintExtension.class,
                        BeanId.class,
                        BeanIdLitteral.class,
                        BlueprintContextualLifecycle.class,
                        Bar.class,
                        CurrencyTypeConverter.class,
                        DateTypeConverter.class,
                        FooImpl.class,
                        Foo.class,
                        MyFooDecorator.class,
                        Baz.class,
                        BazImpl.class,
                        MyBazDecorator.class)
                .addAsLibraries(libs)
                .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension")
                .addAsResource("test.xml")
                .addAsResource("test.properties")
                .addAsWebInfResource("testbeans.xml", "beans.xml");

        return ret;
    }

    @Test
    public void alwaysWork() {
        Assert.assertTrue(true);
    }

    @Test
    public void fooShouldHaveTheRightAValue() {
        Assert.assertTrue(foo.getA() == 5);
    }

/*    @Test
    public void fooShouldBeDecorated() {
        Assert.assertTrue(foo.getB() == 10);
    }

    @Test
    public void bazShouldHaveAValue() {
        Assert.assertTrue(baz.getValue() == 20);
    }*/

}
