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
public class CustodianAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long custodianAuthId;
    private  String custodianAuthority;

    @ManyToOne
    private Custodian custodian;

    @Override
    public String getAuthority() {
        return custodianAuthority;
    }


    public String toString() {
        return "CustodianAuthority(custodianAuthId=" + this.getCustodianAuthId() + ", custodianAuthority=" + this.getCustodianAuthority() + ", custodian=" + this.getCustodian().getId() + ")";
    }
}

