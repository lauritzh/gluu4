/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2016, Gluu
 */

package org.gluu.service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gluu.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;
import jakarta.faces.application.ViewResource;
import jakarta.faces.context.FacesContext;

/**
 * External resource handler to customize applicaton
 *
 * @author Yuriy Movchan Date: 04/05/2016
 */
public class ExternalResourceHandlerWrapper extends ResourceHandlerWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalResourceHandlerWrapper.class);

    private File externalResourceBaseFolder;
    private boolean useExternalResourceBase;

    public ExternalResourceHandlerWrapper(ResourceHandler wrapped) {
    	super(wrapped);

        String externalResourceBase = System.getProperty("server.base");
        if (StringHelper.isNotEmpty(externalResourceBase)) {
            externalResourceBase += "/custom/pages";
            File folder = new File(externalResourceBase);
            if (folder.exists() && folder.isDirectory()) {
                this.externalResourceBaseFolder = folder;
                this.useExternalResourceBase = true;
            } else {
                LOG.error("Specified path '" + externalResourceBase + "' in 'server.base' not exists or not a folder!");
            }
        }
    }

	@Override
	public ViewResource createViewResource(FacesContext context, String resourceName) {
        if (!useExternalResourceBase) {
        	return super.createViewResource(context, resourceName);
        }

        // First try external resource folder
        final File externalResource = new File(this.externalResourceBaseFolder, resourceName);
        if (externalResource.exists()) {
            LOG.debug("Found overriden resource: " + resourceName);
            String resource = externalResource.toURI().getPath();

            return super.createViewResource(context, resource);
        }

        // Return default resource
        return super.createViewResource(context, resourceName);
	}

}
