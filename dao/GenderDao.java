/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Gender;
import javafx.collections.ObservableList;

/**
 *
 * @author Sahan Rid
 */
public class GenderDao {

    public static ObservableList getAll() {
        return CommonDao.select("Gender.findAll");
    }
    
}
