/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Employee;
import entity.Gender;
import entity.Userstatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sahan Rid
 */
public class UserstatusDao {

    public static ObservableList getAll() {
        return CommonDao.select("Userstatus.findAll");
    }

    public static Userstatus getByName(String name) {
        HashMap hmap = new HashMap();
        hmap.put("name", name);
        ObservableList<Userstatus> list = CommonDao.select("Userstatus.findByName", hmap);
        return (!list.isEmpty()?list.get(0):null);
    }
}
