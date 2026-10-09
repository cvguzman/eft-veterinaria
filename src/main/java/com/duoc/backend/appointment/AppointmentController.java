package com.duoc.backend.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return (List<Appointment>) appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    public Appointment saveAppointment(@RequestBody AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setDate(request.date());
        appointment.setTime(request.time());
        appointment.setReason(request.reason());
        appointment.setVeterinarian(request.veterinarian());

        return appointmentService.saveAppointment(appointment);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }

    public record AppointmentRequest(
            LocalDate date,
            LocalTime time,
            String reason,
            String veterinarian
    ) {}
}
