package edu.greenriver.sdev.capstone.fsmp.repository;

import edu.greenriver.sdev.capstone.fsmp.model.Administrator;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministratorAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityAdministratorRepository extends JpaRepository<FacilityAdministrator, Long> {

    Optional<FacilityAdministrator> getFacilityAdministratorByUsername(String username);

    @Modifying
    @Query("UPDATE FacilityAdministrator SET username= :facilityAdminUsername WHERE id = :facilityAdminId")
    int updateFacilityAdministratorUsername(@Param("facilityAdminId") long id,
            @Param("facilityAdminUsername") String facilityAdministratorUsername);

    @Modifying
    @Query("UPDATE FacilityAdministrator SET firstName= :facilityAdminFirstName WHERE id = :facilityAdminId")
    int updateFacilityAdministratorFirstName(@Param("facilityAdminId") long id,
                                            @Param("facilityAdminFirstName") String facilityAdministratorFirstName);

    @Modifying
    @Query("UPDATE FacilityAdministrator SET lastName= :facilityAdminLastName WHERE id = :facilityAdminId")
    int updateFacilityAdministratorLastName(@Param("facilityAdminId") long id,
                                             @Param("facilityAdminLastName") String facilityAdministratorLastName);
}
