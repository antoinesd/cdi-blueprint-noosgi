package org.jboss.blueprint.cdi.extension;

import javax.enterprise.util.AnnotationLiteral;

/**
 * @author Antoine Sabot-Durand
 */
public class BeanIdLitteral extends AnnotationLiteral<BeanId> implements BeanId {
    private String value;

    public BeanIdLitteral(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }


}
