package com.duoc.backend.care;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareService {
    private CareRepository careRepository;

    @Autowired
    public CareService(CareRepository careRepository) {
        this.careRepository = careRepository;
    }

    // Obtener todos los servicios de cuidado
    public List<Care> getAllCares() {
        return (List<Care>) careRepository.findAll();
    }

    // Obtener un servicio de cuidado por ID
    public Care getCareById(Long id) {
        return careRepository.findById(id).orElse(null);
    }

    // Guardar un servicio de cuidado
    public Care saveCare(Care care) {
        return careRepository.save(care);
    }

    // Eliminar un servicio de cuidado por ID
    public void deleteCare(Long id) {
        careRepository.deleteById(id);
    }
}