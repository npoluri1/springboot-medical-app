package com.example.controller;


import com.example.model.MedicalRecord;
import com.example.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);


    @Autowired
    private MedicalRecordService service;

    // Helper for audit logging
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }

    // Get all medical records
    @GetMapping
    public List<MedicalRecord> getAllRecords() {
        logger.info("User '{}' requested all medical records", getCurrentUsername());
        return service.getAllRecords();
    }

    // Get a specific medical record by ID
    @GetMapping("/{id}")
    public MedicalRecord getRecordById(@PathVariable Long id) {
        logger.info("User '{}' requested medical record with ID={}", getCurrentUsername(), id);
        return service.getRecordById(id);
    }

    // Add a new medical record
    @PostMapping
    public MedicalRecord addRecord(@RequestBody MedicalRecord record) {
        logger.info("User '{}' is adding a new medical record: {}", getCurrentUsername(), record);
        return service.addRecord(record);
    }

    // Update an existing medical record
    @PutMapping("/{id}")
    public MedicalRecord updateRecord(@PathVariable Long id, @RequestBody MedicalRecord record) {
        logger.info("User '{}' is updating medical record ID={}: {}", getCurrentUsername(), id, record);
        return service.updateRecord(id, record);
    }

    // Delete a medical record by ID
    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        logger.info("User '{}' is deleting medical record ID={}", getCurrentUsername(), id);
        service.deleteRecord(id);
    }
}