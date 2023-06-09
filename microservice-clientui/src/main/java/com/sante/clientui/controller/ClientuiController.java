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
import com.sante.clientui.service.GatewayService;

import feign.FeignException.FeignClientException;
import jakarta.validation.Valid;
import lombok.Getter;

/**
 * @author trimok
 *
 *         The controller of the aplication
 */
@Controller
@Getter
public class ClientuiController {

    /**
     * gatewayService
     */
    @Autowired
    private GatewayService gatewayService;

    /**
     * Entry point of the application, return list patients view (home)
     * 
     * @param ra    : the redirect attributes
     * @param model : the model
     * @return : string
     */
    @GetMapping("/")
    public String viewHomePage(RedirectAttributes ra, Model model) {

	List<Patient> patients = new ArrayList<>();
	try {
	    patients = gatewayService.getPatients();
	} catch (Exception e) {
	    model.addAttribute("error_get_list_patient", true);
	}

	model.addAttribute("patients", patients);
	return "home";
    }

    /**
     * Return add patient view
     * 
     * @param model :the model
     * @return : add patient view
     */
    @GetMapping("/add")
    public String showNewPatientForm(Model model) {
	Patient patient = new Patient();
	model.addAttribute("patient", patient);
	return "add";
    }

    /**
     * Patient creation, then redirect list patients view
     * 
     * @param patient       : the patient
     * @param bindingResult : bindingResult
     * @param ra            : redirect attributes
     * @param model         : the model
     * @return : redirect redirect list patients view
     */
    @PostMapping("/save")
    public String savePatient(@Valid @ModelAttribute("patient") Patient patient,
	    BindingResult bindingResult, RedirectAttributes ra, Model model) {
	if (bindingResult.hasErrors()) {
	    return "add";
	} else {
	    try {
		gatewayService.createPatient(patient);
	    } catch (Exception e) {
		ra.addAttribute("error_add_patient", true);
		ra.addFlashAttribute("error_add_patient", true);
	    }
	    return "redirect:/";
	}
    }

    /**
     * Patient update, then redirect list patients view
     * 
     * @param patient       : the patient
     * @param bindingResult : bindingResult
     * @param ra            : redirect attributes
     * @param model         : the model
     * @return : redirect application entry point
     */
    @PostMapping("/update")
    public String updatePatient(@Valid @ModelAttribute("patient") Patient patient,
	    BindingResult bindingResult, RedirectAttributes ra, Model model) {
	if (bindingResult.hasErrors()) {
	    return "update";
	} else {
	    try {
		gatewayService.updatePatient(patient);
	    } catch (Exception e) {
		ra.addAttribute("error_update_patient", true);
		ra.addFlashAttribute("error_update_patient", true);
	    }
	    return "redirect:/";
	}
    }

    /**
     * Display the update patient view
     * 
     * @param id    : id patient
     * @param ra    : redirect attributes
     * @param model : the model
     * @return : the update patient view
     */
    @GetMapping("/update/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, RedirectAttributes ra, Model model) {
	Patient patient = null;

	try {
	    patient = gatewayService.getPatient(id);
	    model.addAttribute("patient", patient);
	    return "update";
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}
    }

