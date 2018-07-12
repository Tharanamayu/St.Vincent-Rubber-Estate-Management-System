/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Employee;
import entity.Privilege;
import entity.Role;
import entity.User;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sahan Rid
 */
public class UserDao {

    public static void add(User user) {
        CommonDao.insert(user);
    }

    public static ObservableList<User> getAll() {
        return CommonDao.select("User.findAll");

    }
    
    public static User getUsername(String username) {
        HashMap hmap = new HashMap();
        hmap.put("username", username);
        ObservableList<User> list = CommonDao.select("User.findByUsername", hmap);
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static User getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList<User> list = CommonDao.select("User.findById", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static void update(User user) {
        CommonDao.update(user);
    }

    public static void delete(User user) {
        CommonDao.delete(user);
    }

    public static ObservableList<User> getAllByName(String employeename) {
        HashMap hmap = new HashMap();
        hmap.put("employeename", employeename+"%");
        return CommonDao.select("User.findAllByEmployeeName", hmap);
    }

    public static ObservableList<User> getAllByRole(Role role) {
        HashMap hmap = new HashMap();
        hmap.put("role", role.getId());
        return CommonDao.select("User.findAllByRole", hmap);
    }

    public static ObservableList<User> getAllByUsername(String username) {
        HashMap hmap = new HashMap();
        hmap.put("username", username+"%");
        return CommonDao.select("User.findAllByUserName", hmap);
    }    
}
