/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.term;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

import org.leviosa.bl.service.CMSServiceCore;
import org.hedwig.cloud.client.RoleListClient;
import org.hedwig.cloud.dto.RoleDTO;
import org.hedwig.cloud.response.HedwigResponseCode;
import org.hedwig.cloud.response.HedwigResponseMessage;
import org.leviosa.db.entities.Term;
import org.leviosa.db.entities.TermRole;
import org.leviosa.ui.login.CMSClientAuthCredentialValue;

/**
 *
 * @author bhaduri
 */
@Named(value = "termEdit")
@ViewScoped
public class TermEdit implements Serializable {

    //private List<SelectItem> termSlugDropDown;
    private Term termEdit;
    private List<SelectItem> roleList;
    private String termSlug;
    private String[] selectedRoles;
    /**
     * Creates a new instance of TermEdit
     */
    public TermEdit() {
    }

    public Term getTermEdit() {
        return termEdit;
    }

    public void setTermEdit(Term termEdit) {
        this.termEdit = termEdit;
    }

    public void fillTermValues() {
        CMSServiceCore mts = new CMSServiceCore();

        termEdit = mts.getTerm(termSlug);
        RoleListClient dgrfrlc = new RoleListClient();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setCloudAuthCredentials(CMSClientAuthCredentialValue.AUTH_CREDENTIALS);
        roleDTO = dgrfrlc.getRoleList(roleDTO);
        Map<String, String> roleMap = roleDTO.getRoleMap();
        

        roleList = roleMap.entrySet().stream().map(selectTerm -> {
            SelectItem selectItem = new SelectItem(selectTerm.getKey(), selectTerm.getValue());
            return selectItem;
        }).collect(Collectors.toList());
        
        List <TermRole> termRoleList = termEdit.getTermRoleList();
        
        selectedRoles = new String[termRoleList.size()];
        for (int i=0;i<termRoleList.size();i++) {
            selectedRoles[i] = Integer.toString(termRoleList.get(i).getTermRolePK().getRoleId());
        }
    }

    public String saveTerm() {

        CMSServiceCore mts = new CMSServiceCore();
        List<TermRole> termRoleList = new ArrayList<>();
        for (String role : selectedRoles) {
            TermRole termRole = new TermRole(Integer.parseInt(role), termEdit.getTermSlug());
            termRoleList.add(termRole);
        }
        termEdit.setTermRoleList(termRoleList);        
        FacesMessage message;
        String redirectUrl;
        int response = mts.editTerm(termEdit);
        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
            redirectUrl = "TermEdit";
        } else {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", responseMessage.getResponseMessage(response));
            redirectUrl = "TermList";
        }
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);
        f.addMessage(null, message);
        return redirectUrl;
    }

    public String getTermSlug() {
        return termSlug;
    }

    public void setTermSlug(String termSlug) {
        this.termSlug = termSlug;
    }

    public String[] getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(String[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public List<SelectItem> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SelectItem> roleList) {
        this.roleList = roleList;
    }

}
