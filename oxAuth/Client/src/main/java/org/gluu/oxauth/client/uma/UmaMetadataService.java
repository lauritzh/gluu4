/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxauth.client.uma;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;

import org.gluu.oxauth.model.uma.UmaConstants;
import org.gluu.oxauth.model.uma.UmaMetadata;

/**
 * The endpoint at which the requester can obtain UMA metadata.
 *
 * @author Yuriy Zabrovarnyy
 */
public interface UmaMetadataService {

	@GET
	@Produces({ UmaConstants.JSON_MEDIA_TYPE })
	UmaMetadata getMetadata();

}