package edu.greenriver.sdev.capstone.fsmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Component
public class FacilityAdministrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=2, max=30)
    private String username;

    private String password;

    @NotBlank
    @Size(min=1, max=30)
    private String firstName;

    @NotBlank
    @Size(min=1,max=30)
    private String lastName;

    /*@OneToMany(mappedBy = "facilityAdministrator", cascade = CascadeType.ALL)
    *//*@Transient*//*
    private List<Facility> facilities = new ArrayList<>();*/

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "facilityAdministrator")
    private List<FacilityAdministratorAuthority> facilityAuthorities = new ArrayList<>();
}
