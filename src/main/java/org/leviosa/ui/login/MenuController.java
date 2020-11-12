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

                DefaultSubMenu termManage = new DefaultSubMenu("Term Admin");
                DefaultMenuItem termUpdate = new DefaultMenuItem("Term");
                termUpdate.setOutcome("/cms/Term/TermList");
                termManage.addElement(termUpdate);
                
                DefaultSubMenu menuManage = new DefaultSubMenu("Menu Admin");
                DefaultMenuItem menuUpdate = new DefaultMenuItem("Menu");
                menuUpdate.setOutcome("/cms/Menu/MenuList");
                menuManage.addElement(menuUpdate);
                
                
                //MenuMaker menuMaker = new MenuMaker();
                //menuModel.addElement(menuMaker.getUserMenu());
                menuModel.addElement(termManage);
                menuModel.addElement(menuManage);


        }
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }



}
