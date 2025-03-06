package com.wuzzafni.backend.Services;

import com.wuzzafni.backend.Dto.JobDto;
import com.wuzzafni.backend.Dto.SaveJobRequest;
import com.wuzzafni.backend.Dto.SaveUserRequest;
import com.wuzzafni.backend.Dto.userDto;
import com.wuzzafni.backend.Model.Job;
import com.wuzzafni.backend.Model.UserEntity;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IUserServices {

userDto convertUserToDto(UserEntity user);
 UserEntity cvExtractionNlpModel(String cvText, MultipartFile file);

 userDto saveUserOrRetrieve(SaveUserRequest request,MultipartFile file ) ;

    UserEntity getUser(Long id );

 Job saveJobByUserId(Job request,Long id );

    JobDto getSavedJob(Long jobId);

     JobDto convertToDtoMethod(Job job);

    List <JobDto> getUserJobs(Long userId);

    void deleteUserJobs(Long userId,Long jobId);

}
