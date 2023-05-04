package com.sante.clientui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sante.clientui.model.Patient;
import com.sante.clientui.service.PatientService;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
	List<Patient> patients = patientService.getPatients();
	model.addAttribute("patients", patients);
	return "home";
    }

    @GetMapping("/add")
    public String showNewPatientForm(Model model) {
	Patient patient = new Patient();
	model.addAttribute("patient", patient);
	return "add";
    }

    @PostMapping("/save")
    public String savePatient(@ModelAttribute("patient") Patient patient) {
	patientService.createPatient(patient);
	return "redirect:/";
    }

    @PostMapping("/update")
    public String updatePatient(@ModelAttribute("patient") Patient patient) {
	patientService.updatePatient(patient);
	return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
	Patient patient = patientService.getPatient(id);
	model.addAttribute("patient", patient);
	return "update";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable(value = "id") long id) {
	patientService.deletePatient(id);
	return "redirect:/";
    }
}
