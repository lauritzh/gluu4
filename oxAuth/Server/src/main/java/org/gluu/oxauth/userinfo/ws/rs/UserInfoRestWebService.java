/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxauth.userinfo.ws.rs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * Provides interface for User Info REST web services
 *
 * @author Javier Rojas Blum
 * @version September 7, 2017
 */
public interface UserInfoRestWebService {

    @GET
    @Path("/userinfo")
    @Produces({MediaType.APPLICATION_JSON})
    Response requestUserInfoGet(
            @QueryParam("access_token")String accessToken,
            @HeaderParam("Authorization") String authorization,
            @Context HttpServletRequest request,
            @Context SecurityContext securityContext);

    @POST
    @Path("/userinfo")
    @Produces({MediaType.APPLICATION_JSON})
    Response requestUserInfoPost(
            @FormParam("access_token") String accessToken,
            @HeaderParam("Authorization") String authorization,
            @Context HttpServletRequest request,
            @Context SecurityContext securityContext);
}