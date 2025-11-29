package org.gluu.oxtrust.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import org.gluu.oxtrust.service.PersonService;
import org.gluu.persist.PersistenceEntryManager;
import org.slf4j.Logger;

@FacesConverter("org.gluu.jsf2.converter.UserDateTimeConverter")
public class UserDateTimeConverter implements Converter {

	@Inject
	PersistenceEntryManager manager;

	@Inject
	private PersonService personService;

	@Inject
	private Logger log;

	private String baseDn;
	
	@PostConstruct
	public void create() {
		this.baseDn = personService.getDnForPerson(null);
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String value) {
		try {
			Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(value);
			return manager.encodeTime(baseDn, date);
		} catch (ParseException e) {
			log.info("", e);
			return manager.encodeTime(baseDn, new Date());
		}
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
		Date result = manager.decodeTime(baseDn, (String) value);
		return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(result);
	}
}