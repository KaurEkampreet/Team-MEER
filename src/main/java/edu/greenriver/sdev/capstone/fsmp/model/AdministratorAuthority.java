package edu.greenriver.sdev.capstone.fsmp.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @author Robert Cox
 * @version 1.0
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministratorAuthority implements GrantedAuthority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long authId;
    private  String authority;

    @ManyToOne
    private Administrator administrator;

    @Override
    public String getAuthority() {
        return authority;
    }

    public String toString() {
        return "AdministratorAuthority(authId=" + this.getAuthId() + ", authority=" + this.getAuthority() + ", administrator=" + this.getAdministrator().getId() + ")";
    }
}
