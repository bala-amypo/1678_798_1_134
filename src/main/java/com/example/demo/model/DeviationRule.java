package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeviationRule {

    private Long id;
    private String ruleCode;
    private String parameter;
    private Integer threshold;
    private String severity;
    private Boolean active = true;
}
