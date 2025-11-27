package org.gluu.oxtrust.auth.none;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

import org.gluu.oxtrust.auth.IProtectionService;

import org.slf4j.Logger;

@ApplicationScoped
public class NoProtectionService implements IProtectionService {
    
    @Inject
    private Logger log;
        
    @Override
    public Response processAuthorization(HttpHeaders headers, ResourceInfo resourceInfo) {
        log.warn("Allowing access to endpoint WITHOUT SECURITY checks in place. " +
                "Ensure this is intedended behavior!");
        return null;
    }
    
}
