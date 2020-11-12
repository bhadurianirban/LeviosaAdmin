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
 * @author dgrf-iv
 */
@Named(value = "termMetaEdit")
@ViewScoped
public class TermMetaEdit implements Serializable {

    private List<SelectItem> termSlugDropDown;
    private String termSlug;
    private String metaKey;
    private String termName;
    private List<SelectItem> inputDataTypes;
    private TermMeta termMetaEdit;
    private boolean disableTermDropDown = true;

    public TermMetaEdit() {
    }

    public void fillTermMetaValues() {
        CMSServiceCore mts = new CMSServiceCore();

        termMetaEdit = mts.getTermMeta(termSlug, metaKey);

        Map<String, String> termMetaDataTypes = mts.getTermMetaDataTypes();
        inputDataTypes = termMetaDataTypes.entrySet().stream().map(dataType -> {
            SelectItem selectItem = new SelectItem(dataType.getKey(), dataType.getValue());
            return selectItem;
        }).collect(Collectors.toList());
        //inputDataTypes = TermUtil.getTermMetaDataTypes();
        termName = mts.getTerm(termSlug).getName();
        String metaDataType = termMetaEdit.getDataType();
        if (metaDataType.equals("selectone")) {
            disableTermDropDown = false;
        } else {

            if (metaDataType.equals("selectmany")) {
                disableTermDropDown = false;
            } else {
                disableTermDropDown = true;
            }
        }
        List<Term> termList = mts.getTermList();
        termSlugDropDown = termList.stream().map(term -> {
            SelectItem selectItem = new SelectItem(term.getTermSlug(), term.getName());
            return selectItem;
        }).collect(Collectors.toList());

    }

    public List<SelectItem> getTermSlugDropDown() {
        return termSlugDropDown;
    }

    public void setTermSlugDropDown(List<SelectItem> termSlugDropDown) {
        this.termSlugDropDown = termSlugDropDown;
    }

    public void makeShowHide() {
        String metaDataType = termMetaEdit.getDataType();
        if (metaDataType.equals("selectone")) {
            disableTermDropDown = false;
        } else {

            if (metaDataType.equals("selectmany")) {
                disableTermDropDown = false;
            } else {
                disableTermDropDown = true;
            }
        }
        //enableTermDropDown = !gardenTermMetaBeanEdit.getDataType().equals("selectone");
        //enableTermDropDown = !gardenTermMetaBeanEdit.getDataType().equals("selectmany");

    }

    public String saveTermMeta() {
        TermMetaPK termMetaPK = new TermMetaPK();
        termMetaPK.setTermSlug(termSlug);
        termMetaPK.setMetaKey(metaKey);
        termMetaEdit.setTermMetaPK(termMetaPK);
        if (!termMetaEdit.getDataType().equals("selectone")) {
            if (!termMetaEdit.getDataType().equals("selectmany")) {
                termMetaEdit.setManyToOneTermSlug(null);
            }
        }
        CMSServiceCore mts = new CMSServiceCore();
        int response = mts.editTermMeta(termMetaEdit);

        FacesMessage message;
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);
        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "TermMetaEdit";
            return redirectUrl;
        } else {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "TermMetaList";
            return redirectUrl;
        }
    }

    public String getTermSlug() {
        return termSlug;
    }

    public void setTermSlug(String termSlug) {
        this.termSlug = termSlug;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public TermMeta getTermMetaEdit() {
        return termMetaEdit;
    }

    public void setTermMetaEdit(TermMeta termMetaEdit) {
        this.termMetaEdit = termMetaEdit;
    }

    public boolean isDisableTermDropDown() {
        return disableTermDropDown;
    }

    public void setDisableTermDropDown(boolean disableTermDropDown) {
        this.disableTermDropDown = disableTermDropDown;
    }

    public List<SelectItem> getInputDataTypes() {
        return inputDataTypes;
    }

    public void setInputDataTypes(List<SelectItem> inputDataTypes) {
        this.inputDataTypes = inputDataTypes;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

}
