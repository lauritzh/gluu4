package org.gluu.oxauth.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.gluu.oxauth.model.common.WebKeyStorage;
import org.gluu.oxauth.model.configuration.AppConfiguration;
import org.gluu.oxauth.model.crypto.AbstractCryptoProvider;
import org.slf4j.Logger;

/**
 * Crypto Provider
 * 
 * @author Yuriy Movchan
 * @version 11/02/2018
 */
@ApplicationScoped
@Named
public class CryptoProviderProviderFactory {

    @Inject
    private Logger log;

    @Inject
    private AppConfiguration appConfiguration;

    @Produces
    @ApplicationScoped
    public AbstractCryptoProvider getCryptoProvider() throws Exception {
        log.debug("Started to create crypto provider");

        WebKeyStorage webKeyStorage = appConfiguration.getWebKeysStorage();
        if (webKeyStorage == null) {
            throw new RuntimeException("Failed to initialize cryptoProvider, cryptoProviderType is not specified!");
        }

        AbstractCryptoProvider cryptoProvider = org.gluu.oxauth.model.crypto.CryptoProviderFactory.getCryptoProvider(appConfiguration);

        if (cryptoProvider == null) {
            throw new RuntimeException("Failed to initialize cryptoProvider, cryptoProviderType is unsupported: " + webKeyStorage);
        }

        return cryptoProvider;
    }

}
