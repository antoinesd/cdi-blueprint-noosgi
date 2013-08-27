cdi-blueprint-noosgi
====================

A bridge to use blueprint bean as CDI bean.

This CDI extension use a Blueprint container to wrap Blueprint beans as CDI beans. It allows to initialize CDI beans with a
blueprint config file.

Right now it is in early stage and has the following main limitations:

 * Only work on Non OSGi Blueprint version
 * Generated CDI beans cannot be Decorated or intercepted
 * Blueprint and CDI container co-exists during all the application life

Building and testing
-----------

Using the command `mvn clean install` you'll build the jar and launch integration test with Arquillian and embedded Weld
container (Weld 1.x)

You can launch the tests against OpenWebBeans with `mvn clean install -Powb` and Weld 2.x (CDI 1.1) with `mvn clean install
-Pweld-2.x`
