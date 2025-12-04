/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxtrust.util.jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("profileConfigurationConverter")
public class ProfileConfigurationConverter implements Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String profileConfigurationName) {
		return profileConfigurationName;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object profileConfiguration) {
		return (String) profileConfiguration;
	}

}
