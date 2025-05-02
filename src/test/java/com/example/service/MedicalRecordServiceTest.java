package com.example.service;

import com.example.model.MedicalRecord;
import com.example.repository.MedicalRecordRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository repository;

    @Mock
    private AuditService auditService; // <-- Mock the audit service

    @InjectMocks
    private MedicalRecordService service;

    public MedicalRecordServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRecords() {
        MedicalRecord record1 = new MedicalRecord();
        record1.setId(1L);
        record1.setName("John Doe");
        record1.setAge(30);
        record1.setMedicalHistory("None");

        MedicalRecord record2 = new MedicalRecord();
        record2.setId(2L);
        record2.setName("Jane Smith");
        record2.setAge(25);
        record2.setMedicalHistory("Asthma");

        when(repository.findAll()).thenReturn(Arrays.asList(record1, record2));

        var records = service.getAllRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
        verify(repository, times(1)).findAll();
        verify(auditService, atLeastOnce()).audit(anyString(), anyString(), anyString()); // optional
    }

    @Test
    void testGetRecordById() {
        MedicalRecord record = new MedicalRecord();
        record.setId(1L);
        record.setName("John Doe");
        record.setAge(30);
        record.setMedicalHistory("None");

        when(repository.findById(1L)).thenReturn(Optional.of(record));

        var result = service.getRecordById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(repository, times(1)).findById(1L);
        verify(auditService, atLeastOnce()).audit(anyString(), anyString(), anyString()); // optional
    }

    @Test
    void testAddRecord() {
        MedicalRecord record = new MedicalRecord();
        record.setName("John Doe");
        record.setAge(30);
        record.setMedicalHistory("None");

        when(repository.save(record)).thenReturn(record);

        var result = service.addRecord(record);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(repository, times(1)).save(record);
        verify(auditService, atLeastOnce()).audit(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateRecord() {
        MedicalRecord record = new MedicalRecord();
        record.setId(1L);
        record.setName("John Doe");
        record.setAge(30);
        record.setMedicalHistory("None");

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(record)).thenReturn(record);

        var result = service.updateRecord(1L, record);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).save(record);
        verify(auditService, atLeastOnce()).audit(anyString(), anyString(), anyString());
    }

    @Test
    void testDeleteRecord() {
        doNothing().when(repository).deleteById(1L);

        service.deleteRecord(1L);

        verify(repository, times(1)).deleteById(1L);
        verify(auditService, atLeastOnce()).audit(anyString(), anyString(), anyString());
    }
}