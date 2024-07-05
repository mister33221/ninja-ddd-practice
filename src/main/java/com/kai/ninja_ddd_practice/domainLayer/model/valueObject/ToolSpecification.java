package com.kai.ninja_ddd_practice.domainLayer.model.valueObject;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToolSpecification {
    private Double length;
    private Double weight;
    private String material;
}
