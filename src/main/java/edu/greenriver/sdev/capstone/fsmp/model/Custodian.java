package edu.greenriver.sdev.capstone.fsmp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Custodian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=2, max=50)
    private String username;

    private String password;

    @NotBlank
    @Size(min=1, max=50)
    private String firstName;

    @NotBlank
    @Size(min=1,max=50)
    private String lastName;

    private String startDate;
    private String status;
    private String cleaningLevel;

    private long facilityAdministratorId;
    private long zoneId;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "custodian")
    private List<CustodianAuthority> custodianAuthorities = new ArrayList<>();

    public String toString() {

        return "Custodian(id=" + this.getId() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ", facilityAdministratorId=" + this.getFacilityAdministratorId() + ", zoneId=" + this.getZoneId() + ", custodianAuthorities=" + this.getCustodianAuthorities() + ")";

    }
}
