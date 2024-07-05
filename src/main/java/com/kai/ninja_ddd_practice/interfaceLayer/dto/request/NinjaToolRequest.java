package com.kai.ninja_ddd_practice.interfaceLayer.dto.request;

import com.kai.ninja_ddd_practice.domainLayer.model.valueObject.ToolSpecification;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NinjaToolRequest {

    private String name;
    private String category;
    private ToolSpecification specification;

}
