package com.example.assignement.repositories;

import com.example.assignement.domains.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {

}
