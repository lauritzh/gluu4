package org.gluu.oxtrust.exception;

import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;

/**
 * Created by eugeniuparvan on 5/25/17.
 */
public class GlobalExceptionHandlerFactory extends ExceptionHandlerFactory {
    private ExceptionHandlerFactory exceptionHandlerFactory;

    public GlobalExceptionHandlerFactory() {
    }

    public GlobalExceptionHandlerFactory(ExceptionHandlerFactory exceptionHandlerFactory) {
        this.exceptionHandlerFactory = exceptionHandlerFactory;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new GlobalExceptionHandler(exceptionHandlerFactory.getExceptionHandler());
    }
}
