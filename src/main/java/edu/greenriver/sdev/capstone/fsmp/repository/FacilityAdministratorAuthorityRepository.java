package edu.greenriver.sdev.capstone.fsmp.repository;

import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministratorAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityAdministratorAuthorityRepository extends JpaRepository<FacilityAdministratorAuthority, Long> {
}
