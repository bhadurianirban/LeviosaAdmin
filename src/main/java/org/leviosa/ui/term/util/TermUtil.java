/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.term.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.model.SelectItem;
import org.leviosa.bl.service.CMSServiceCore;
import org.leviosa.db.entities.Term;

/**
 *
 * @author bhaduri
 */
public class TermUtil {

    public static List<SelectItem> fillTermSlugList(String termSlug) {

        CMSServiceCore mts = new CMSServiceCore();
        List<Term> termList = mts.getTermList();
        List<SelectItem> selectItems = new ArrayList<>();
        //selectItems.add(new SelectItem("None","None"));
        termList.stream().filter((term) -> (!term.getTermSlug().equals(termSlug))).forEachOrdered((term) -> {
            selectItems.add(new SelectItem(term.getTermSlug(), term.getName()));
        });

        return selectItems;
    }

//    public static List<SelectItem> fillTermSlugList() {
//        MasterTermService mts = new MasterTermService();
//        List<Term> termList = mts.getTermList();
//        List<SelectItem> selectItems = new ArrayList<>();
//        //selectItems.add(new SelectItem("None","None"));
//        termList.forEach((gardenTerm) -> {
//            selectItems.add(new SelectItem(gardenTerm.getSlug(), gardenTerm.getName()));
//        });
//
//        return selectItems;
//    }





    public static List<SelectItem> convertMapToSelectItem(Map<String, String> selectItemMap) {
        if (selectItemMap == null) {
            return null;
        }

        List<SelectItem> selectItems =
                        selectItemMap.entrySet().stream().map((entry) -> {
            SelectItem si = new SelectItem();
            si.setLabel(entry.getValue());
            si.setValue(entry.getKey());
            return si;
        }).collect(Collectors.toList());
        return selectItems;
    }

//    public static List<SelectItem> getTermMetaDataTypes() {
//        MasterTermService mts = new MasterTermService();
//        Map<String, String> termMetaDataTypes = mts.getTermMetaDataTypes();
//        List<SelectItem> selectItems = convertMapToSelectItem(termMetaDataTypes);
//        return selectItems;
//    }
}
