/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Tappinglayer;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class TappinglayerDao {

    public static ObservableList<Tappinglayer> getAll() {
        return CommonDao.select("Tappinglayer.findAll");
    }
    
}
