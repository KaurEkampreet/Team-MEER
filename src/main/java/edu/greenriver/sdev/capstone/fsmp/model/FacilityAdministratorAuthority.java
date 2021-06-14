package edu.greenriver.sdev.capstone.fsmp.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityAdministratorAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long facAuthId;
    private  String facilityAuthority;

    @ManyToOne
    private FacilityAdministrator facilityAdministrator;

    @Override
    public String getAuthority() {
        return facilityAuthority;
    }

    public String toString() {
        return "FacilityAdministratorAuthority(facAuthId=" + this.getFacAuthId() + ", facilityAuthority=" + this.getFacilityAuthority() + ", facilityAdministrator=" + this.getFacilityAdministrator().getId() + ")";
    }
}
