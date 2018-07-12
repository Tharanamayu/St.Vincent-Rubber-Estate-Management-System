/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Treeblockstatus;
import entity.Treestatus;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class TreestatusDao {

    public static ObservableList<Treestatus> getAll() {
        return CommonDao.select("Treestatus.findAll");
    }
    
}
