package com.inventory.Inventory.Service;

import com.inventory.Inventory.Entity.InventoryTable;
import com.inventory.Inventory.Entity.MainTable;

import java.util.List;

public interface InventoryService {
    public void addRecords(String upiCode, String partNumber, String partName);

   public boolean findIfPresent(String partNumber, String upiCode);

    boolean checkByPartNumber(String uniPartNumber);

    public void deleteItem(String upiCode, String partNumber);

    String findPartNameByPartNumber(String partNumber);

    boolean findByUPICodeAndParNumber(String uniUpICode, String uniPartNumber);

   public List<MainTable> getAllItems();

    List<InventoryTable> getAllInventoryItems();
//    public int checkIfPresent(String upiCode, String partNumber);
}
