package org.gluu.service;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.gluu.persist.PersistenceEntryManager;
import org.gluu.persist.cloud.spanner.impl.SpannerEntryManagerFactory;
import org.gluu.persist.ldap.impl.LdapEntryManagerFactory;

@ApplicationScoped
public class DataSourceTypeService implements Serializable {

    private static final long serialVersionUID = -1941135478226842653L;

    @Inject
    private PersistenceEntryManager entryManager;

    public boolean isSpanner(String key) {
        return entryManager.getPersistenceType(key).equals(SpannerEntryManagerFactory.PERSISTENCE_TYPE);
    }

    public boolean isLDAP(String key) {
        return entryManager.getPersistenceType(key).equals(LdapEntryManagerFactory.PERSISTENCE_TYPE);
    }

}
