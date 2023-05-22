package com.sante.clientui.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sante.clientui.dao.PatientDao;
import com.sante.clientui.model.Note;
import com.sante.clientui.model.Patient;
import com.sante.clientui.model.PatientHistory;
import com.sante.clientui.model.Risque;
import com.sante.clientui.service.ExpertService;
import com.sante.clientui.service.PatientHistoryService;
import com.sante.clientui.service.PatientService;

import jakarta.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientHistoryService patientHistoryService;

    @Autowired
    private ExpertService expertService;

    @GetMapping("/")
    public String viewHomePage(RedirectAttributes ra, Model model) {
	List<Patient> patients = new ArrayList<>();
	try {
	    patients = patientService.getPatients();
	} catch (Exception e) {
	    model.addAttribute("error_get_list_patient", true);
	}

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
    public String savePatient(@Valid @ModelAttribute("patient") Patient patient, RedirectAttributes ra, Model model,
	    BindingResult bindingResult) {
	if (bindingResult.hasErrors()) {
	    return "add";
	} else {
	    try {
		patientService.createPatient(patient);
	    } catch (Exception e) {
		ra.addAttribute("error_add_patient", true);
		ra.addFlashAttribute("error_add_patient", true);
	    }
	    return "redirect:/";
	}
    }

    @PostMapping("/update")
    public String updatePatient(@Valid @ModelAttribute("patient") Patient patient, RedirectAttributes ra, Model model,
	    BindingResult bindingResult) {
	if (bindingResult.hasErrors()) {
	    return "update";
	} else {
	    try {
		patientService.updatePatient(patient);
	    } catch (Exception e) {
		ra.addAttribute("error_update_patient", true);
		ra.addFlashAttribute("error_update_patient", true);
	    }
	    return "redirect:/";
	}
    }

    @GetMapping("/update/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, RedirectAttributes ra, Model model) {
	Patient patient = null;

	try {
	    patient = patientService.getPatient(id);
	    model.addAttribute("patient", patient);
	    return "update";
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable(value = "id") long id, RedirectAttributes ra, Model model) {
	try {
	    patientService.deletePatient(id);
	} catch (Exception e) {
	    ra.addAttribute("error_delete_patient", true);
	    ra.addFlashAttribute("error_delete_patient", true);
	}
	return "redirect:/";
    }

    @GetMapping("/note/add/{id}")
    public String showNewPatientHistoryForm(@PathVariable(value = "id") long id,
	    RedirectAttributes ra, Model model) {
	Note note = new Note();
	model.addAttribute("note", note);

	PatientHistory patientHistory = new PatientHistory();
	patientHistory.setId(id);
	model.addAttribute("patientHistory", patientHistory);

	Patient patient = null;
	try {
	    patient = patientService.getPatient(id);
	    model.addAttribute("patient", patient);
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}

	return "note_add";
    }

    @GetMapping("/notes/{id}")
    public String viewNotesHomePage(@PathVariable(value = "id") long id, RedirectAttributes ra, Model model) {
	Patient patient = null;

	try {
	    patient = patientService.getPatient(id);
	    model.addAttribute("patient", patient);

	    PatientHistory patientHistory = null;

	    try {
		patientHistory = patientHistoryService.getPatientHistory(patient.getId());
	    } catch (Exception e) {
		// S'il n'y a pas d'historique, on revient quand même dans notes_home
		// avec une liste vide
		patientHistory = new PatientHistory();
		patientHistory.setId(patient.getId());
	    }

	    model.addAttribute("patientHistory", patientHistory);
	    return "note_home";
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}
    }

    @PostMapping("/note/save")
    public String savePatientHistory(@Valid @ModelAttribute("patientHistory") PatientHistory patientHistory,
	    @Valid @ModelAttribute("note") Note note,
	    RedirectAttributes ra, Model model,
	    BindingResult bindingResult) {
	if (bindingResult.hasErrors()) {
	    return "note_add";
	} else {
	    try {
		patientHistory.getNotes().add(note);
		patientHistory = patientHistoryService.createUpdatePatientHistory(patientHistory);
	    } catch (Exception e) {
		ra.addAttribute("error_add_patient_history", true);
		ra.addFlashAttribute("error_add_patient_history", true);
	    }
	    model.addAttribute("patientHistory", patientHistory);

	    Patient patient;
	    try {
		patient = patientService.getPatient(patientHistory.getId());
		model.addAttribute("patient", patient);
	    } catch (Exception e) {
		ra.addAttribute("error_get_patient", true);
		ra.addFlashAttribute("error_get_patient", true);
		return "redirect:/";
	    }
	    model.addAttribute("patient", patient);
	    return "note_home";
	}
    }

    @GetMapping("/note/delete/{id}/{creationDate}")
    public String deleteNote(@PathVariable(value = "id") long id,
	    @PathVariable(value = "creationDate") LocalDateTime creationDate,
	    RedirectAttributes ra, Model model) {

	PatientHistory patientHistory = new PatientHistory();
	patientHistory.setId(id);
	Note note = new Note();
	note.setCreationDate(creationDate);
	patientHistory.getNotes().add(note);

	try {
	    patientHistoryService.deleteNote(patientHistory);
	} catch (Exception e) {
	    ra.addAttribute("error_delete_note", true);
	    ra.addFlashAttribute("error_delete_note", true);
	}
	return "redirect:/notes/" + id;
    }

    @GetMapping("/note/update/{id}/{creationDate}")
    public String showNoteFormForUpdate(@PathVariable(value = "id") long id,
	    @PathVariable(value = "creationDate") LocalDateTime creationDate, RedirectAttributes ra, Model model) {
	Patient patient = null;
	PatientHistory patientHistory = null;

	try {
	    patient = patientService.getPatient(id);
	    model.addAttribute("patient", patient);
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}

	try {
	    patientHistory = patientHistoryService.getPatientHistory(id);
	    model.addAttribute("patientHistory", patientHistory);
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient_history", true);
	    ra.addFlashAttribute("error_get_patient_history", true);
	    return "redirect:/";
	}

	Optional<Note> optionalNote = patientHistory.getNotes().stream()
		.filter(n -> n.getCreationDate().equals(creationDate))
		.findFirst();

	if (optionalNote.isEmpty()) {
	    ra.addAttribute("error_get_note", true);
	    ra.addFlashAttribute("error_get_note", true);
	    return "redirect:/notes/" + id;
	} else {
	    model.addAttribute("note", optionalNote.get());
	    return "note_update";
	}
    }

    @PostMapping("/note/update")
    public String updateNote(@Valid @ModelAttribute("patientHistory") PatientHistory patientHistory,
	    @Valid @ModelAttribute("note") Note note, RedirectAttributes ra, Model model,
	    BindingResult bindingResult) {
	if (bindingResult.hasErrors()) {
	    return "note_update";
	} else {
	    try {
		PatientHistory patientHistoryToUpdate = new PatientHistory();
		patientHistoryToUpdate.setId(patientHistory.getId());
		patientHistoryToUpdate.getNotes().add(note);
		patientHistoryService.updateNote(patientHistoryToUpdate);
	    } catch (Exception e) {
		ra.addAttribute("error_update_note", true);
		ra.addFlashAttribute("error_update_note", true);
	    }
	    return "redirect:/notes/" + patientHistory.getId();
	}
    }

    @GetMapping("/risque/{id}")
    public String showRisqueForm(@PathVariable(value = "id") long id, RedirectAttributes ra, Model model) {
	Patient patient = null;
	PatientHistory patientHistory = null;

	try {
	    patient = patientService.getPatient(id);
	    patientHistory = patientHistoryService.getPatientHistory(id);
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}

	PatientDao patientDao = new PatientDao(patient, patientHistory);
	Risque risque = null;
	try {
	    risque = expertService.getRisque(patientDao);
	} catch (Exception e) {
	    ra.addAttribute("error_get_risque", true);
	    ra.addFlashAttribute("error_get_risque", true);
	    return "redirect:/";
	}

	model.addAttribute("patientDao", patientDao);
	model.addAttribute("risque", risque);

	return "risque";
    }
}
