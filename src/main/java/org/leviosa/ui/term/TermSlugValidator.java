/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.term;

import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.leviosa.bl.service.CMSServiceCore;
import org.primefaces.validate.ClientValidator;

/**
 *
 * @author bhaduri
 */
@FacesValidator("custom.termSlugValidator")
public class TermSlugValidator implements Validator, ClientValidator {
    private static final String SLUG_PATTERN = "[a-zA-Z]+";
    private Pattern pattern;

    public TermSlugValidator() {
        pattern = Pattern.compile(SLUG_PATTERN);
    }
     
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if (o == null) {
            return;
        }
        if(!pattern.matcher(o.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data entry error","slug should not contan spaces;"));
        }
        CMSServiceCore mts = new CMSServiceCore();
        String termSlug = (String)o;
        //boolean slugExists = mts.isTermExists(termSlug);
        if (mts.getTerm(termSlug)!=null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate Slug",termSlug + " is already present;"));
        }
        
        
    }

    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }

    @Override
    public String getValidatorId() {
        return "custom.termSlugValidator";
    }
    
}
