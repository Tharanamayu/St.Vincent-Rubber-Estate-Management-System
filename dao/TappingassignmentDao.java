/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Employee;
import entity.Tappingassignment;
import entity.Tree;
import entity.Treeblock;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class TappingassignmentDao {
    public static ObservableList getAll() {
        return CommonDao.select("Tappingassignment.findAll");
    }

    public static void insert(Tappingassignment tappingAssignment) {
        CommonDao.insert(tappingAssignment);
    }
    
    public static void update(Tappingassignment tappingAssignment) {
        CommonDao.update(tappingAssignment);
    }
    
    public static void delete(Tappingassignment tappingAssignment){
        CommonDao.delete(tappingAssignment);
    }

    public static Tappingassignment getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList<Tappingassignment> list = CommonDao.select("Tappingassignment.findById", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static Tappingassignment getByTreeBlockAndEmployee(Treeblock treeBlock, Employee employee) {
        HashMap hmap = new HashMap();
        hmap.put("treeBlock", treeBlock);
        hmap.put("employee", employee);
        ObservableList<Tappingassignment> list = CommonDao.select("Tappingassignment.findByTreeBlockAndEmployee", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
