package com.inventory.Inventory;

public class ItemRequest {
    private String qrData;
    private String itemName;

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemRequest(String qrData, String itemName) {
        this.qrData = qrData;
        this.itemName = itemName;
    }
}
