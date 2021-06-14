package edu.greenriver.sdev.capstone.fsmp.model;

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
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String facilityName;

    /*@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    List<Zone> zones = new ArrayList<>();*/


    private long facilityAdministratorId;
    private String buildingNumber;

    public String toString() {
        return "Facility(id=" + this.getId() + ", facilityName=" + this.getFacilityName() + ", facilityAdministratorId=" + this.getFacilityAdministratorId() + ")";
    }
}

