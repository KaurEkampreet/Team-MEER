package edu.greenriver.sdev.capstone.fsmp.repository;

import edu.greenriver.sdev.capstone.fsmp.model.Custodian;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustodianRepository extends JpaRepository<Custodian, Long> {

    Optional<Custodian> getCustodianByUsername(String username);

    List<Custodian> getCustodiansByFacilityAdministratorId(long facilityAdministratorId);

    Optional<Custodian> getCustodianById(Long id);
    List<Custodian> getCustodiansByZoneId(Long id);

    @Modifying
    @Query("UPDATE Custodian SET username= :custodianUsername WHERE id = :custodianId")
    int updateCustodianUsername(@Param("custodianId") Long id, @Param("custodianUsername") String username);

    @Modifying
    @Query("UPDATE Custodian SET firstName= :custodianFirstName WHERE id = :custodianId")
    int updateCustodianFirstName(@Param("custodianId") Long id, @Param("custodianFirstName") String firstName);

    @Modifying
    @Query("UPDATE Custodian SET lastName= :custodianLastName WHERE id = :custodianId")
    int updateCustodianLastName(@Param("custodianId") Long id, @Param("custodianLastName") String lastName);

    @Modifying
    @Query("UPDATE Custodian SET startDate= :custodianStartDate WHERE id = :custodianId")
    int updateCustodianStartDate(@Param("custodianId") Long id, @Param("custodianStartDate") String startDate);

    @Modifying
    @Query("UPDATE Custodian SET status= :custodianStatus WHERE id = :custodianId")
    int updateCustodianStatus(@Param("custodianId") Long id, @Param("custodianStatus") String status);

    @Modifying
    @Query("UPDATE Custodian SET cleaningLevel= :custodianCleaningLevel WHERE id = :custodianId")
    int updateCustodianCleaningLevel(@Param("custodianId") Long id, @Param("custodianCleaningLevel") String cleaningLevel);
}