    /**
     * Delete a patient, then redirect list patient view
     * 
     * @param id    : the patient id
     * @param ra    : the redirect attributes
     * @param model : the model
     * @return : redirect list patient view
     */
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable(value = "id") long id, RedirectAttributes ra, Model model) {
	try {
	    gatewayService.deletePatient(id);
	} catch (Exception e) {
	    ra.addAttribute("error_delete_patient", true);
	    ra.addFlashAttribute("error_delete_patient", true);
	}
	try {
	    gatewayService.deletePatientHistory(id);
	} catch (Exception e) {
	    ra.addAttribute("error_delete_patient_history", true);
	    ra.addFlashAttribute("error_delete_patient_history", true);
	}
	return "redirect:/";
    }

    /**
     * Display add note view
     * 
     * @param id    : patient id
     * @param ra    : redirect attributes
     * @param model : the model
     * @return : the add note view
     */
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
	    patient = gatewayService.getPatient(id);
	    model.addAttribute("patient", patient);
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}

	return "note_add";
    }

    /**
     * Display list notes home page
     * 
     * @param id    : patient id
     * @param ra    : redirect attributes
     * @param model : the model
     * @return : list notes home page
     */
    @GetMapping("/notes/{id}")
    public String viewNotesHomePage(@PathVariable(value = "id") long id, RedirectAttributes ra, Model model) {
	Patient patient = null;

	try {
	    patient = gatewayService.getPatient(id);
	    model.addAttribute("patient", patient);

	    PatientHistory patientHistory = null;

	    try {
		patientHistory = gatewayService.getPatientHistory(patient.getId());
	    } catch (Exception e) {
		FeignClientException fe = (FeignClientException) e;
		String message = fe.getMessage();

		// Acces interdit
		if (message.contains("403")) {
		    ra.addAttribute("error_get_patient_history", true);
		    ra.addFlashAttribute("error_get_patient_history", true);
		    return "redirect:/";
		} else {
		    // Acces autorisé, mais historique absent
		    // S'il n'y a pas d'historique, on revient quand même dans notes_home
		    // avec une liste vide
		    patientHistory = new PatientHistory();
		    patientHistory.setId(patient.getId());
		}
	    }

	    model.addAttribute("patientHistory", patientHistory);
	    return "note_home";
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}
    }

    /**
     * Save note and display list notes view
     * 
     * @param patientHistory : patient history
     * @param note           : the note to add
     * @param bindingResult  : bindingResult
     * @param ra             : redirect attributes
     * @param model          : the model
     * @return : list notes view
     */
    @PostMapping("/note/save")
    public String savePatientHistory(@Valid @ModelAttribute("patientHistory") PatientHistory patientHistory,
	    @Valid @ModelAttribute("note") Note note,
	    BindingResult bindingResult,
	    RedirectAttributes ra, Model model) {
	if (bindingResult.hasErrors()) {
	    return "note_add";
	} else {
	    try {
		patientHistory.getNotes().add(note);
		patientHistory = gatewayService.createUpdatePatientHistory(patientHistory);
	    } catch (Exception e) {
		ra.addAttribute("error_add_patient_history", true);
		ra.addFlashAttribute("error_add_patient_history", true);
	    }
	    model.addAttribute("patientHistory", patientHistory);

	    Patient patient;
	    try {
		patient = gatewayService.getPatient(patientHistory.getId());
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

    /**
     * Delete a note, then redirect list notes view
     * 
     * @param id           : patient id
     * @param creationDate : creation date (note id)
     * @param ra           : redirect attributes
     * @param model        : the model
     * @return : list notes view
     */
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
	    gatewayService.deleteNote(patientHistory);
	} catch (Exception e) {
	    ra.addAttribute("error_delete_note", true);
	    ra.addFlashAttribute("error_delete_note", true);
	}
	return "redirect:/homenotes/" + id;
    }

    /**
     * Display the notes update view
     * 
     * @param id           : patient id
     * @param creationDate creationDate (note id)
     * @param ra           : redirect attributes
     * @param model        : the model
     * @return : notes update view
     */
    @GetMapping("/note/update/{id}/{creationDate}")
    public String showNoteFormForUpdate(@PathVariable(value = "id") long id,
	    @PathVariable(value = "creationDate") LocalDateTime creationDate, RedirectAttributes ra, Model model) {
	Patient patient = null;
	PatientHistory patientHistory = null;

	try {
	    patient = gatewayService.getPatient(id);
	    model.addAttribute("patient", patient);
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}

	try {
	    patientHistory = gatewayService.getPatientHistory(id);
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
	    return "redirect:/homenotes/" + id;
	} else {
	    model.addAttribute("note", optionalNote.get());
	    return "note_update";
	}
    }

    /**
     * Update a note, and redirect list notes view
     * 
     * @param patientHistory : patient history
     * @param note           : note
     * @param bindingResult  : bindingResult
     * @param ra             : redirect attributes
     * @param model          : the model
     * @return : redirect list notes view
     */
    @PostMapping("/note/update")
    public String updateNote(@Valid @ModelAttribute("patientHistory") PatientHistory patientHistory,
	    @Valid @ModelAttribute("note") Note note,
	    BindingResult bindingResult, RedirectAttributes ra, Model model) {
	if (bindingResult.hasErrors()) {
	    return "note_update";
	} else {
	    try {
		PatientHistory patientHistoryToUpdate = new PatientHistory();
		patientHistoryToUpdate.setId(patientHistory.getId());
		patientHistoryToUpdate.getNotes().add(note);
		gatewayService.updateNote(patientHistoryToUpdate);
	    } catch (Exception e) {
		ra.addAttribute("error_update_note", true);
		ra.addFlashAttribute("error_update_note", true);
	    }
	    return "redirect:/homenotes/" + patientHistory.getId();
	}
    }

    /**
     * Calculate the risk, and redirect risk view
     * 
     * @param id    : patient id
     * @param ra    : redirect attributes
     * @param model : the model
     * @return : risk view
     */
    @GetMapping("/risque/{id}")
    public String showRisqueForm(@PathVariable(value = "id") long id, RedirectAttributes ra, Model model) {
	Patient patient = null;
	PatientHistory patientHistory = null;

	try {
	    patient = gatewayService.getPatient(id);
	} catch (Exception e) {
	    ra.addAttribute("error_get_patient", true);
	    ra.addFlashAttribute("error_get_patient", true);
	    return "redirect:/";
	}

	try {
	    patientHistory = gatewayService.getPatientHistory(id);
	} catch (Exception e) {
	    patientHistory = new PatientHistory();
	    patientHistory.setId(patient.getId());
	}

	PatientDao patientDao = new PatientDao(patient, patientHistory);
	Risque risque = null;
	try {
	    risque = gatewayService.getRisque(patientDao);
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
