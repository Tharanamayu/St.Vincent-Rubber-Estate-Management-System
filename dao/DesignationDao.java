/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Designation;
import javafx.collections.ObservableList;

/**
 *
 * @author Sahan Rid
 */
public class DesignationDao {

    public static ObservableList getAll() {
        return CommonDao.select("Designation.findAll");

    }

    public static void add(Designation designation) {
        CommonDao.insert(designation);
    }
    
}
