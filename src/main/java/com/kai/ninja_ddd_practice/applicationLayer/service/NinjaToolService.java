package com.kai.ninja_ddd_practice.applicationLayer.service;

import com.kai.ninja_ddd_practice.domainLayer.model.entity.NinjaTool;
import com.kai.ninja_ddd_practice.domainLayer.model.valueObject.ToolCategory;
import com.kai.ninja_ddd_practice.domainLayer.repository.NinjaToolRepository;
import com.kai.ninja_ddd_practice.interfaceLayer.dto.request.NinjaToolRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NinjaToolService {

    private final NinjaToolRepository ninjaToolRepository;

    @Autowired
    public NinjaToolService(NinjaToolRepository ninjaToolRepository) {
        this.ninjaToolRepository = ninjaToolRepository;
    }

    public NinjaTool createNinjaTool(NinjaToolRequest request) {
        NinjaTool ninjaTool = new NinjaTool(
                request.getName(),
                ToolCategory.valueOf(request.getCategory()),
                request.getSpecification()
        );
        return ninjaToolRepository.save(ninjaTool);
    }

    public NinjaTool getNinjaTool(Long id) {
        return ninjaToolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NinjaTool not found"));
    }

    public NinjaTool updateNinjaTool(Long id, NinjaToolRequest request) {
        NinjaTool ninjaTool = getNinjaTool(id);
        ninjaTool.setName(request.getName());
        ninjaTool.setCategory(ToolCategory.valueOf(request.getCategory()));
        ninjaTool.setSpecification(request.getSpecification());
        return ninjaToolRepository.save(ninjaTool);
    }

    public void deleteNinjaTool(Long id) {
        ninjaToolRepository.deleteById(id);
    }

}
