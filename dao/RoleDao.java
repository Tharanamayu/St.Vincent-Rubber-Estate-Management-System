/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Role;
import javafx.collections.ObservableList;

/**
 *
 * @author Sahan Rid
 */
public class RoleDao {

    public static ObservableList<Role> getAll() {
        return CommonDao.select("Role.findAll");
    }
    
}
