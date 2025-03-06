package com.wuzzafni.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserEntity {

    @Id

    private Long id = generateRandomId();


    @NotEmpty
    @Size(min = 4)
    private String name ;

    @NotEmpty
    private String title ;

    @Email
    @Column(unique = true, nullable = false)
    private String email ;

    @NotEmpty
    private String location ;


    @NotEmpty
    private String collegeName  ;

    private List<String>skills;

    @ManyToMany
    @JoinTable(
            name = "user_saved_jobs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    @JsonIgnore
    private List<Job> savedJobs ;

    @OneToOne(cascade = CascadeType.ALL)  // Ensures image is saved properly
    @JoinColumn(name = "Cv_image")
    private Image userCvImage ;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_image_id", referencedColumnName = "id")
    private Image UserImage ;


    private static Long generateRandomId() {
        Random random = new Random();
        return (long) (100000 + random.nextInt(900000));

    }



}
