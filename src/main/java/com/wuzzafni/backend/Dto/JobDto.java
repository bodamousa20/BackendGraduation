package com.wuzzafni.backend.Dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class JobDto {
    private Long id ;
    private String jobName ;
    private String companyName ;
    private String location ;
    private String jobDate ;
    private String description ;
    private String jobLink ;
    private String ImageLink ;
}
