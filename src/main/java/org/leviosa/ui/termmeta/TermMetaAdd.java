/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.termmeta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.leviosa.bl.service.CMSServiceCore;
import org.hedwig.cloud.response.HedwigResponseCode;
import org.hedwig.cloud.response.HedwigResponseMessage;
import org.leviosa.db.entities.Term;
import org.leviosa.db.entities.TermMeta;
import org.leviosa.db.entities.TermMetaPK;

/**
 *
 * @author bhaduri
 */
@Named(value = "termMetaAdd")
@ViewScoped
public class TermMetaAdd implements Serializable {

    private String termSlug;

    private List<SelectItem> inputDataTypes;
    private TermMeta termMetaAdd;
    private boolean disableTermDropDown = true;
    private boolean mandatory;
    private String termName;
    private List<SelectItem> termSlugDropDown;

    /**
     * Creates a new instance of TermMetaAdd
     */
    public TermMetaAdd() {
        termMetaAdd = new TermMeta();
    }

    public void fillInputDataTypes() {

        CMSServiceCore mts = new CMSServiceCore();

        Map<String, String> termMetaDataTypes = mts.getTermMetaDataTypes();
        termName = mts.getTerm(termSlug).getName();
        inputDataTypes = termMetaDataTypes.entrySet().stream().map(dataType -> {
            SelectItem selectItem = new SelectItem(dataType.getKey(), dataType.getValue());
            return selectItem;
        }).collect(Collectors.toList());

        List<Term> termList = mts.getTermList();
        termSlugDropDown = termList.stream().map(term -> {
            SelectItem selectItem = new SelectItem(term.getTermSlug(), term.getName());
            return selectItem;
        }).collect(Collectors.toList());
        TermMetaPK termMetaPK = new TermMetaPK();
        termMetaPK.setTermSlug(termSlug);
        termMetaAdd = new TermMeta(termMetaPK);

    }

    public String add() {
        
        Term term = new Term(termSlug);
        termMetaAdd.setTerm(term);
        if (!termMetaAdd.getDataType().equals("selectone")) {
            if (!termMetaAdd.getDataType().equals("selectmany")) {
                termMetaAdd.setManyToOneTermSlug(null);
            }
        }
        CMSServiceCore mts = new CMSServiceCore();
        int response = mts.insertIntoTermMeta(termMetaAdd);
        FacesMessage message;
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);
        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "TermMetaAdd";
            return redirectUrl;
        } else {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "TermMetaList";
            return redirectUrl;
        }
    }

    public List<SelectItem> getInputDataTypes() {
        return inputDataTypes;
    }

    public void setInputDataTypes(List<SelectItem> inputDataTypes) {
        this.inputDataTypes = inputDataTypes;
    }

    public void makeShowHide() {
        String metaDataType = termMetaAdd.getDataType();
        if (metaDataType.equals("selectone")) {
            disableTermDropDown = false;
        } else {

            if (metaDataType.equals("selectmany")) {
                disableTermDropDown = false;
            } else {
                disableTermDropDown = true;
            }
        }

//        enableTermDropDown = !gardenTermMetaBeanAdd.getDataType().equals("selectone");
//        enableTermDropDown = !gardenTermMetaBeanAdd.getDataType().equals("selectmany");
    }

    public String getTermSlug() {
        return termSlug;
    }

    public void setTermSlug(String termSlug) {
        this.termSlug = termSlug;
    }

    public TermMeta getTermMetaAdd() {
        return termMetaAdd;
    }

    public void setTermMetaAdd(TermMeta termMetaAdd) {
        this.termMetaAdd = termMetaAdd;
    }

    public boolean isDisableTermDropDown() {
        return disableTermDropDown;
    }

    public void setDisableTermDropDown(boolean disableTermDropDown) {
        this.disableTermDropDown = disableTermDropDown;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public List<SelectItem> getTermSlugDropDown() {
        return termSlugDropDown;
    }

    public void setTermSlugDropDown(List<SelectItem> termSlugDropDown) {
        this.termSlugDropDown = termSlugDropDown;
    }

}
