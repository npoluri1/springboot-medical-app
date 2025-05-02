package com.example.controller;


import com.example.model.MedicalRecord;
import com.example.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MedicalRecordControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicalRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        MedicalRecord record = new MedicalRecord();
        record.setName("John Doe");
        record.setAge(30);
        record.setMedicalHistory("None");

        repository.save(record);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetAllRecords() throws Exception {
        mockMvc.perform(get("/api/medical-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetRecordById() throws Exception {
        var record = repository.findAll().get(0);

        mockMvc.perform(get("/api/medical-records/{id}", record.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testAddRecord() throws Exception {
        String newRecord = """
                {
                    "name": "Jane Smith",
                    "age": 25,
                    "medicalHistory": "Asthma"
                }
                """;

        mockMvc.perform(post("/api/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newRecord))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jane Smith")));

        // Verify the record is added
        var records = repository.findAll();
        assertEquals(2, records.size());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateRecord() throws Exception {
        var record = repository.findAll().get(0);

        String updatedRecord = """
                {
                    "name": "John Updated",
                    "age": 35,
                    "medicalHistory": "Updated History"
                }
                """;

        mockMvc.perform(put("/api/medical-records/{id}", record.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRecord))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Updated")));

        // Verify the record is updated
        var updated = repository.findById(record.getId()).get();
        assertEquals("John Updated", updated.getName());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteRecord() throws Exception {
        var record = repository.findAll().get(0);

        mockMvc.perform(delete("/api/medical-records/{id}", record.getId()))
                .andExpect(status().isOk());

        // Verify the record is deleted
        assertEquals(0, repository.findAll().size());
    }
}