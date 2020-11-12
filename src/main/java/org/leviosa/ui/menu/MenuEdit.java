/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
@Named(value = "menuEdit")
@ViewScoped
public class MenuEdit implements Serializable{

    private String menuId;
    private List<SelectItem> termDropDown;
    private List<SelectItem> parentMenuDropDown;
    private Menu menu;

    /**
     * Creates a new instance of MenuEdit
     */
    public MenuEdit() {
    }

    public void fillEditScreen() {
        CMSServiceCore mts = new CMSServiceCore();
        List<Term> termList = mts.getTermList();
        termDropDown = termList.stream().map(term-> {
            SelectItem selectItem = new SelectItem(term.getTermSlug(), term.getName());
            return selectItem;
        }).collect(Collectors.toList());
        
        Integer menuIdInt = Integer.parseInt(menuId);
        MenuService ms = new MenuService();
        List<Menu> menuList = ms.getMenuList();
        List<Menu> menuListWithout = menuList.stream().filter(m -> !Objects.equals(m.getId(), menuIdInt)).collect(Collectors.toList());
        parentMenuDropDown = new ArrayList<>();

        //selectItems.add(new SelectItem("None","None"));
        menuListWithout.forEach((mb) -> {
            parentMenuDropDown.add(new SelectItem(mb.getId(), mb.getName()));
        });
        //parentMenuDropDown = TermUtil.fillParentMenuList();
        List<Menu> menuEditList = menuList.stream().filter((Menu m)-> Objects.equals(m.getId(), menuIdInt)).collect(Collectors.toList());
        menu = menuEditList.get(0);
        //menuBean = ms.getMenu(Integer.parseInt(menuId));

    }

    public String save() {
        MenuService ms = new MenuService();

        int response = ms.editMenu(menu);
        FacesMessage message;
        FacesContext f = FacesContext.getCurrentInstance();
        f.getExternalContext().getFlash().setKeepMessages(true);

        if (response != HedwigResponseCode.SUCCESS) {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "MenuEdit";
            return redirectUrl;
        } else {
            HedwigResponseMessage responseMessage = new HedwigResponseMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", responseMessage.getResponseMessage(response));
            f.addMessage(null, message);
            String redirectUrl = "MenuList";
            return redirectUrl;
        }
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
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
