package edu.greenriver.sdev.capstone.fsmp.service;

import edu.greenriver.sdev.capstone.fsmp.model.*;
import edu.greenriver.sdev.capstone.fsmp.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FacilityRepositoryService {

    FacilityRepository facilityRepository;
    ZoneRepositoryService zoneRepositoryService;
    ZoneRepository zoneRepository;
    FacilityAdministratorRepository facAdminRepository;

    public ArrayList<Facility> getFacilitiesByFacAdminId(Long facilityAdminId) {
        return facilityRepository.getAllByFacilityAdministratorId(facilityAdminId);
    }

    public Facility getFacilityById(Long id) {
        return facilityRepository.getFacilityById(id);
    }


    public void createInitialFacility(long id) {
        Facility initialFacility = Facility.builder()
                .facilityAdministratorId(id)
                .facilityName("Unassigned")
                .build();

        initialFacility = facilityRepository.save(initialFacility);

        zoneRepositoryService.createInitialZone(initialFacility);
    }

    public Optional<FacilityAdministrator> getFacilityAdminByUsername(String username){
        return facAdminRepository.getFacilityAdministratorByUsername(username);
    }

    public Facility addFacility(Long facAdminID, Facility facility){

        // Add a facility to a facility administrator
        ArrayList<Facility> facilitiesToCompare = facilityRepository.getAllByFacilityAdministratorId(facAdminID);

        for(Facility current : facilitiesToCompare){
            if(current.getFacilityName().equals(facility.getFacilityName())){
                return null;
            }
        }

        facility.setFacilityAdministratorId(facAdminID);

        facility = facilityRepository.save(facility);

        zoneRepositoryService.createInitialZone(facility);

        if (facility.getId() != null) {
            return facility;
        } else {
            return null;
        }
    }

    public Facility getFacilityByFacilityAdminIdAndBuildingNumber(Long id, String number) {
        return facilityRepository.getFacilityByFacilityAdministratorIdAndAndBuildingNumber(id, number);
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Facility getByFacilityNameAndAdminId (Long id, String name) {
        return facilityRepository.getByFacilityAdministratorIdAndAndFacilityName(id, name);
    }

    public boolean deleteFacility(Long id) {

        List<Zone> zones = zoneRepositoryService.getAllZonesByFacilityID(id);

        for (Zone zone :  zones) {
            zoneRepositoryService.deleteZoneAndRooms(zone.getId());
        }

        facilityRepository.deleteById(id);
        return facilityRepository.getFacilityById(id) == null;
    }
}
