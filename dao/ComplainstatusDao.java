/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Complainstatus;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class ComplainstatusDao {

    public static ObservableList<Complainstatus> getAll() {
return CommonDao.select("Complainstatus.findAll");
    }
    
}
