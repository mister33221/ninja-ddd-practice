package com.kai.ninja_ddd_practice.domainLayer.model.entity;

import com.kai.ninja_ddd_practice.domainLayer.model.valueObject.ToolCategory;
import com.kai.ninja_ddd_practice.domainLayer.model.valueObject.ToolSpecification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 我們不使用 Lobok 的 @Data，因為我們不希望這個類別有 equals 和 hashCode 方法
 * 1. 當這個物件是個 Entity時，通常我們是會拿他的 ID 來比較，reference address
 * 2. @Data 會把所有的 field 都加上 setter 和 getter，這樣可能會破壞我們想要的客製封裝結構
 */


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ninja_tool")
public class NinjaTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ToolCategory category;

    @Embedded
    private ToolSpecification specification;


    public NinjaTool(String name, ToolCategory toolCategory, ToolSpecification specification) {
        this.name = name;
        this.category = toolCategory;
        this.specification = specification;
    }

}
