/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.termmeta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
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
 * @author dgrf-iv
 */
@Named(value = "termMetaList")
@ViewScoped
public class TermMetaList implements Serializable {

    private String termName;
    private String termSlug;
    private List<Map<String, Object>> termMetaInMapList;

    private Map<String, Object> selectedMeta;

    public TermMetaList() {
    }

    @PostConstruct
    public void init() {

    }

    public void fillTermValues() {

        CMSService mts = new CMSService();
        termName = mts.getTerm(termSlug).getName();
        TermMetaDTO termMetaDTO = new TermMetaDTO();
        termMetaDTO.setTermSlug(termSlug);
        termMetaInMapList = mts.getTermMetaList(termMetaDTO).getTermMetaFields();
        int termSlugIndex;
        for (termSlugIndex = 0; termSlugIndex < termMetaInMapList.size(); termSlugIndex++) {
            Map<String, Object> termMetaInMap = termMetaInMapList.get(termSlugIndex);
            if (termMetaInMap.get("metaKey").toString().equals("termInstanceSlug")) {
                termMetaInMapList.remove(termSlugIndex);
            }
        }

    }


    public String deleteTermMeta() {
        
        CMSService mts = new CMSService();
        int response = mts.deleteTermMetaAndData(termSlug, (String) selectedMeta.get("metaKey"));
        FacesMessage message;
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);
        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
        } else {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            
        }

        
        return "TermMetaList";
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String refreshGrid() {
        String redirectUrl = "/TermMeta/TermMetaList?faces-redirect=true&termslug=" + termSlug;
        return redirectUrl;
    }

    public String getTermSlug() {
        return termSlug;
    }

    public void setTermSlug(String termSlug) {
        this.termSlug = termSlug;
    }

    public List<Map<String, Object>> getTermMetaInMapList() {
        return termMetaInMapList;
    }

    public void setTermMetaInMapList(List<Map<String, Object>> termMetaInMapList) {
        this.termMetaInMapList = termMetaInMapList;
    }

    public Map<String, Object> getSelectedMeta() {
        return selectedMeta;
    }

    public void setSelectedMeta(Map<String, Object> selectedMeta) {
        this.selectedMeta = selectedMeta;
    }

}
