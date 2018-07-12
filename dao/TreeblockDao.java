/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Employee;
import entity.Tappinglayer;
import entity.Treeblock;
import entity.Treeblockstatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author SahanRid
 */
public class TreeblockDao {

    public static ObservableList getAll() {
        return CommonDao.select("Treeblock.findAll");
    }

    public static void insert(Treeblock treeblock) {
        CommonDao.insert(treeblock);
    }

    public static void delete(Treeblock treeblock) {
        CommonDao.delete(treeblock);
    }

    public static void update(Treeblock treeblock) {
        CommonDao.update(treeblock);
    }

    public static ObservableList<Treeblock> getAllExceptAssignedBlockTP() {
        return CommonDao.select("Treeblock.findAllExceptAssignedBlockTP");
    }

    public static ObservableList<Treeblock> getAllExceptAssignedBlockWD() {
        return CommonDao.select("Treeblock.findAllExceptAssignedBlockWD");
    }

    public static ObservableList<Treeblock> getAllByEmployeeTP(Employee employeeId) {
        HashMap hmap = new HashMap();
        hmap.put("employeeId", employeeId);
        return CommonDao.select("Treeblock.findAllByEmployeeTP", hmap);
    }

    public static ObservableList<Treeblock> getAllByEmployeeWD(Employee employeeId) {
        HashMap hmap = new HashMap();
        hmap.put("employeeId", employeeId);
        return CommonDao.select("Treeblock.findAllByEmployeeWD", hmap);
    }

    public static Treeblock getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList<Treeblock> list = CommonDao.select("Treeblock.findById", hmap);
        if (list == null) {
            return null;
        } else {
            return list.get(0);
        }

    }

    public static ObservableList<Treeblock> getAllByBlockNo(String blockNo) {
        HashMap hmap = new HashMap();
        hmap.put("blockNo", blockNo + "%");
        return CommonDao.select("Treeblock.findAllByBlockNo", hmap);
    }

    public static ObservableList<Treeblock> getAllByTappingLayer(Tappinglayer tappingLayer) {
        HashMap hmap = new HashMap();
        hmap.put("tappingLayer", tappingLayer);
        return CommonDao.select("Treeblock.findAllByTappingLayer", hmap);
    }

    public static ObservableList<Treeblock> getAllByStatus(Treeblockstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        return CommonDao.select("Treeblock.findAllByStatus", hmap);
    }

    public static ObservableList<Treeblock> getAllByBlockNoStatus(String blockNo, Treeblockstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("blockNo", blockNo);
        hmap.put("status", status);
        return CommonDao.select("Treeblock.findAllByBlockNoStatus", hmap);
    }

    public static ObservableList<Treeblock> getAllByBlockNoTappingLayer(String blockNo, Tappinglayer tappingLayer) {
        HashMap hmap = new HashMap();
        hmap.put("blockNo", blockNo);
        hmap.put("tappingLayer", tappingLayer);
        return CommonDao.select("Treeblock.findAllByBlockNoTappingLayer", hmap);
    }

    public static ObservableList<Treeblock> getAllByStatusTappingLayer(Treeblockstatus status, Tappinglayer tappingLayer) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("tappingLayer", tappingLayer);
        return CommonDao.select("Treeblock.findAllByStatusTappingLayer", hmap);
    }

    public static ObservableList<Treeblock> getAllByBlockNoStatusTappingLayer(String blockNo, Treeblockstatus status, Tappinglayer tappingLayer) {
        HashMap hmap = new HashMap();
        hmap.put("blockNo", blockNo);
        hmap.put("status", status);
        hmap.put("tappingLayer", tappingLayer);
        return CommonDao.select("Treeblock.findAllByBlockNoStatusTappingLayer", hmap);
    }

}
