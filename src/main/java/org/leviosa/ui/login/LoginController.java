/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.login;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.leviosa.bl.service.DatabaseConnection;
import org.hedwig.cloud.client.TenantListClient;
import org.hedwig.cloud.dto.ProductDTO;
import org.hedwig.cloud.dto.TenantDTO;
import org.hedwig.cloud.dto.UserAuthDTO;
import org.hedwig.cloud.client.DGRFProductListClient;
import org.hedwig.cloud.client.UserAuthClient;
import org.hedwig.cloud.response.HedwigResponseCode;
import org.hedwig.cloud.dto.HedwigAuthCredentials;

/**
 *
 * @author dgrf-vi
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    /**
     * Creates a new instance of LoginController
     */
    private int tenantID;
    private String tenantName;
    private int productID;
    private String userID;
    private String password;
    private UserAuthDTO userAuthDTO;
    private String selectedTheme;
    private String productName;
    private String selectedHomeImg;
    private String productCaption;
    private String homeScreenLogo;
    private String iconAndLogo;

    private Map<String, Object> tenantMap;
    private Map<String, Object> productMap;

    public LoginController() {
    }

    @PostConstruct
    private void init() {
        userAuthDTO = new UserAuthDTO();
        userAuthDTO.setUserId(null);

    }

    public UserAuthDTO getUserAuthDTO() {
        return userAuthDTO;
    }

    public void setUserAuthDTO(UserAuthDTO userAuthDTO) {
        this.userAuthDTO = userAuthDTO;
    }

    public void fillLOginFormValues() {
        TenantListClient dgrftlc = new TenantListClient();
        List<TenantDTO> tenantDTOs = dgrftlc.getTenantList(productID);
        tenantMap = new HashMap<>();
        for (TenantDTO tdto : tenantDTOs) {
            tenantMap.put(tdto.getName(), tdto.getTenantId());
        }
    }

    public void fillCMSProductList() {
        DGRFProductListClient dgrfplc = new DGRFProductListClient();
        List<ProductDTO> dgrfProductDTOs = dgrfplc.getProductList();
        productMap = dgrfProductDTOs.stream().collect(Collectors.toMap(ProductDTO::getProductName, ProductDTO::getProductId));
    }

    private void setAuthCredentials() {
        HedwigAuthCredentials authCredentials = new HedwigAuthCredentials();
        authCredentials.setUserId(userID);
        authCredentials.setPassword(password);
        authCredentials.setProductId(productID);
        authCredentials.setTenantId(tenantID);
        CMSClientAuthCredentialValue.AUTH_CREDENTIALS = authCredentials;
    }

    public String login() {
        //userAuthBean = new UserAuthBean();
        UserAuthClient uac = new UserAuthClient();
        userAuthDTO.setUserId(userID);
        userAuthDTO.setPassword(password);
        userAuthDTO.setProductId(productID);
        userAuthDTO.setTenantId(tenantID);
        userAuthDTO = uac.authenticateUser(userAuthDTO);
        FacesMessage message;

        if (userAuthDTO.getResponseCode() == HedwigResponseCode.SUCCESS) {
            if (userAuthDTO.getRoleId() != 1) {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "User is not Admin");
                FacesContext f = FacesContext.getCurrentInstance();
                f.addMessage(null, message);
                return "Login";
            }
            setAuthCredentials();
            DatabaseConnection dc = new DatabaseConnection(userAuthDTO.getDbAdminUser(), userAuthDTO.getDbAdminPassword(), userAuthDTO.getDbConnUrl());
            return "Landing";
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", userAuthDTO.getResponseMessage());
            FacesContext f = FacesContext.getCurrentInstance();
            f.addMessage(null, message);
            return "Login";
        }

    }

    public String logout() {
        userAuthDTO.setUserId(null);
        userID = null;
        password = null;
        return "/Login?faces-redirect=true";
    }

    public Map<String, Object> getTenantMap() {
        return tenantMap;
    }

    public void setTenantMap(Map<String, Object> tenantMap) {
        this.tenantMap = tenantMap;
    }

    public int getTenantID() {
        return tenantID;
    }

    public void setTenantID(int tenantID) {
        this.tenantID = tenantID;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String selectTenant() {
        return "welcome?faces-redirect=true&tenant=" + tenantID;
    }

    public Map<String, Object> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<String, Object> productMap) {
        this.productMap = productMap;
    }

    public String moveToDefaultHost() {
        //productID = 1;
        if (productID == 0) {
            return "selectProduct";
        }
        if (tenantID == 0) {
            return "SelectTenant";
        }

        TenantListClient dgrftlc = new TenantListClient();
        List<TenantDTO> tenantDTOs = dgrftlc.getTenantList(productID);
        if (tenantDTOs == null) {
            return "access";
        }

        List<TenantDTO> tenantDTOMatched = tenantDTOs.stream().filter(tenant -> tenant.getTenantId() == tenantID).collect(Collectors.toList());
        if (tenantDTOMatched.isEmpty()) {
            return "access";
        }
        tenantName = tenantDTOMatched.get(0).getName();
        setSelectedThemeCss(tenantID, productID);
        //this.selectedTheme = "css/term-green.css";
        return "index";

    }

    private void setSelectedThemeCss(int tenantID, int productID) {

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String contextPath = servletContext.getContextPath();

        StringBuilder sb = new StringBuilder(contextPath);
        sb.deleteCharAt(0);
        productName = sb.toString();
        productCaption = "Time you enjoy wasting is not wasted time.";
        selectedTheme = "css/term-blue.css";
        homeScreenLogo = contextPath + "/faces/javax.faces.resource/term/images/default-butterfly.png";
        iconAndLogo = contextPath + "/faces/javax.faces.resource/term/images/dgrflogo.png";
        selectedHomeImg = contextPath + "/faces/javax.faces.resource/term/images/dgrf-default-home-img.jpg";

    }

    public void goToUserRegister() {

    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(String selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSelectedHomeImg() {
        return selectedHomeImg;
    }

    public void setSelectedHomeImg(String selectedHomeImg) {
        this.selectedHomeImg = selectedHomeImg;
    }

    public String getProductCaption() {
        return productCaption;
    }

    public void setProductCaption(String productCaption) {
        this.productCaption = productCaption;
    }

    public String getHomeScreenLogo() {
        return homeScreenLogo;
    }

    public void setHomeScreenLogo(String homeScreenLogo) {
        this.homeScreenLogo = homeScreenLogo;
    }

    public String getIconAndLogo() {
        return iconAndLogo;
    }

    public void setIconAndLogo(String iconAndLogo) {
        this.iconAndLogo = iconAndLogo;
    }

}
