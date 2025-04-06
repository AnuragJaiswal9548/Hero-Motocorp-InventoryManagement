package com.inventory.Inventory.Repository;

import com.inventory.Inventory.Entity.InventoryTable;
import com.inventory.Inventory.Entity.MainTable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MainTableRepo extends JpaRepository<MainTable, Integer> {
    @Transactional
    @Modifying
    @Query(value = "Delete FROM main_table WHERE part_no = :partNumber", nativeQuery = true)
    void deleteItems(@Param("partNumber") String partNumber);


    @Transactional
    @Modifying
    @Query("UPDATE MainTable m SET m.quantity = :quantity WHERE m.partNo = :partNumber")
    void updateQuantityByPartNo(@Param("partNumber") String partNumber, @Param("quantity") int quantity);
}
