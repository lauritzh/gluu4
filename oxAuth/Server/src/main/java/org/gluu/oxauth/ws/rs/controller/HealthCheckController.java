package org.gluu.oxauth.ws.rs.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.gluu.oxauth.service.external.ExternalAuthenticationService;
import org.gluu.oxauth.service.external.ExternalDynamicScopeService;
import org.gluu.persist.PersistenceEntryManager;

/**
 * Health check controller
 * 
 * @author Yuriy Movchan
 * @version Jul 24, 2020
 */
@ApplicationScoped
@Path("/")
public class HealthCheckController {

	@Inject
	private PersistenceEntryManager persistenceEntryManager;

	@Inject
	private ExternalAuthenticationService externalAuthenticationService;

	@Inject
	private ExternalDynamicScopeService externalDynamicScopeService;

    @GET
    @POST
    @Path("/health-check")
    @Produces(MediaType.APPLICATION_JSON)
	public String healthCheckController() {
    	boolean isConnected = persistenceEntryManager.getOperationService().isConnected();
    	String dbStatus = isConnected ? "online" : "offline";
    	String appStatus = getAppStatus();
        return "{\"status\": \"" + appStatus + "\", \"db_status\":\"" + dbStatus + "\"}";
	}

    public String getAppStatus() {
        if (externalAuthenticationService.isLoaded() && externalDynamicScopeService.isLoaded()) {
        	return "running";
        } else {
        	return "starting";
        }
    }
}
