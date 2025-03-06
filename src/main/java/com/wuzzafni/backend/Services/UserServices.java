package com.wuzzafni.backend.Services;

import com.wuzzafni.backend.Dto.JobDto;
import com.wuzzafni.backend.Dto.SaveJobRequest;
import com.wuzzafni.backend.Dto.SaveUserRequest;
import com.wuzzafni.backend.Dto.userDto;
import com.wuzzafni.backend.Exception.ResourceNotFoundException;
import com.wuzzafni.backend.Model.Image;
import com.wuzzafni.backend.Model.Job;
import com.wuzzafni.backend.Model.UserEntity;
import com.wuzzafni.backend.Repositary.ImageRepository;
import com.wuzzafni.backend.Repositary.JobRepository;
import com.wuzzafni.backend.Repositary.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServices implements IUserServices {


    private final UserRepository userRepository;

    private final JobRepository jobRepository;
    private final ImageRepository imageRepository;

    private final ModelMapper modelMapper;
    private final EmailServices emailService; // ✅ Use EmailService instead of UserController
@Transactional
    public UserEntity cvExtractionNlpModel(String cvText, MultipartFile cvFile){
        // call the nlp api from abdo after ahmed finish

        //get the result of nlp + file and send to saveUserOrRetrieve fun

        //get result and send to controller

        return null ;
    }



    // finedTuned Result from NLP Model ( Non Rest Method )
    @Override
    @Transactional
    public userDto saveUserOrRetrieve(SaveUserRequest request, MultipartFile fileCv){
        // the fined text with the cv screenshot
        UserEntity user = userRepository.findByEmail(request.getEmail());
        if(user == null){
            // Create user,and send email
            UserEntity newUser = new UserEntity() ;
            try {
                newUser.setName(request.getName());
                newUser.setEmail(request.getEmail());
                newUser.setName(request.getName());
                newUser.setCollegeName(request.getCollegeName());
                newUser.setLocation(request.getLocation());
                newUser.setTitle(request.getTitle());
                newUser.setSkills(request.getSkills());

                //cv cvImage
                Image cvImage = new Image();
                cvImage.setImage(new SerialBlob(fileCv.getBytes()));
                cvImage.setFileName(fileCv.getOriginalFilename());
                cvImage.setFileType(fileCv.getContentType());
                imageRepository.save(cvImage);
                String imageUrl = "https://wazafnibackendjava-eqbecshxatgtb0df.uaenorth-01.azurewebsites.net/api/v1/image/";
                cvImage.setDownloadUrl(imageUrl + cvImage.getId());
                imageRepository.save(cvImage);
                newUser.setUserCvImage(cvImage);




                // send email to current user
                 emailService.sendHtmlEmail(newUser.getEmail(), "frontendLink", newUser.getName());
              UserEntity savedUser =   userRepository.save(newUser);
                return convertUserToDto(savedUser);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else{
            //Retrive it

            return convertUserToDto(user) ;


        }

    }

    @Override
    public UserEntity getUser(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("the user with id is not found "));
    }

    @Override
    public Job saveJobByUserId(SaveJobRequest request, Long id ) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        try {
            Job newJob = new Job();
            newJob.setJobLink(request.getJobLink());
            newJob.setJobDate(request.getDate());
            newJob.setImageLink(request.getImageLink());
            newJob.setDescription(request.getDescription());
            newJob.setJobName(request.getTitle());
            newJob.setLocation(request.getLocation());
            newJob.setCompanyName(request.getCompanyName());

            Job savedJob = jobRepository.save(newJob);
            user.getSavedJobs().add(savedJob);
            userRepository.save(user);
            return savedJob;
        } catch (Exception e) {
            throw new RuntimeException("Error saving job: " + e.getMessage());
        }
    }
// single job
    @Override
    public JobDto getSavedJob(Long jobId) {
     Job job =  jobRepository.findById(jobId).orElseThrow(()->new ResourceNotFoundException("the user is not found "));
       return convertToDtoMethod(job);
    }

    //convert job to dto
    @Override
    public JobDto convertToDtoMethod(Job job) {
        JobDto jobDto = modelMapper.map(job, JobDto.class);
        jobDto.setImageLink(job.getImageLink()); // Explicitly setting it
        return jobDto;
    }

    @Override
    public userDto convertUserToDto(UserEntity user) {
   List<Job> jobs =   user.getSavedJobs();
   if(jobs == null){
      return modelMapper.map(user, userDto.class);

   }else {
       List<JobDto> jobDtos = jobs.stream().map(this::convertToDtoMethod).toList();

       userDto userDto = modelMapper.map(user, userDto.class);
       userDto.setSavedJobs(jobDtos);

       return userDto;
   }
    }
    //get User Jobs
    @Override
    public List<JobDto> getUserJobs(Long userId) {
        UserEntity user =  userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("The user is not found"));
        List<Job> jobs = user.getSavedJobs();

        return jobs.stream().map(this::convertToDtoMethod).toList();
    }

    @Override
    public void deleteUserJobs(Long userId, Long jobId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user is not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("The job is not found"));

        if (user.getSavedJobs().contains(job)) {
            user.getSavedJobs().remove(job); // Remove the job from the user's saved jobs
            userRepository.save(user); // Save the updated user entity
        } else {
            throw new ResourceNotFoundException("The job is not found in the user's saved jobs");
        }
    }





}

