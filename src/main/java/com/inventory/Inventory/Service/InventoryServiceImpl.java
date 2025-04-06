package com.inventory.Inventory.Service;

import com.inventory.Inventory.Entity.InventoryTable;
import com.inventory.Inventory.Entity.MainTable;
import com.inventory.Inventory.Repository.InventoryRepository;
import com.inventory.Inventory.Repository.MainTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MainTableRepo mainTableRepo;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository, MainTableRepo mainTableRepo) {
        this.inventoryRepository = inventoryRepository;
        this.mainTableRepo = mainTableRepo;
    }

    @Override
    public void addRecords(String upiCode, String partNumber, String partName) {
        InventoryTable inventoryTable = new InventoryTable();
        inventoryTable.setPartNumber(partNumber);
        inventoryTable.setItemName(partName);
        inventoryTable.setUpiCode(upiCode);
        inventoryTable.setQuantity(1);
        inventoryRepository.save(inventoryTable);

        int count = inventoryRepository.countByPartNumber(partNumber);
        if (count <= 1) {
            addRecordMainTable(partNumber, partName);
        } else {
            mainTableRepo.updateQuantityByPartNo(partNumber, count);
        }
    }

    private void addRecordMainTable(String partNumber, String partName) {
        int count = inventoryRepository.countByPartNumber(partNumber);
        MainTable mainTable = new MainTable();
        mainTable.setPartNo(partNumber);
        mainTable.setPartName(partName);
        mainTable.setQuantity(count);
        mainTableRepo.save(mainTable);
    }

    @Override
    public boolean findIfPresent(String partNumber, String upiCode) {
        return inventoryRepository.countByUpiCodeAndPartNumber(upiCode, partNumber) > 0;
    }

    @Override
    public boolean checkByPartNumber(String partNumber) {
        return inventoryRepository.countByPartNumber(partNumber) > 0;
    }

    @Override
    public void deleteItem(String upiCode, String partNumber) {
        inventoryRepository.deleteItems(upiCode, partNumber);
        int count = inventoryRepository.countByPartNumber(partNumber);

        if (count == 0) {
            mainTableRepo.deleteItems(partNumber);
        } else {
            mainTableRepo.updateQuantityByPartNo(partNumber, count);
        }
    }

    @Override
    public String findPartNameByPartNumber(String partNumber) {
        return inventoryRepository.findPartNameByPartNumber(partNumber);
    }

    @Override
    public boolean findByUPICodeAndParNumber(String uniUpICode, String uniPartNumber) {
        return inventoryRepository.findByUpiCodeAndPartNumber(uniUpICode, uniPartNumber) > 0;
    }

    @Override
    public List<MainTable> getAllItems() {
        return mainTableRepo.findAll();
    }

    @Override
    public List<InventoryTable> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }

}
