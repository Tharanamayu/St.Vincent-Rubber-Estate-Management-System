/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Complaintype;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class ComplaintypeDao {

    public static ObservableList<Complaintype> getAll() {
        return CommonDao.select("Complaintype.findAll");
    }
    
}
