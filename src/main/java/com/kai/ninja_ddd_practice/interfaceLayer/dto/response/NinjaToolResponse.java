package com.kai.ninja_ddd_practice.interfaceLayer.dto.response;

import com.kai.ninja_ddd_practice.domainLayer.model.entity.NinjaTool;
import com.kai.ninja_ddd_practice.domainLayer.model.valueObject.ToolSpecification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NinjaToolResponse {

    private Long id;
    private String name;
    private String category;
    private ToolSpecification specification;

    public NinjaToolResponse(NinjaTool ninjaTool) {
        this.id = ninjaTool.getId();
        this.name = ninjaTool.getName();
        this.category = ninjaTool.getCategory().name();
        this.specification = ninjaTool.getSpecification();
    }
}
