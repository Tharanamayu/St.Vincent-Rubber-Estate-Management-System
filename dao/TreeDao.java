/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Designation;
import entity.Tree;
import entity.Treeblock;
import entity.Treestatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sahan Rid
 */
public class TreeDao {

    public static ObservableList getAll() {
        return CommonDao.select("Tree.findAll");
    }

    public static void insert(Tree tree) {
        CommonDao.insert(tree);
    }

    public static void update(Tree tree) {
        CommonDao.update(tree);
    }

    public static void delete(Tree tree) {
        CommonDao.delete(tree);
    }

    public static Tree getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList<Tree> list = CommonDao.select("Tree.findById", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static ObservableList<Tree> getAllByTreekNo(String treeNo) {
        HashMap hmap = new HashMap();
        hmap.put("treeNo", treeNo + "%");
        return CommonDao.select("Tree.findAllByTreeNo", hmap);
    }

    public static ObservableList<Tree> getAllByTreeBlock(Treeblock treeBlock) {
        HashMap hmap = new HashMap();
        hmap.put("treeBlock", treeBlock);
        return CommonDao.select("Tree.findAllByTreeBlock", hmap);
    }

    public static ObservableList<Tree> getAllByStatus(Treestatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        return CommonDao.select("Tree.findAllByStatus", hmap);
    }

    public static ObservableList<Tree> getAllByTreeNoStatus(String treeNo, Treestatus status) {
        HashMap hmap = new HashMap();
        hmap.put("treeNo", treeNo + "%");
        hmap.put("status", status);
        return CommonDao.select("Tree.findAllByTreeNoStatus", hmap);
    }

    public static ObservableList<Tree> getAllByTreeNoTreeBlock(String treeNo, Treeblock treeBlock) {
        HashMap hmap = new HashMap();
        hmap.put("treeNo", treeNo + "%");
        hmap.put("treeBlock", treeBlock);
        return CommonDao.select("Tree.findAllByTreeNoTreeBlock", hmap);
    }

    public static ObservableList<Tree> getAllByStatusTreeBlock(Treestatus status, Treeblock treeBlock) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("treeBlock", treeBlock);
        return CommonDao.select("Tree.findAllByStatusTreeBlock", hmap);
    }

    public static ObservableList<Tree> getAllByTreeNoStatusTreeBlock(String treeNo, Treestatus status, Treeblock treeBlock) {
        HashMap hmap = new HashMap();
        hmap.put("treeNo", treeNo + "%");
        hmap.put("status", status);
        hmap.put("treeBlock", treeBlock);
        return CommonDao.select("Tree.findAllByTreeNoStatusTreeBlock", hmap);
    }
}
