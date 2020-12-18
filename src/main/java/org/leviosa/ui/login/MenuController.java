/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.login;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author bhaduri
 */
@Named(value = "menuController")
@RequestScoped
public class MenuController {

    private MenuModel menuModel;
   
    /**
     * Creates a new instance of MenuController
     */
    @Inject
    LoginController loginController;

    public MenuController() {
    }

    @PostConstruct
    void init() {
       

        if (loginController.getUserAuthDTO().getUserId() == null) {
            
        } else {
           
                
                menuModel = new DefaultMenuModel();
                DefaultMenuItem termUpdate = DefaultMenuItem.builder().value("Term").outcome("/cms/Term/TermList").build();
                DefaultMenuItem menuUpdate = DefaultMenuItem.builder().value("Menu").outcome("/cms/Menu/MenuList").build();
                DefaultSubMenu termManage =  DefaultSubMenu.builder().label("Term Admin").addElement(termUpdate).build();
                DefaultSubMenu menuManage = DefaultSubMenu.builder().label("Menu Admin").addElement(menuUpdate).build();

                menuModel.getElements().add(termManage);
                menuModel.getElements().add(menuManage);


        }
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }



}
