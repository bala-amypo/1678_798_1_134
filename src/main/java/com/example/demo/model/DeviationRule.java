package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "deviation_rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String surgeryType;

    @NotBlank
    private String ruleCode;

    @NotBlank
    private String parameter;

    @NotNull
    @Positive
    private Integer threshold;

    @NotBlank
    private String severity;

    @Column(nullable = false)
    private Boolean active = true;
}
