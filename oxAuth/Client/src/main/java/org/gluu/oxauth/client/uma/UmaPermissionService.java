/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxauth.client.uma;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;

import org.gluu.oxauth.model.uma.PermissionTicket;
import org.gluu.oxauth.model.uma.UmaConstants;
import org.gluu.oxauth.model.uma.UmaPermissionList;

/**
 * The endpoint at which the host registers permissions that it anticipates a
 * requester will shortly be asking for from the AM. This AM's endpoint is part
 * of resource registration API.
 * <p/>
 * In response to receiving an access request accompanied by an RPT that is
 * invalid or has insufficient authorization data, the host SHOULD register a
 * permission with the AS that would be sufficient for the type of access
 * sought. The AS returns a permission ticket for the host to give to the
 * requester in its response.
 * 
 */
public interface UmaPermissionService {

	@POST
	@Consumes({ UmaConstants.JSON_MEDIA_TYPE })
	@Produces({ UmaConstants.JSON_MEDIA_TYPE })
	PermissionTicket registerPermission(
			@HeaderParam("Authorization") String authorization,
			UmaPermissionList permissions);
}