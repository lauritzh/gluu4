package org.gluu.service.exception;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class ExceptionHandlerProducer {
    @Inject
    private ExceptionHandlerExtension exceptionHandlerExtension;

    @Produces
    @ApplicationScoped
    protected ExceptionHandlerMethods createExceptionHandlerProducer() {
        return new ExceptionHandlerMethods(exceptionHandlerExtension.getAllExceptionHandlers());
    }
}
