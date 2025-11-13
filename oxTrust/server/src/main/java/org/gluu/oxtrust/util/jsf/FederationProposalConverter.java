/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxtrust.util.jsf;

import java.util.List;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import org.gluu.oxtrust.model.GluuSAMLFederationProposal;
import org.gluu.oxtrust.service.FederationService;

@FacesConverter("federationProposalConverter")
public class FederationProposalConverter implements Converter {
	
	@Inject
	private FederationService federationService;

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String federationName) {
		List<GluuSAMLFederationProposal> federations = federationService.getAllFederations();
		for (GluuSAMLFederationProposal federation : federations) {
			if (federation.getDisplayName().equals(federationName)) {
				return federation;
			}
		}
		return null;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object federation) {
		if (federation == null) {
			return null;
		}

		return ((GluuSAMLFederationProposal) federation).getDisplayName();
	}

}
