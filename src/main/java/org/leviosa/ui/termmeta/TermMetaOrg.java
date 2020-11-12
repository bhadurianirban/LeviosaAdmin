/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.termmeta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

import org.leviosa.bl.service.CMSService;
import org.hedwig.cloud.response.HedwigResponseCode;
import org.hedwig.cloud.response.HedwigResponseMessage;
import org.hedwig.cms.dto.TermMetaDTO;

/**
 *
 * @author bhaduri
 */
@Named(value = "termMetaOrg")
@ViewScoped
public class TermMetaOrg implements Serializable {

    private String termSlug;
    private String termName;
    private List<Map<String, Object>> termScreenFields;

    /**
     * Creates a new instance of TermMetaOrg
     */
    public TermMetaOrg() {
    }

    public void fillTermMetaOrglist() {

        CMSService mts = new CMSService();
        termName = mts.getTerm(termSlug).getName();
        TermMetaDTO termMetaDTO = new TermMetaDTO();
        termMetaDTO.setTermSlug(termSlug);
        termScreenFields = mts.getTermMetaList(termMetaDTO).getTermMetaFields();

        int termSlugIndex;
        for (termSlugIndex = 0; termSlugIndex < termScreenFields.size(); termSlugIndex++) {
            Map<String, Object> termMetaInMap = termScreenFields.get(termSlugIndex);
            if (termMetaInMap.get("metaKey").toString().equals("termInstanceSlug")) {
                termScreenFields.remove(termSlugIndex);
            }
        }
        //termMetaList = mts.getOrderedTermMetaList(termSlug);
    }

    public String chageOrder() {

        CMSService mts = new CMSService();
        int response = mts.reorderTermMeta(termScreenFields);
        FacesMessage message;
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);
        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "TermMetaOrg";
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

    public List<Map<String, Object>> getTermScreenFields() {
        return termScreenFields;
    }

    public void setTermScreenFields(List<Map<String, Object>> termScreenFields) {
        this.termScreenFields = termScreenFields;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

}
