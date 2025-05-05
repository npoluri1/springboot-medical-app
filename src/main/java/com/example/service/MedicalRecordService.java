package com.example.service;

import com.example.model.MedicalRecord;
import com.example.repository.MedicalRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    @Autowired
    private MedicalRecordRepository repository;

    @Autowired
    private AuditService auditService;

    // Helper method to get the current username for auditing purposes
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }

    // Fetch all medical records (excluding soft-deleted records)
    public List<MedicalRecord> getAllRecords() {
        logger.debug("Fetching all medical records from repository");
        auditService.audit(getCurrentUsername(), "GET_ALL", "Fetched all records");
        return repository.findByDeleted(false); // Exclude soft-deleted records
    }

    // Fetch a medical record by its ID (excluding soft-deleted records)
    public MedicalRecord getRecordById(Long id) {
        logger.debug("Fetching medical record with ID={} from repository", id);
        auditService.audit(getCurrentUsername(), "GET_BY_ID", "Fetched record id=" + id);
        return repository.findByIdAndDeleted(id, false).orElse(null); // Exclude soft-deleted records
    }

    // Add a new medical record
    public MedicalRecord addRecord(MedicalRecord record) {
        logger.debug("Adding new medical record: {}", record);
        auditService.audit(getCurrentUsername(), "ADD", "Added record: " + record);
        return repository.save(record);
    }

    // Update an existing medical record by ID
    public MedicalRecord updateRecord(Long id, MedicalRecord record) {
        if (repository.existsByIdAndDeleted(id, false)) {
            logger.debug("Updating medical record ID={}: {}", id, record);
            auditService.audit(getCurrentUsername(), "UPDATE", "Updated record id=" + id + ": " + record);
            record.setId(id); // Ensure the ID is set before saving
            return repository.save(record);
        }
        // If the record does not exist, log and audit the failure
        logger.warn("Attempted to update non-existent or deleted medical record ID={}", id);
        auditService.audit(getCurrentUsername(), "UPDATE_FAIL", "Attempted update on non-existent or deleted record id=" + id);
        return null; // Return null if the record does not exist
    }

    // Soft delete a medical record by ID
    public void deleteRecord(Long id) {
        MedicalRecord record = repository.findByIdAndDeleted(id, false).orElse(null);
        if (record != null) {
            logger.debug("Soft deleting medical record ID={}", id);
            record.setDeleted(true); // Mark the record as deleted
            repository.save(record);
            auditService.audit(getCurrentUsername(), "DELETE", "Soft deleted record id=" + id);
        } else {
            logger.warn("Attempted to delete non-existent or already deleted medical record ID={}", id);
            auditService.audit(getCurrentUsername(), "DELETE_FAIL", "Attempted delete on non-existent or already deleted record id=" + id);
        }
    }
}