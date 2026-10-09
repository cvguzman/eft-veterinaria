package com.duoc.backend.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    private PatientService patientService;

    public record PatientRequest(
            String name,
            String species,
            String breed,
            int age,
            String owner
    ) {}

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GetMapping(value = "/register", produces = MediaType.TEXT_HTML_VALUE)
    public String greetings(
            @RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello {" + HtmlUtils.htmlEscape(name) + "}";
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return (List<Patient>) patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PostMapping
    public Patient savePatient(@RequestBody PatientRequest request) {
        Patient patient = new Patient();
        patient.setName(request.name());
        patient.setSpecies(request.species());
        patient.setBreed(request.breed());
        patient.setAge(request.age());
        patient.setOwner(request.owner());

        return patientService.savePatient(patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}