/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Dailytapping;
import entity.Employee;
import entity.Tree;
import entity.Treeblock;
import entity.Treestatus;
import java.time.LocalDate;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class DailytappingDao {

    public static ObservableList getAll() {
        return CommonDao.select("Dailytapping.findAll");
    }

    public static void insert(Dailytapping dailyTapping) {
        CommonDao.insert(dailyTapping);
    }

    public static void update(Dailytapping dailyTapping) {
        CommonDao.update(dailyTapping);
    }

    public static void delete(Dailytapping dailyTapping) {
        CommonDao.delete(dailyTapping);
    }

    public static Dailytapping getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList<Dailytapping> list = CommonDao.select("Dailytapping.findById", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static ObservableList<Dailytapping> getAllByTappingDate(java.sql.Date date) {
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        return CommonDao.select("Dailytapping.findAllByTappingDate", hmap);
    }

    public static ObservableList<Dailytapping> getAllByEmployee(Employee employee) {
        HashMap hmap = new HashMap();
        hmap.put("employee", employee);
        return CommonDao.select("Dailytapping.findAllByEmployee", hmap);
    }

    public static ObservableList<Dailytapping> getAllByTreeBlock(Treeblock treeBlock) {
        HashMap hmap = new HashMap();
        hmap.put("treeBlock", treeBlock);
        return CommonDao.select("Dailytapping.findAllByTreeBlock", hmap);
    }

    public static ObservableList<Dailytapping> getAllByTappingDateTreeBlock(java.sql.Date date, Treeblock treeBlock) {
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        hmap.put("treeBlock", treeBlock);
        return CommonDao.select("Dailytapping.findAllByTappingDateTreeBlock", hmap);
    }

    public static ObservableList<Dailytapping> getAllByTappingDateEmployee(java.sql.Date date, Employee employee) {
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        hmap.put("employee", employee);
        return CommonDao.select("Dailytapping.findAllByTappingDateEmployee", hmap);
    }

    public static ObservableList<Dailytapping> getAllByTreeBlockEmployee(Treeblock treeBlock, Employee employee) {
        HashMap hmap = new HashMap();
        hmap.put("treeBlock", treeBlock);
        hmap.put("employee", employee);
        return CommonDao.select("Dailytapping.findAllByTreeBlockEmployee", hmap);
    }

    public static ObservableList<Dailytapping> getAllByTappingDateTreeBlockEmployee(java.sql.Date date, Treeblock treeBlock, Employee employee) {
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        hmap.put("treeBlock", treeBlock);
        hmap.put("employee", employee);
        return CommonDao.select("Dailytapping.findAllByTappingDateTreeBlockEmployee", hmap);
    }
}
