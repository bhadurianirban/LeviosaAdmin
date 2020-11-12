/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

import org.leviosa.bl.service.MenuService;
import org.hedwig.cloud.response.HedwigResponseCode;
import org.hedwig.cloud.response.HedwigResponseMessage;

/**
 *
 * @author bhaduri
 */
@Named(value = "menuList")
@ViewScoped
public class MenuList implements Serializable {


    private List<HashMap<String, String>> menuBeanMapList;
    private HashMap<String, String> selectedMenu;

    /**
     * Creates a new instance of MenuList
     */
    public MenuList() {
    }

    public void createMenuList() {
        MenuService ms = new MenuService();
        menuBeanMapList = ms.getMenuListInMap();
        //menuBeanList = ms.getMenuList();
    }

    public String deleteMenu () {
        MenuService ms = new MenuService();
        int response = ms.deleteMenu(selectedMenu.get("id"));
        FacesMessage message;
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);
        
        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "MenuList";
            return redirectUrl;
        } else {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "MenuList";
            return redirectUrl;
        }
    }

    public HashMap<String, String> getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(HashMap<String, String> selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public List<HashMap<String, String>> getMenuBeanMapList() {
        return menuBeanMapList;
    }

    public void setMenuBeanMapList(List<HashMap<String, String>> menuBeanMapList) {
        this.menuBeanMapList = menuBeanMapList;
    }
    

}
