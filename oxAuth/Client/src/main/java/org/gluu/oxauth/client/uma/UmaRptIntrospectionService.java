/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxauth.client.uma;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;

import org.gluu.oxauth.model.uma.RptIntrospectionResponse;
import org.gluu.oxauth.model.uma.UmaConstants;

/**
 * The endpoint at which the host requests the status of an RPT presented to it by a requester.
 * The endpoint is RPT introspection profile implementation defined here:
 * http://docs.kantarainitiative.org/uma/draft-uma-core.html#uma-bearer-token-profile
 */
public interface UmaRptIntrospectionService {

    @POST
    @Produces({UmaConstants.JSON_MEDIA_TYPE})
    RptIntrospectionResponse requestRptStatus(@HeaderParam("Authorization") String authorization,
                                              @FormParam("token") String rptAsString,
                                              @FormParam("token_type_hint") String tokenTypeHint);

}
