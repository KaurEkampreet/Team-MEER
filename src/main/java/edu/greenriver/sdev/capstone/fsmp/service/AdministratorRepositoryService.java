package edu.greenriver.sdev.capstone.fsmp.service;

import edu.greenriver.sdev.capstone.fsmp.adapter.AdministratorUserDetailsAdapter;
import edu.greenriver.sdev.capstone.fsmp.model.Administrator;
import edu.greenriver.sdev.capstone.fsmp.repository.AdministratorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdministratorRepositoryService  implements UserDetailsService {

    AdministratorRepository administratorRepository;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Administrator> administrator = administratorRepository.getAdministratorByUsername(username);
        if (administrator.isPresent()) {
            return new AdministratorUserDetailsAdapter(administrator.get());
        } else {
            throw new UsernameNotFoundException("Username or Password is incorrect");
        }
    }
}
