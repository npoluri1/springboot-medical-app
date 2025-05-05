package com.example.service;

import com.example.model.MedicalRecord;
import com.example.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private AuditService auditService;

    @InjectMocks
    private MedicalRecordService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRecords() {
        MedicalRecord record1 = new MedicalRecord();
        record1.setId(1L);
        record1.setName("John Doe");
        record1.setAge(30);
        record1.setMedicalHistory("None");
        record1.setDeleted(false);

        MedicalRecord record2 = new MedicalRecord();
        record2.setId(2L);
        record2.setName("Jane Smith");
        record2.setAge(25);
        record2.setMedicalHistory("Asthma");
        record2.setDeleted(false);

        when(repository.findByDeleted(false)).thenReturn(Arrays.asList(record1, record2));

        var records = service.getAllRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
        verify(repository, times(1)).findByDeleted(false);
        verify(auditService, atLeastOnce()).audit(anyString(), eq("GET_ALL"), anyString());
    }

    @Test
    void testGetRecordById() {
        MedicalRecord record = new MedicalRecord();
        record.setId(1L);
        record.setName("John Doe");
        record.setAge(30);
        record.setMedicalHistory("None");
        record.setDeleted(false);

        when(repository.findByIdAndDeleted(1L, false)).thenReturn(Optional.of(record));

        var result = service.getRecordById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(repository, times(1)).findByIdAndDeleted(1L, false);
        verify(auditService, atLeastOnce()).audit(anyString(), eq("GET_BY_ID"), anyString());
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
        verify(auditService, atLeastOnce()).audit(anyString(), eq("ADD"), anyString());
    }

    @Test
    void testUpdateRecord() {
        MedicalRecord record = new MedicalRecord();
        record.setId(1L);
        record.setName("John Doe");
        record.setAge(30);
        record.setMedicalHistory("None");
        record.setDeleted(false);

        when(repository.existsByIdAndDeleted(1L, false)).thenReturn(true);
        when(repository.save(record)).thenReturn(record);

        var result = service.updateRecord(1L, record);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(repository, times(1)).existsByIdAndDeleted(1L, false);
        verify(repository, times(1)).save(record);
        verify(auditService, atLeastOnce()).audit(anyString(), eq("UPDATE"), anyString());
    }

    @Test
    void testUpdateNonExistentRecord() {
        MedicalRecord record = new MedicalRecord();
        record.setId(1L);
        record.setName("John Doe");
        record.setAge(30);
        record.setMedicalHistory("None");

        when(repository.existsByIdAndDeleted(1L, false)).thenReturn(false);

        var result = service.updateRecord(1L, record);

        assertNull(result);
        verify(repository, times(1)).existsByIdAndDeleted(1L, false);
        verify(auditService, atLeastOnce()).audit(anyString(), eq("UPDATE_FAIL"), anyString());
    }

    @Test
    void testDeleteRecord() {
        MedicalRecord record = new MedicalRecord();
        record.setId(1L);
        record.setDeleted(false);

        when(repository.findByIdAndDeleted(1L, false)).thenReturn(Optional.of(record));
        when(repository.save(any(MedicalRecord.class))).thenReturn(record);

        service.deleteRecord(1L);

        assertTrue(record.isDeleted());
        verify(repository, times(1)).findByIdAndDeleted(1L, false);
        verify(repository, times(1)).save(record);
        verify(auditService, atLeastOnce()).audit(anyString(), eq("DELETE"), anyString());
    }

    @Test
    void testDeleteNonExistentRecord() {
        when(repository.findByIdAndDeleted(1L, false)).thenReturn(Optional.empty());

        service.deleteRecord(1L);

        verify(repository, times(1)).findByIdAndDeleted(1L, false);
        verify(repository, never()).save(any());
        verify(auditService, atLeastOnce()).audit(anyString(), eq("DELETE_FAIL"), anyString());
    }
}