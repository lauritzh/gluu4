package org.gluu.oxauth.revoke;

import org.apache.commons.lang.ArrayUtils;
import org.gluu.oxauth.model.common.User;
import org.gluu.oxauth.model.config.Constants;
import org.gluu.oxauth.model.error.ErrorResponseFactory;
import org.gluu.oxauth.model.session.EndSessionErrorResponseType;
import org.gluu.oxauth.model.session.SessionClient;
import org.gluu.oxauth.model.session.SessionId;
import org.gluu.oxauth.model.session.SessionIdState;
import org.gluu.oxauth.security.Identity;
import org.gluu.oxauth.service.ScopeService;
import org.gluu.oxauth.service.SessionIdService;
import org.gluu.oxauth.service.UserService;
import org.slf4j.Logger;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Yuriy Zabrovarnyy
 */
@Path("/")
public class RevokeSessionRestWebService {

    @Inject
    private Logger log;

    @Inject
    private UserService userService;

    @Inject
    private SessionIdService sessionIdService;

    @Inject
    private ErrorResponseFactory errorResponseFactory;

    @Inject
    private Identity identity;

    @Inject
    private ScopeService scopeService;

    @POST
    @Path("/revoke_session")
    @Produces({MediaType.APPLICATION_JSON})
    public Response requestRevokeSession(
            @FormParam("user_criterion_key") String userCriterionKey,
            @FormParam("user_criterion_value") String userCriterionValue,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response,
            @Context SecurityContext sec) {
        try {
            log.debug("Attempting to revoke session: userCriterionKey = {}, userCriterionValue = {}, isSecure = {}",
                    userCriterionKey, userCriterionValue, sec.isSecure());

            validateAccess();

            final User user = userService.getUserByAttribute(userCriterionKey, userCriterionValue);
            if (user == null) {
                log.trace("Unable to find user by {}={}", userCriterionKey, userCriterionValue);
                return Response.ok().build(); // no error because we don't want to disclose internal AS info about users
            }

            List<SessionId> sessionIdList = sessionIdService.findByUser(user.getDn());
            if (sessionIdList == null || sessionIdList.isEmpty()) {
                log.trace("No sessions found for user uid: {}, dn: {}", user.getUserId(), user.getDn());
                return Response.ok().build();
            }

            final List<SessionId> authenticatedSessions = sessionIdList.stream().filter(sessionId -> sessionId.getState() == SessionIdState.AUTHENTICATED).collect(Collectors.toList());
            sessionIdService.remove(authenticatedSessions);
            log.debug("Revoked {} user's sessions (user: {})", authenticatedSessions.size(), user.getUserId());

            return Response.ok().build();
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(500).build();
        }
    }

    private void validateAccess() {
        SessionClient sessionClient = identity.getSessionClient();
        if (sessionClient == null || sessionClient.getClient() == null || ArrayUtils.isEmpty(sessionClient.getClient().getScopes())) {
            log.debug("Client failed to authenticate.");
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED.getStatusCode())
                            .entity(errorResponseFactory.getErrorAsJson(EndSessionErrorResponseType.INVALID_REQUEST))
                            .build());
        }

        List<String> scopesAllowedIds = scopeService.getScopeIdsByDns(Arrays.asList(sessionClient.getClient().getScopes()));

        if (!scopesAllowedIds.contains(Constants.REVOKE_SESSION_SCOPE)) {
            log.debug("Client does not have required revoke_session scope.");
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED.getStatusCode())
                    .entity(errorResponseFactory.getErrorAsJson(EndSessionErrorResponseType.INVALID_REQUEST))
                    .build());
        }
    }
}
