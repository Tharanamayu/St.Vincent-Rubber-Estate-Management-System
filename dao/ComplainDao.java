/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Complain;
import entity.Complainstatus;
import entity.Complaintype;
import entity.Tree;
import java.sql.Date;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class ComplainDao {

    public static ObservableList<Complain> getAll() {
        return CommonDao.select("Complain.findAll");
    }

    public static void insert(Complain complain) {
        CommonDao.insert(complain);
    }

    public static void update(Complain complain) {
        CommonDao.update(complain);
    }

    public static void delete(Complain complain) {
        CommonDao.delete(complain);
    }

    public static Complain getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList<Complain> list = CommonDao.select("Complain.findById", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static ObservableList<Complain> getAllByComplainDate(java.sql.Date date) {
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        return CommonDao.select("Dailytapping.findAllByComplainDate", hmap);
    }

    public static ObservableList<Complain> getAllByComplainStatus(Complainstatus complainStatus) {
        HashMap hmap = new HashMap();
        hmap.put("complainStatus", complainStatus);
        return CommonDao.select("Dailytapping.findAllByComplainStatus", hmap);
    }

    public static ObservableList<Complain> getAllByComplainType(Complaintype complainType) {
        HashMap hmap = new HashMap();
        hmap.put("complainType", complainType);
        return CommonDao.select("Dailytapping.findAllByComplainType", hmap);
    }

    public static ObservableList<Complain> getAllByComplainDateComplainType(java.sql.Date date, Complaintype complainType) {
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        hmap.put("complainType", complainType);
        return CommonDao.select("Dailytapping.findAllByComplainDateComplainType", hmap);
    }

    public static ObservableList<Complain> getAllByComplainDateComplainStatus(java.sql.Date date, Complainstatus complainStatus) {
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        hmap.put("complainStatus", complainStatus);
        return CommonDao.select("Dailytapping.findAllByComplainDateComplainStatus", hmap);
    }

    public static ObservableList<Complain> getAllByComplainTypeComplainStatus(Complaintype complainType, Complainstatus complainStatus) {
        HashMap hmap = new HashMap();
        hmap.put("complainType", complainType);
        hmap.put("complainStatus", complainStatus);
        return CommonDao.select("Dailytapping.findAllByComplainTypeComplainStatus", hmap);
    }

    public static ObservableList<Complain> getAllByComplainDateComplainTypeComplainStatus(java.sql.Date date, Complaintype complainType, Complainstatus complainStatus) {
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        hmap.put("complainType", complainType);
        hmap.put("complainStatus", complainStatus);
        return CommonDao.select("Dailytapping.findAllByComplainDateComplainTypeComplainStatus", hmap);
    }

}
