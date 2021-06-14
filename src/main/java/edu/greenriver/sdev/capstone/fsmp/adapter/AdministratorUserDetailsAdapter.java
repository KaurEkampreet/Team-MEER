package edu.greenriver.sdev.capstone.fsmp.adapter;

import edu.greenriver.sdev.capstone.fsmp.model.Administrator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 *
 * @author Robert Cox
 * @version 1.0
 */
public class AdministratorUserDetailsAdapter implements UserDetails {

    private final Administrator administrator;

    /**
     * @param administrator I don't even know at this point
     */
    public AdministratorUserDetailsAdapter(Administrator administrator) {
        this.administrator = administrator;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return administrator.getAuthorities();
    }

    @Override
    public String getPassword() {
        return administrator.getPassword();
    }

    @Override
    public String getUsername() {
        return administrator.getUsername();
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
                "administrator=" + administrator +
                '}';
    }
}
