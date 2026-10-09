package com.duoc.backend.care;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/care")
public class CareController {
    private CareRepository careRepository;

    @Autowired
    public CareController(CareRepository careRepository) {
        this.careRepository = careRepository;
    }

    @GetMapping
    public List<Care> getAllCares() {
        return (List<Care>) careRepository.findAll();
    }

    @GetMapping("/{id}")
    public Care getCareById(@PathVariable Long id) {
        return careRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Care saveCare(@RequestBody CareRequest request) {
        Care service = new Care("Vacuna", 10000);
        service.setName(request.name());
        service.setCost(request.cost());

        return careRepository.save(service);
    }

    @DeleteMapping("/{id}")
    public void deleteCare(@PathVariable Long id) {
        careRepository.deleteById(id);
    }

    public record CareRequest(String name, Double cost) {}

}