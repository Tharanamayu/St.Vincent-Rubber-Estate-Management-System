/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Employee;
import entity.Weedingassignment;
import entity.Tree;
import entity.Treeblock;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class WeedingassignmentDao {

    public static ObservableList getAll() {
        return CommonDao.select("Weedingassignment.findAll");
    }

    public static void insert(Weedingassignment weedingAssignment) {
        CommonDao.insert(weedingAssignment);
    }

    public static void update(Weedingassignment weedingAssignment) {
        CommonDao.update(weedingAssignment);
    }

    public static void delete(Weedingassignment weedingAssignment) {
        CommonDao.delete(weedingAssignment);
    }

    public static Weedingassignment getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList<Weedingassignment> list = CommonDao.select("Weedingassignment.findById", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static Weedingassignment getByTreeBlockAndEmployee(Treeblock treeBlock, Employee employee) {
        HashMap hmap = new HashMap();
        hmap.put("treeBlock", treeBlock);
        hmap.put("employee", employee);
        ObservableList<Weedingassignment> list = CommonDao.select("Weedingassignment.findByTreeBlockAndEmployee", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
