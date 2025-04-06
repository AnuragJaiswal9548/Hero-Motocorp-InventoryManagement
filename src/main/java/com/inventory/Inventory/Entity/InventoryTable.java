package com.inventory.Inventory.Entity;


import jakarta.persistence.*;

@Entity
public class InventoryTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String itemName;

    @Column(name = "upi_code")
    private String upiCode;
    private int quantity;
    @Column(name = "part_number")
    private String partNumber;

    public InventoryTable() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUpiCode() {
        return upiCode;
    }

    public void setUpiCode(String upiCode) {
        this.upiCode = upiCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public InventoryTable(int id, String itemName, String upiCode, int quantity, String partNumber) {
        this.id = id;
        this.itemName = itemName;
        this.upiCode = upiCode;
        this.quantity = quantity;
        this.partNumber = partNumber;
    }
}
