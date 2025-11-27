package org.gluu.oxtrust.util.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator(value = "gluuPasswordConfirmationValidator", managed = true)
public class PasswordConfirmationValidator  implements Validator<String> {

	@Override
	public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        // Get the password value from the component
        String confirm = value;

        // Get the confirmation password component and its submitted value
        String confirmId = (String) component.getAttributes().get("confirmId");
        UIInput passowrdComponent = (UIInput) context.getViewRoot().findComponent(confirmId);
        String password = (String) passowrdComponent.getValue();

        // Check if both fields are filled (let required="true" handle empty fields)
        if (confirm == null || confirm.isEmpty() || password == null || password.isEmpty()) {
            return;
        }

        // Compare the password with the confirm password
        if (!confirm.equals(password)) {
        	((UIInput) component).setValid(false); // Mark the confirm field as invalid
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Passwords do not match.", "Passwords do not match."));
        }
	}

}
