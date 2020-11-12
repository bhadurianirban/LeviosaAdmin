/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.term;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


import org.leviosa.bl.service.CMSServiceCore;
import org.hedwig.cloud.response.HedwigResponseCode;
import org.hedwig.cloud.response.HedwigResponseMessage;
import org.hedwig.cms.constants.CMSConstants;

/**
 *
 * @author dgrf-iv
 */
@Named(value = "termList")
@ViewScoped
public class TermList implements Serializable {

    
    private List<Map<String,Object>> termListInMap;
    private Map<String,String> selectedTerm;

    public TermList() {
    }


    public void fillTermList() {
        CMSServiceCore mts = new CMSServiceCore();
        termListInMap = mts.getTermListInMap();
    }


    public String deleteTerm() {
        FacesMessage message;
        CMSServiceCore mts = new CMSServiceCore();
        int response = mts.deleteTerm(selectedTerm.get(CMSConstants.TERM_SLUG));
        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error", responseMessage.getResponseMessage(response));
            FacesContext f = FacesContext.getCurrentInstance();
            f.getExternalContext().getFlash().setKeepMessages(true);
            f.addMessage(null, message);
        } else {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO,  "Success", responseMessage.getResponseMessage(response));
            FacesContext f = FacesContext.getCurrentInstance();
            f.getExternalContext().getFlash().setKeepMessages(true);
            f.addMessage(null, message);
        }

        return "TermList";
    }

    public List<Map<String, Object>> getTermListInMap() {
        return termListInMap;
    }

    public void setTermListInMap(List<Map<String, Object>> termListInMap) {
        this.termListInMap = termListInMap;
    }

    public Map<String, String> getSelectedTerm() {
        return selectedTerm;
    }

    public void setSelectedTerm(Map<String, String> selectedTerm) {
        this.selectedTerm = selectedTerm;
    }



}
