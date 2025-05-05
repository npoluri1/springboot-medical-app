package com.example.repository;

import com.example.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByDeleted(boolean deleted);

    Optional<MedicalRecord> findByIdAndDeleted(Long id, boolean deleted);

    boolean existsByIdAndDeleted(Long id, boolean deleted);
}