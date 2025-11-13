package org.gluu.service.el;

import jakarta.el.ExpressionFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * @author Yuriy Movchan Date: 05/22/2017
 */
@ApplicationScoped
public class ExpressionFactoryProducer {

    @Produces
    @ApplicationScoped
    public ExpressionFactory createExpressionFactory() {
        return ExpressionFactory.newInstance();
    }

}
