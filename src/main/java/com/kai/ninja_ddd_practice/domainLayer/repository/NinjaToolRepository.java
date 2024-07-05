package com.kai.ninja_ddd_practice.domainLayer.repository;

import com.kai.ninja_ddd_practice.domainLayer.model.entity.NinjaTool;

import java.util.Optional;

public interface NinjaToolRepository {
    NinjaTool save(NinjaTool ninjaTool);
    Optional<NinjaTool> findById(Long id);
    void deleteById(Long id);
}
