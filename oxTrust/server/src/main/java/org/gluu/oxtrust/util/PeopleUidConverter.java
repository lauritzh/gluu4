/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxtrust.util;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import org.gluu.oxtrust.model.GluuCustomPerson;
import org.gluu.oxtrust.service.IPersonService;

@FacesConverter("PeopleUidConverter")
public class PeopleUidConverter implements Converter {
	
	@Inject
	private IPersonService personService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value == null) {
            return null;
        }
        GluuCustomPerson person = personService.getPersonByUid(value);
        return person;
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        String string = null;
        if (value instanceof GluuCustomPerson) {
            string = ((GluuCustomPerson) value).getUid();
        }
        return string;
    }

}