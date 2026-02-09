package org.gluu.oxauth.model.registration;

import com.beust.jcommander.internal.Lists;
import org.gluu.oxauth.model.common.GrantType;
import org.gluu.oxauth.model.common.ResponseType;
import org.gluu.oxauth.model.common.SubjectType;
import org.gluu.oxauth.model.configuration.AppConfiguration;
import org.gluu.oxauth.model.error.ErrorResponseFactory;
import org.gluu.oxauth.model.register.ApplicationType;
import org.gluu.oxauth.model.register.RegisterErrorResponseType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.slf4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Listeners(MockitoTestNGListener.class)
public class RegisterParamsValidatorTest {

    @InjectMocks
    private RegisterParamsValidator registerParamsValidator;

    @Mock
    private Logger log;

    @Mock
    private AppConfiguration appConfiguration;

    @Mock
    private ErrorResponseFactory errorResponseFactory;

    @Test
    public void validateRedirectUris_whenSectorIdentifierDoesNotHostValidRedirectUri_shouldThrowInvalidClientMetadataError() {
        try {
            allowAllRedirectUris();
            when(errorResponseFactory.createWebApplicationException(any(), any(), any())).thenCallRealMethod();
            registerParamsValidator.validateRedirectUris(
                    Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                    Lists.newArrayList(ResponseType.CODE),
                    ApplicationType.WEB,
                    SubjectType.PAIRWISE,
                    Lists.newArrayList("https://someuri.com"),
                    "https://invaliduri.com");
        } catch (WebApplicationException e) {
            verify(errorResponseFactory, times(1)).createWebApplicationException(eq(Response.Status.BAD_REQUEST), eq(RegisterErrorResponseType.INVALID_CLIENT_METADATA), any());
        }
    }

    @Test
    public void validateRedirectUris_whenJavascriptSchemeForWeb_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.WEB,
                SubjectType.PUBLIC,
                Lists.newArrayList("javascript://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenJavascriptSchemeForNative_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.NATIVE,
                SubjectType.PUBLIC,
                Lists.newArrayList("javascript://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenDataSchemeForWeb_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.WEB,
                SubjectType.PUBLIC,
                Lists.newArrayList("data://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenDataSchemeForNative_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.NATIVE,
                SubjectType.PUBLIC,
                Lists.newArrayList("data://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenVbscriptSchemeForWeb_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.WEB,
                SubjectType.PUBLIC,
                Lists.newArrayList("vbscript://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenVbscriptSchemeForNative_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.NATIVE,
                SubjectType.PUBLIC,
                Lists.newArrayList("vbscript://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenMixedCaseJavascriptSchemeForWeb_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.WEB,
                SubjectType.PUBLIC,
                Lists.newArrayList("JavaScript://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenMixedCaseJavascriptSchemeForNative_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.NATIVE,
                SubjectType.PUBLIC,
                Lists.newArrayList("JavaScript://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenMixedCaseDataSchemeForWeb_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.WEB,
                SubjectType.PUBLIC,
                Lists.newArrayList("DATA://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenMixedCaseDataSchemeForNative_shouldReturnFalse() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.NATIVE,
                SubjectType.PUBLIC,
                Lists.newArrayList("DATA://lhq.at/%0aconfirm(location)"),
                null
        );

        assertFalse(result);
    }

    @Test
    public void validateRedirectUris_whenHttpsForWeb_shouldReturnTrue() {
        allowAllRedirectUris();

        boolean result = registerParamsValidator.validateRedirectUris(
                Lists.newArrayList(GrantType.AUTHORIZATION_CODE),
                Lists.newArrayList(ResponseType.CODE),
                ApplicationType.WEB,
                SubjectType.PUBLIC,
                Lists.newArrayList("https://client.example.com/callback"),
                null
        );

        assertTrue(result);
    }

    private void allowAllRedirectUris() {
        when(appConfiguration.getAllowWildcardRedirectUri()).thenReturn(false);
        when(appConfiguration.getClientWhiteList()).thenReturn(Lists.newArrayList("*"));
        when(appConfiguration.getClientBlackList()).thenReturn(Lists.newArrayList());
    }
}
