package com.example.demo.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "deviation_rules", uniqueConstraints = {
    @UniqueConstraint(columnNames = "ruleCode")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviationRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String ruleCode;
    
    @Column(nullable = false)
    private String parameter; // PAIN, MOBILITY, FATIGUE
    
    @Column(nullable = false)
    private Integer threshold;
    
    @Column(nullable = false)
    private String severity; // LOW, MEDIUM, HIGH
    
    @Builder.Default
    private Boolean active = true;
}