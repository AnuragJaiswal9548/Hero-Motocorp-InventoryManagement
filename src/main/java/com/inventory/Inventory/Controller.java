package com.inventory.Inventory;

import com.inventory.Inventory.Entity.InventoryTable;
import com.inventory.Inventory.Entity.MainTable;
import com.inventory.Inventory.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/scan")
public class Controller {

    private InventoryService inventoryService;

    @Autowired
    public Controller(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    String uniPartNumber = "";
    String uniUpICode = "";

    // Process scanned QR code
    @PostMapping("/process-scan")
    public ResponseEntity<ApiResponse> processQrCode(@RequestBody ScanRequest request) {
        String scannedData = request.getQrData();
        String input = scannedData.replaceAll("\\s+", "");
        String[] parts = input.split("/");

        String upiCode = null;
        String partNumber = null;

        for (int i = 0; i < parts.length - 1; i++) {
            if (isAlphanumeric(parts[i]) && parts[i].length() == 12) {
                upiCode = parts[i];
                partNumber = parts[i + 1];
                break;
            }
        }

        if (upiCode == null || partNumber == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid QR Code"));
        }

        uniPartNumber = partNumber.trim();
        uniUpICode = upiCode.trim();

        boolean duplicate = inventoryService.findByUPICodeAndParNumber(uniUpICode, uniPartNumber);
        if (duplicate) {
            return ResponseEntity.ok(new ApiResponse(true, "Item already exists in stock."));
        }

        boolean exists = inventoryService.checkByPartNumber(uniPartNumber);
        if (exists) {
            boolean alreadyPresent = inventoryService.findIfPresent(uniPartNumber, uniUpICode);
            if (alreadyPresent) {
                return ResponseEntity.ok(new ApiResponse(false, "Duplicate record."));
            } else {
                String partName = inventoryService.findPartNameByPartNumber(uniPartNumber);
                inventoryService.addRecords(upiCode, uniPartNumber, partName);
                return ResponseEntity.ok(new ApiResponse(true, "Item added to inventory."));
            }
        }

        return ResponseEntity.ok(new ApiResponse(false, "Part number not found. Please provide item name."));
    }

    // Add new item if not found in main table
    @CrossOrigin(origins = "*")
    @PostMapping("/addItem")
    public ResponseEntity<ApiResponse> addItems(@RequestBody ItemRequest itemRequest) {
        boolean exists = inventoryService.findIfPresent(uniPartNumber, uniUpICode);
        if (exists) {
            return ResponseEntity.ok(new ApiResponse(false, "Duplicate record. Not added."));
        }

        String partName = itemRequest.getItemName();
        boolean added = addRecords(uniUpICode, uniPartNumber, partName);
        if (added) {
            return ResponseEntity.ok(new ApiResponse(true, "Item added successfully."));
        } else {
            return ResponseEntity.ok(new ApiResponse(false, "Failed to add item."));
        }
    }

    // Delete item based on scanned QR code
    @CrossOrigin(origins = "*")
    @PostMapping("/check")
    public ResponseEntity<ApiResponse> extractQrCode(@RequestBody ScanRequest request) {
        String scannedData = request.getQrData();
        String input = scannedData.replaceAll("\\s+", "");
        String[] parts = input.split("/");

        String upiCode = null;
        String partNumber = null;

        for (int i = 0; i < parts.length - 1; i++) {
            if (isAlphanumeric(parts[i]) && parts[i].length() == 12) {
                upiCode = parts[i];
                partNumber = parts[i + 1];
                break;
            }
        }

        if (upiCode == null || partNumber == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid QR code."));
        }

        inventoryService.deleteItem(upiCode.trim(), partNumber.trim());
        return ResponseEntity.ok(new ApiResponse(true, "Item deleted successfully."));
    }

    // Helper method to check if a string is alphanumeric
    private static boolean isAlphanumeric(String str) {
        return str.matches("^[a-zA-Z0-9]+$");
    }

    // Add record helper
    public boolean addRecords(String upiCode, String partNumber, String partName) {
        boolean check = inventoryService.findIfPresent(partNumber, upiCode);
        if (check) {
            return false;
        } else {
            inventoryService.addRecords(upiCode, partNumber, partName);
            return true;
        }
    }

    // Inner DTO class for scan requests
    static class ScanRequest {
        private String qrData;

        public String getQrData() {
            return qrData;
        }

        public void setQrData(String qrData) {
            this.qrData = qrData;
        }
    }


    // Inner DTO class for item add request
    static class ItemRequest {
        private String itemName;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }


    @GetMapping("/getAllItems")
    public List<MainTable> getAllItems() {
        return inventoryService.getAllItems();
    }
    @GetMapping("/getAllInventoryItems")
    public List<InventoryTable> getAllInventoryItems() {
        return inventoryService.getAllInventoryItems();
    }
}

