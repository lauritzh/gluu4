package org.gluu.oxtrust.security;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Named;
import jakarta.interceptor.Interceptor;

import org.gluu.oxtrust.model.GluuCustomPerson;

@Alternative
@Priority(Interceptor.Priority.APPLICATION + 20)
@SessionScoped
@Named
public class Identity extends org.gluu.model.security.Identity {

    private static final long serialVersionUID = 2751659008033189259L;

    private OauthData oauthData;
    private GluuCustomPerson user;
    private Map<String, Object> sessionMap;

    private String savedRequestUri;

    @PostConstruct
    public void create() {
        super.create();
        reset();
    }

    @Override
    public void logout() {
        reset();
        super.logout();
    }

    public OauthData getOauthData() {
        return oauthData;
    }

    public void setOauthData(OauthData oauthData) {
        this.oauthData = oauthData;
    }

    public GluuCustomPerson getUser() {
        return user;
    }

    public void setUser(GluuCustomPerson user) {
        this.user = user;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public String getSavedRequestUri() {
        return savedRequestUri;
    }

    public void setSavedRequestUri(String savedRequestUri) {
        this.savedRequestUri = savedRequestUri;
    }

    private void reset() {
        this.sessionMap = new HashMap<String, Object>();
        this.oauthData = new OauthData();
        this.savedRequestUri = null;
    }

}
