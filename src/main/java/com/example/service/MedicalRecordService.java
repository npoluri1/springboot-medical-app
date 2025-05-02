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

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }

    public List<MedicalRecord> getAllRecords() {
        logger.debug("Fetching all medical records from repository");
        auditService.audit(getCurrentUsername(), "GET_ALL", "Fetched all records");
        return repository.findAll();
    }

    public MedicalRecord getRecordById(Long id) {
        logger.debug("Fetching medical record with ID={} from repository", id);
        auditService.audit(getCurrentUsername(), "GET_BY_ID", "Fetched record id=" + id);
        return repository.findById(id).orElse(null);
    }

    public MedicalRecord addRecord(MedicalRecord record) {
        logger.debug("Adding new medical record: {}", record);
        auditService.audit(getCurrentUsername(), "ADD", "Added record: " + record);
        return repository.save(record);
    }

    public MedicalRecord updateRecord(Long id, MedicalRecord record) {
        if (repository.existsById(id)) {
            logger.debug("Updating medical record ID={}: {}", id, record);
            auditService.audit(getCurrentUsername(), "UPDATE", "Updated record id=" + id + ": " + record);
            record.setId(id);
            return repository.save(record);
        }
        logger.warn("Attempted to update non-existent medical record ID={}");
        auditService.audit(getCurrentUsername(), "UPDATE_FAIL", "Attempted update on non-existent record id=" + id);
        return null;
    }

    public void deleteRecord(Long id) {
        logger.debug("Deleting medical record ID={}", id);
        auditService.audit(getCurrentUsername(), "DELETE", "Deleted record id=" + id);
        repository.deleteById(id);
    }
}