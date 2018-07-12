/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Module;
import entity.Privilege;
import entity.Role;
import entity.User;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sahan Rid
 */
public class PrivilegeDao {
    public static ObservableList<Privilege> getAll() {
        return CommonDao.select("Privilege.findAll");
    }

    public static void add(Privilege privilege) {
        CommonDao.insert(privilege);
    }

    public static Privilege getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList<Privilege> list = CommonDao.select("Privilege.findById", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static ObservableList<Privilege> getAllByUser(User user) {
        HashMap hmap = new HashMap();
        hmap.put("user", user);
        return CommonDao.select("Privilege.findAllByUser", hmap);  
    }

    public static void update(Privilege privilege) {
        CommonDao.update(privilege);
    }

    public static void delete(Privilege privilege) {
        CommonDao.delete(privilege);
    }

    public static ObservableList<Privilege> getAllByRole(Role role) {
        HashMap hmap = new HashMap();
        hmap.put("role", role);
        return CommonDao.select("Privilege.findAllByRole", hmap);
    }

    public static ObservableList<Privilege> getAllByModule(Module module) {
        HashMap hmap = new HashMap();
        hmap.put("module", module);
        return CommonDao.select("Privilege.findAllByModule", hmap);
    }
}
