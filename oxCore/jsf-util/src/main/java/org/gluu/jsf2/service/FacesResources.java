package org.gluu.jsf2.service;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.ViewHandler;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

/**
 * @author Yuriy Movchan
 * @version 03/17/2017
 */
@Dependent
public class FacesResources {

    @Inject
    private FacesContext facesContext;

    @Produces
    @Dependent
    public ViewHandler getViewHandler() {
        if (facesContext != null) {
            return facesContext.getApplication().getViewHandler();
        }

        return null;
    }

}
