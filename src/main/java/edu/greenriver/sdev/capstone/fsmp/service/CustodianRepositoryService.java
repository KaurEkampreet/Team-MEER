package edu.greenriver.sdev.capstone.fsmp.service;

import edu.greenriver.sdev.capstone.fsmp.adapter.CustodianUserDetailAdapter;
import edu.greenriver.sdev.capstone.fsmp.controllers.CustodianController;
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
public class CustodianRepositoryService implements UserDetailsService {

    CustodianRepository custodianRepository;
    FacilityAdministratorRepository facilityAdministratorRepository;
    FacilityRepository facilityRepository;
    ZoneRepository zoneRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Custodian> custodian = custodianRepository.getCustodianByUsername(username);
        if (custodian.isPresent()) {
            return new CustodianUserDetailAdapter(custodian.get());
        } else {
            throw new UsernameNotFoundException("Username or Password is incorrect");
        }
    }

    public Custodian registerCustodian(Long facilityAdminID, Custodian custodian) {

        custodian.setPassword(
                new BCryptPasswordEncoder()
                        .encode(custodian.getPassword()));

        CustodianAuthority authority = CustodianAuthority.builder()
                .custodianAuthority("ROLE_CUSTODIAN")
                .custodian(custodian)
                .build();

        custodian.getCustodianAuthorities().add(authority);

        authority = CustodianAuthority.builder()
                .custodianAuthority("ROLE_USER")
                .custodian(custodian)
                .build();

        custodian.getCustodianAuthorities().add(authority);

        custodian.setFacilityAdministratorId(facilityAdminID);
        custodian = custodianRepository.save(custodian);

        if (custodian.getId() != null) {
            addToUnassignedZone(facilityAdminID, custodian);
            return custodian;
        } else {
            return null;
        }
    }

    public boolean custodianUsernameExists(String username) {
        return custodianRepository.getCustodianByUsername(username).isPresent();
    }

    public List<Custodian> custodiansByFacilityAdminID(Long id) {
        return custodianRepository.getCustodiansByFacilityAdministratorId(id);
    }

    public Custodian getCustodianById(Long id) {
        return (Custodian) custodianRepository
                .getCustodianById(id)
                .orElse(null);
    }

    @Transactional
    public void addToUnassignedZone(Long facilityAdminID, Custodian custodian) {
        Facility unassignedFacility = facilityRepository.getFacilityByFacilityAdministratorIdAndAndFacilityName(facilityAdminID, "Unassigned");

        Zone unassignedZone = zoneRepository.getZoneByFacilityIdAndAndZoneName(unassignedFacility.getId(), "Unassigned");

        custodian.setZoneId(unassignedZone.getId());
        custodianRepository.save(custodian);
    }

    public List<Custodian> allCustodians() {
        return custodianRepository.findAll();
    }

    public Custodian getCustodianByUsername(String loggedInUsername) {
        Optional<Custodian> custodianOptional = custodianRepository.getCustodianByUsername(loggedInUsername);

        if (custodianOptional.isPresent()) {
            return custodianOptional.get();
        }
        return null;
    }
    public Optional<FacilityAdministrator> getFacilityAdminByUsername(String username){
        return facilityAdministratorRepository.getFacilityAdministratorByUsername(username);
    }

    public boolean custodianUsernameChanged(Long custodianId, String username) {
        Optional<Custodian> custodian = custodianRepository.findById(custodianId);
        
        if (custodian.isPresent()) {
            return !custodian.get().getUsername().equals(username);
        }
        return false;
    }

    public boolean updateCustodian(Custodian custodian) {

        if (custodian!=null) {
            if (custodian.getId() == null || custodian.getUsername() == null ||
                    custodian.getFirstName() == null || custodian.getLastName() == null)
            {return false;}

            int changes = 0;

            if (custodianRepository.updateCustodianUsername(custodian.getId(), custodian.getUsername()) > 0) {
                changes+=1;
            }

            if (custodianRepository.updateCustodianFirstName(custodian.getId(), custodian.getFirstName()) > 0) {
                changes+=1;
            }

            if (custodianRepository.updateCustodianLastName(custodian.getId(), custodian.getLastName()) > 0) {
                changes+=1;
            }

            if (custodianRepository.updateCustodianStartDate(custodian.getId(), custodian.getStartDate()) > 0) {
                changes+=1;
            }

            if (custodianRepository.updateCustodianStatus(custodian.getId(), custodian.getStatus()) > 0) {
                changes+=1;
            }

            if (custodianRepository.updateCustodianCleaningLevel(custodian.getId(), custodian.getCleaningLevel()) > 0) {
                changes+=1;
            }


            return changes > 0;
        }

        return false;

    }
}

