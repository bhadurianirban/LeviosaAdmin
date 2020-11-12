/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.leviosa.bl.service.CMSServiceCore;


import org.leviosa.bl.service.MenuService;
import org.hedwig.cloud.response.HedwigResponseCode;
import org.hedwig.cloud.response.HedwigResponseMessage;
import org.leviosa.db.entities.Menu;
import org.leviosa.db.entities.Term;

/**
 *
 * @author bhaduri
 */
@Named(value = "menuAdd")
@ViewScoped
public class MenuAdd implements Serializable{
    private List<SelectItem> termDropDown;
    private List<SelectItem> parentMenuDropDown;
    private Menu menu;
    /**
     * Creates a new instance of MenuAdd
     */
    public MenuAdd() {
    }
    public void fillAddScreen() {
        CMSServiceCore mts = new CMSServiceCore();
        List<Term> termList = mts.getTermList();
        termDropDown = termList.stream().map(term-> {
            SelectItem selectItem = new SelectItem(term.getTermSlug(), term.getName());
            return selectItem;
        }).collect(Collectors.toList());
        MenuService ms = new MenuService();
        List<Menu> menuList = ms.getMenuList();
        parentMenuDropDown = new ArrayList<>();
        menuList.forEach((mb) -> {
            parentMenuDropDown.add(new SelectItem(mb.getId(), mb.getName()));
        });
        Integer maxMenuId;
        if (menuList.isEmpty()) {
            maxMenuId = 0;
        } else {
            maxMenuId = menuList.stream().max(Comparator.comparing(m -> m.getId())).get().getId();
            
        }
        
        //Integer maxMenuId = TermUtil.getMaxMenuId();
        menu = new Menu();
        menu.setId(maxMenuId+1);
    }
    public String add() {
        MenuService ms = new MenuService();

        int response = ms.insertMenu(menu);
        FacesMessage message;
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);
        
        
        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "MenuAdd";
            return redirectUrl;
        } else {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "MenuList";
            return redirectUrl;
        }
    }

    public List<SelectItem> getTermDropDown() {
        return termDropDown;
    }

    public void setTermDropDown(List<SelectItem> termDropDown) {
        this.termDropDown = termDropDown;
    }

    public List<SelectItem> getParentMenuDropDown() {
        return parentMenuDropDown;
    }

    public void setParentMenuDropDown(List<SelectItem> parentMenuDropDown) {
        this.parentMenuDropDown = parentMenuDropDown;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }


    
    
}
