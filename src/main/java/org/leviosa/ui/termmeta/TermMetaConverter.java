/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.termmeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.orderlist.OrderList;

/**
 *
 * @author bhaduri
 */
@FacesConverter ("termMetaConverter")
public class TermMetaConverter implements Converter {
    private List<Map<String, Object>> termScreenFields;
    @Override
    public Object getAsObject (FacesContext context, UIComponent component, String newValue) {
        if (component instanceof OrderList) {
            //Object list = ((OrderList) component).getValue();
            termScreenFields = (ArrayList<Map<String, Object>>)((OrderList) component).getValue();
            //termMetaList = (ArrayList<TermMetaBean>) list;
        }
        if (newValue.isEmpty()) {
            return null;
        }
        for (Map<String, Object> termMetaInMap : termScreenFields) {
            if (termMetaInMap.get("metaKey").toString().equals(newValue)) {
                return termMetaInMap;
            }
        }
        return null;
    }
    @Override
    public String getAsString (FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        } 

        Map<String, Object> row = (Map<String, Object>) value;

        String output = (String)row.get("metaKey");

        return output;
    }
    
}

