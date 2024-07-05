package com.kai.ninja_ddd_practice.infrastructureLayer.persistence;

import com.kai.ninja_ddd_practice.domainLayer.model.entity.NinjaTool;
import com.kai.ninja_ddd_practice.domainLayer.repository.NinjaToolRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NinjaToolJpaRepository extends JpaRepository<NinjaTool, Long>, NinjaToolRepository {
}
