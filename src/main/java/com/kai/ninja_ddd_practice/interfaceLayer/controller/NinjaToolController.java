package com.kai.ninja_ddd_practice.interfaceLayer.controller;

import com.kai.ninja_ddd_practice.applicationLayer.service.NinjaToolService;
import com.kai.ninja_ddd_practice.domainLayer.model.entity.NinjaTool;
import com.kai.ninja_ddd_practice.interfaceLayer.dto.request.NinjaToolRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.dto.response.NinjaToolResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ninja-tools")
public class NinjaToolController {

    private final NinjaToolService ninjaToolService;

    @Autowired
    public NinjaToolController(NinjaToolService ninjaToolService) {
        this.ninjaToolService = ninjaToolService;
    }

    @Operation(summary = "Create a new NinjaTool")
    @PostMapping
    public ResponseEntity<NinjaToolResponse> createNinjaTool(@RequestBody NinjaToolRequest request) {
        NinjaTool ninjaTool = ninjaToolService.createNinjaTool(request);
        return ResponseEntity.ok(new NinjaToolResponse(ninjaTool));
    }

    @Operation(summary = "Get a NinjaTool by ID")
    @GetMapping("/{id}")
    public ResponseEntity<NinjaToolResponse> getNinjaTool(@PathVariable Long id) {
        NinjaTool ninjaTool = ninjaToolService.getNinjaTool(id);
        return ResponseEntity.ok(new NinjaToolResponse(ninjaTool));
    }

    @Operation(summary = "Update a NinjaTool by ID")
    @PutMapping("/{id}")
    public ResponseEntity<NinjaToolResponse> updateNinjaTool(@PathVariable Long id, @RequestBody NinjaToolRequest request) {
        NinjaTool ninjaTool = ninjaToolService.updateNinjaTool(id, request);
        return ResponseEntity.ok(new NinjaToolResponse(ninjaTool));
    }

    @Operation(summary = "Delete a NinjaTool by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNinjaTool(@PathVariable Long id) {
        ninjaToolService.deleteNinjaTool(id);
        return ResponseEntity.noContent().build();
    }

}
