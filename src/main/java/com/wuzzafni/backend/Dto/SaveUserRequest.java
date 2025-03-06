package com.wuzzafni.backend.Dto;

import com.wuzzafni.backend.Model.Image;
import lombok.Data;

import java.util.List;

@Data
public class SaveUserRequest {
    private String name ;
    private String title ;

    private String email ;
    private String location ;
    private String collegeName  ;
    List<String> skills ;

}
