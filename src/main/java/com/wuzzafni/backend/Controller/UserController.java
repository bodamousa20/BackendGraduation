package com.wuzzafni.backend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuzzafni.backend.Dto.SaveJobRequest;
import com.wuzzafni.backend.Dto.SaveUserRequest;
import com.wuzzafni.backend.Model.Job;
import com.wuzzafni.backend.Model.UserEntity;
import com.wuzzafni.backend.Response.ApiResponse;
import com.wuzzafni.backend.Services.UserServices;
import com.wuzzafni.backend.Services.YouTubeService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/user")
@AllArgsConstructor
public class UserController {
    private final UserServices userServices ;
    private final YouTubeService youTubeService ;

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> saveUser(
          // @RequestParam("image") String CvText
            @ModelAttribute SaveUserRequest request,
            @RequestParam("cvImage") MultipartFile image
    ) {
        try {

            return ResponseEntity.ok(new ApiResponse("success",
                    userServices.saveUserOrRetrieve(request,image)
                   // userServices.cvExtractionNlpModel(CvText,image)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getYoutubePlaylist/{query}")
    public ResponseEntity<ApiResponse> getYoutubePlaylist(@PathVariable String query) throws IOException {

        try {
            List<Map<String, Object>> youtubeJson =  youTubeService.searchYouTube(query,10);
            return ResponseEntity.ok(new ApiResponse("success",youtubeJson));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse>getUser(@PathVariable Long id ){
        try {
          UserEntity user =   userServices.getUser(id);
          userServices.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("success", userServices.convertUserToDto(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }
    }
   @PostMapping("/saveJob")
    public ResponseEntity<ApiResponse>saveJobByUser(@ModelAttribute SaveJobRequest request ,@RequestParam("userId") Long id){
        try {
           Job job = userServices.saveJobByUserId(request,id);
            return ResponseEntity.ok(new ApiResponse("success",userServices.convertToDtoMethod(job) ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }
    }


    @GetMapping("/jobs/{userId}")
    public ResponseEntity<ApiResponse>getUserJobs(@PathVariable Long userId){
        try {
            return ResponseEntity.ok(new ApiResponse("success",userServices.getUserJobs(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed",null));

        }
    }

    @DeleteMapping("{userId}/delete-job/{jobId}")
    public ResponseEntity<ApiResponse>deleteJob(@PathVariable Long userId,@PathVariable Long jobId ){
        try {
            userServices.deleteUserJobs(userId,jobId);
            return ResponseEntity.ok(new ApiResponse("deleted successfully",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed",null));

        }
    }






}
