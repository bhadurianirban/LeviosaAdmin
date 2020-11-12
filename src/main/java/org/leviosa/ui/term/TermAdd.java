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
import javax.annotation.PostConstruct;
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
import org.leviosa.db.entities.TermMeta;
import org.leviosa.db.entities.TermRole;
import org.leviosa.ui.login.CMSClientAuthCredentialValue;

/**
 *
 * @author bhaduri
 */
@Named(value = "termAdd")
@ViewScoped
public class TermAdd implements Serializable {

    //private List<SelectItem> termSlugDropDown;
    private Term termAdd;
    private List<SelectItem> roleList;
    private String[] selectedRoles;

    /**
     * Creates a new instance of TermAdd
     */
    public TermAdd() {
        termAdd = new Term();
    }

    @PostConstruct
    public void init() {
        termAdd.setScreen("/TermInstance/TermInstanceList");
    }

    public void fillTermValues() {
        CMSServiceCore mts = new CMSServiceCore();
        RoleListClient dgrfrlc = new RoleListClient();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setCloudAuthCredentials(CMSClientAuthCredentialValue.AUTH_CREDENTIALS);
        roleDTO = dgrfrlc.getRoleList(roleDTO);
        Map<String, String> roleMap = roleDTO.getRoleMap();
        

        roleList = roleMap.entrySet().stream().map(selectTerm -> {
            SelectItem selectItem = new SelectItem(selectTerm.getKey(), selectTerm.getValue());
            return selectItem;
        }).collect(Collectors.toList());

        //termSlugDropDown = TermUtil.fillTermSlugList(termSlug);
    }

    public String add() {
        
        CMSServiceCore mts = new CMSServiceCore();
        List<TermRole> termRoleList = new ArrayList<>();
        for (String role : selectedRoles) {
            TermRole termRole = new TermRole(Integer.parseInt(role), termAdd.getTermSlug());
            termRoleList.add(termRole);
        }
        termAdd.setTermRoleList(termRoleList);
        List<TermMeta> termMetaList = new ArrayList<>();
        termAdd.setTermMetaList(termMetaList);
        int response = mts.insertIntoTerm(termAdd);
        FacesMessage message;
        String redirectUrl;
        if (response != HedwigResponseCode.SUCCESS) {
            redirectUrl = "TermAdd";
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
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

    public Term getTermAdd() {
        return termAdd;
    }

    public void setTermAdd(Term termAdd) {
        this.termAdd = termAdd;
    }

    public List<SelectItem> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SelectItem> roleList) {
        this.roleList = roleList;
    }

    public String[] getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(String[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

}
