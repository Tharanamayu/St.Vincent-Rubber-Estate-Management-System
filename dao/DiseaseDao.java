/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Disease;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class DiseaseDao {

    public static ObservableList<Disease> getAll() {
        return CommonDao.select("Disease.findAll");
    }

}
