package edu.greenriver.sdev.capstone.fsmp.repository;

import edu.greenriver.sdev.capstone.fsmp.model.Facility;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    ArrayList<Facility> getAllByFacilityAdministratorId(long facilityAdministratorId);

    Facility getFacilityById(long id);
    Facility getByFacilityName(String name);
    Facility getFacilityByFacilityAdministratorIdAndAndFacilityName(Long id, String name);
    Facility getFacilityByFacilityAdministratorIdAndAndBuildingNumber(Long id, String number);
    Facility getByFacilityAdministratorIdAndAndFacilityName(Long id, String name);
}
