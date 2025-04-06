package com.inventory.Inventory.Repository;

import com.inventory.Inventory.Entity.InventoryTable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends JpaRepository<InventoryTable, Integer> {

//    @Query(value = "SELECT COUNT(id) FROM inventory_table WHERE upi_code = ?1 AND part_number = ?2", nativeQuery = true)
//    int countByUpiCodeAndPartNumber(String upiCode, String partNumber);

    @Query(value = "SELECT COUNT(id) FROM inventory_table WHERE upi_code = :upiCode AND part_number = :partNumber", nativeQuery = true)
    int countByUpiCodeAndPartNumber(@Param("upiCode") String upiCode, @Param("partNumber") String partNumber);


    @Query(value = "SELECT COUNT(id) FROM inventory_table WHERE part_number = :partNumber", nativeQuery = true)
    int countByPartNumber(@Param("partNumber")String partNumber);

    @Transactional
    @Modifying
    @Query(value = "Delete FROM inventory_table WHERE upi_code = :upiCode AND part_number = :partNumber", nativeQuery = true)
    void deleteItems(@Param("upiCode") String upiCode, @Param("partNumber") String partNumber);


    @Query(value = "SELECT item_name FROM inventory_table WHERE  part_number = :partNumber", nativeQuery = true)
    public String findPartNameByPartNumber(@Param("partNumber") String partNumber);

    @Query(value = "SELECT COUNT(upi_code) FROM inventory_table WHERE upi_code = :upiCode AND part_number = :partNumber", nativeQuery = true)
    int findByUpiCodeAndPartNumber(@Param("upiCode") String upiCode, @Param("partNumber") String partNumber);
}
