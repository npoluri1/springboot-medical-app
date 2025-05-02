package com.example.controller;

import com.example.model.MedicalRecord;
import com.example.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private MedicalRecordService service;

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }

    // Home page (list of all medical records)
    @GetMapping("/")
    public String home(Model model) {
        logger.info("User '{}' accessed home page (medical records list)", getCurrentUsername());
        model.addAttribute("records", service.getAllRecords());
        return "index"; // Points to "index.xhtml" in src/main/resources/templates
    }

    // Add Record Form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("User '{}' opened add medical record form", getCurrentUsername());
        model.addAttribute("medicalRecord", new MedicalRecord());
        return "form"; // Points to "form.xhtml" in src/main/resources/templates
    }

    // Edit Record Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("User '{}' opened edit form for medical record ID={}", getCurrentUsername(), id);
        model.addAttribute("medicalRecord", service.getRecordById(id));
        return "form"; // Reuse "form.xhtml" for editing
    }

    // Save Record (Add or Edit)
    @PostMapping("/save")
    public String saveRecord(@Valid MedicalRecord record, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("User '{}' submitted invalid medical record form: {}", getCurrentUsername(), result.getAllErrors());
            // Return to the form with validation errors
            return "form"; // Points to "form.xhtml"
        }
        logger.info("User '{}' is saving medical record: {}", getCurrentUsername(), record);
        service.addRecord(record);
        return "redirect:/"; // Redirect back to the home page after saving
    }
    // This enables form-based DELETE, using HiddenHttpMethodFilter (_method=delete)
    @DeleteMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id) {
        logger.info("User '{}' is deleting medical record ID={} (UI)", getCurrentUsername(), id);
        service.deleteRecord(id);
        return "redirect:/";
    }
}