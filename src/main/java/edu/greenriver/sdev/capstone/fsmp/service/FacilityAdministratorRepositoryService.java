package edu.greenriver.sdev.capstone.fsmp.service;

import edu.greenriver.sdev.capstone.fsmp.adapter.FacilityAdministratorUserDetailsAdapter;
import edu.greenriver.sdev.capstone.fsmp.model.*;
import edu.greenriver.sdev.capstone.fsmp.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FacilityAdministratorRepositoryService implements UserDetailsService {

    FacilityAdministratorRepository facilityAdministratorRepository;
    FacilityAdministratorAuthorityRepository facilityAdministratorAuthorityRepository;
    CustodianRepository custodianRepository;
    FacilityRepository facilityRepository;
    FacilityRepositoryService facilityRepositoryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<FacilityAdministrator> administrator = facilityAdministratorRepository.getFacilityAdministratorByUsername(username);
        if (administrator.isPresent()) {
            return new FacilityAdministratorUserDetailsAdapter(administrator.get());
        } else {
            throw new UsernameNotFoundException("Username or Password is incorrect");
        }
    }

    public FacilityAdministrator registerFacAdmin(FacilityAdministrator facAdmin) {

        facAdmin.setPassword(
                new BCryptPasswordEncoder()
                        .encode(facAdmin.getPassword()));

        FacilityAdministratorAuthority authority = FacilityAdministratorAuthority.builder()
                .facilityAuthority("ROLE_FACILITY_ADMIN")
                .facilityAdministrator(facAdmin)
                .build();

        facAdmin.getFacilityAuthorities().add(authority);

        authority = FacilityAdministratorAuthority.builder()
                .facilityAuthority("ROLE_USER")
                .facilityAdministrator(facAdmin)
                .build();

        facAdmin.getFacilityAuthorities().add(authority);

        facAdmin = facilityAdministratorRepository.save(facAdmin);

        if (facAdmin.getId() != null) {
            facilityRepositoryService.createInitialFacility(facAdmin.getId());
            return facAdmin;
        } else {
            return null;
        }
    }

    public boolean facilityAdminUsernameExists(String username) {
        return facilityAdministratorRepository.getFacilityAdministratorByUsername(username).isPresent();
    }

    public List<FacilityAdministrator> getAll() {
        return facilityAdministratorRepository.findAll();
    }

    public boolean updateFacAdmin(FacilityAdministrator facilityAdministrator) {

        if (facilityAdministrator!=null) {
            if (facilityAdministrator.getId() == null || facilityAdministrator.getUsername() == null ||
                    facilityAdministrator.getFirstName() == null || facilityAdministrator.getLastName() == null)
                {return false;}

            int changes = 0;

            if (facilityAdministratorRepository.updateFacilityAdministratorUsername(facilityAdministrator.getId(), facilityAdministrator.getUsername()) > 0) {
                changes+=1;
            }

            if (facilityAdministratorRepository.updateFacilityAdministratorFirstName(facilityAdministrator.getId(), facilityAdministrator.getFirstName()) > 0) {
                changes+=1;
            }

            if (facilityAdministratorRepository.updateFacilityAdministratorLastName(facilityAdministrator.getId(), facilityAdministrator.getLastName()) > 0) {
                changes+=1;
            }

            return changes > 0;
        }

        return false;
    }

    @Transactional
    public boolean deleteFacilityAdmin(FacilityAdministrator facilityAdministrator) {

        List<Facility> facilities = facilityRepository.getAllByFacilityAdministratorId(facilityAdministrator.getId());

        for (Facility facility : facilities) {
            facilityRepositoryService.deleteFacility(facility.getId());
        }


        List<Custodian> custodians = custodianRepository.getCustodiansByFacilityAdministratorId(facilityAdministrator.getId());
        for (Custodian custodian: custodians) {
            custodianRepository.delete(custodian);
        }

        facilityAdministratorRepository.deleteById(facilityAdministrator.getId());
        return facilityAdministratorRepository.findById(facilityAdministrator.getId()).isEmpty();
    }

    public boolean facilityAdminUsernameChanged(long id, String username) {
        Optional<FacilityAdministrator> facilityAdministrator = facilityAdministratorRepository.findById(id);

        if (facilityAdministrator.isPresent()) {
            return !facilityAdministrator.get().getUsername().equals(username);
        }
        return false;
    }
}
