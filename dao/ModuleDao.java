/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Module;
import entity.Role;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sahan Rid
 */
public class ModuleDao {

    public static ObservableList<Module> getAll() {
        return CommonDao.select("Module.findAll");
    }

    public static ObservableList<Module> getAllUnssignedToRole(Role roleId) {
        //return CommonDao.select("Module.findAllUnssignedToRole");
        //change the quary later
        
        HashMap hmap = new HashMap();
        hmap.put("roleId", roleId);
        return CommonDao.select("Module.findAllUnssignedToModule", hmap);
    }
    
}
