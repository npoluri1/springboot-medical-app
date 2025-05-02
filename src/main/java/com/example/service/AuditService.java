package com.example.service;

import com.example.model.AuditLog;
import com.example.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository repository;

    public void audit(String username, String action, String details) {
        repository.save(new AuditLog(username, action, details));
    }
}