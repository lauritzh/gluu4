/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2016, Gluu
 */

package org.gluu.service;

import java.io.File;

import org.gluu.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;
import jakarta.faces.application.ViewResource;
import jakarta.faces.context.FacesContext;

/**
 * External resource handler to customize applicaton
 *
 * @author Yuriy Movchan Date: 04/05/2016
 */
public class ExternalResourceHandler extends ResourceHandlerWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalResourceHandler.class);

    private File externalResourceBaseFolder;
    private boolean useExternalResourceBase;

    public ExternalResourceHandler(ResourceHandler wrapped) {
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
		System.out.println("ExternalResourceHandlerWrapper.createViewResource: " + resourceName);

        if (!useExternalResourceBase) {
        	return super.createViewResource(context, resourceName);
        }

        // First try external resource folder
        final File externalResource = new File(this.externalResourceBaseFolder, resourceName);
        if (externalResource.exists()) {
            LOG.debug("Found overriden resource: " + resourceName);
            String resource = externalResource.toURI().getPath();
            System.out.println("ExternalResourceHandlerWrapper.createViewResource: returning external resource: " + resource);

            return super.createViewResource(context, resourceName);
        }

        // Return default resource
        return super.createViewResource(context, resourceName);
	}

	@Override
	public Resource createResource(String resourceName) {
		System.out.println("ExternalResourceHandlerWrapper.createResource: " + resourceName);
		return super.createResource(resourceName);
	}

	@Override
	public Resource createResourceFromId(String resourceId) {
		System.out.println("ExternalResourceHandlerWrapper.createResourceFromId: " + resourceId);
		return super.createResourceFromId(resourceId);
	}

	@Override
	public Resource createResource(String resourceName, String libraryName) {
		System.out.println("ExternalResourceHandlerWrapper.createResource: " + resourceName + ", " + libraryName);
		return super.createResource(resourceName, libraryName);
	}

	@Override
	public Resource createResource(String resourceName, String libraryName, String contentType) {
		return super.createResource(resourceName, libraryName, contentType);
	}

}
