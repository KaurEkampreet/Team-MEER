package edu.greenriver.sdev.capstone.fsmp.adapter;

import edu.greenriver.sdev.capstone.fsmp.model.Custodian;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustodianUserDetailAdapter implements UserDetails {
    private final Custodian custodian;

    /**
     * @param custodian I don't even know at this point
     */
    public CustodianUserDetailAdapter(Custodian custodian) {
        this.custodian = custodian;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return custodian.getCustodianAuthorities();
    }

    @Override
    public String getPassword() {
        return custodian.getPassword();
    }

    @Override
    public String getUsername() {
        return custodian.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "UserDetailsAdapter{" +
                "custodian=" + custodian +
                '}';
    }
}
