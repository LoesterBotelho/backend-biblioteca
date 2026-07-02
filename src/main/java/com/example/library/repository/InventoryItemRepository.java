package com.example.library.repository;

import com.example.library.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByBookIdAndBranchId(Long bookId, Long branchId);
}
