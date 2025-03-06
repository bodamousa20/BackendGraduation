package com.wuzzafni.backend.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wuzzafni.backend.Model.Image;
import com.wuzzafni.backend.Model.Job;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class userDto {

    private Long id ;

    private String name ;
    private String title ;
    private String email ;
    private String location ;
    private String collegeName  ;
    private List<String>skills;
    private List<JobDto> savedJobs ;
    private ImageDto userCvImage ;
    private ImageDto UserImage ;

}
