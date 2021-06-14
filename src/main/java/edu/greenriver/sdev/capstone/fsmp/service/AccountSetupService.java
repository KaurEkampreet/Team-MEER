package edu.greenriver.sdev.capstone.fsmp.service;

import edu.greenriver.sdev.capstone.fsmp.jsonMappings.BuildingNumberPOJO;
import edu.greenriver.sdev.capstone.fsmp.jsonMappings.CleaningTimesDatabasePOJO;
import edu.greenriver.sdev.capstone.fsmp.jsonMappings.CustodianPOJO;
import edu.greenriver.sdev.capstone.fsmp.model.*;
import edu.greenriver.sdev.capstone.fsmp.repository.AdministratorRepository;
import edu.greenriver.sdev.capstone.fsmp.repository.CustodianRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountSetupService {

    private BuildingNumberPOJO buildingNumberPOJO;
    private CleaningTimesDatabasePOJO cleaningTimesDatabasePOJO;
    private CustodianPOJO custodianPOJO;
    private AdministratorRepository adminRepo;
    private FacilityAdministratorRepositoryService facilityAdministratorRepositoryService;
    private FacilityRepositoryService facilityRepositoryService;
    private ZoneRepositoryService zoneRepositoryService;
    private RoomRepositoryService roomRepositoryService;
    private CustodianRepository custodianRepository;
    private FacilityAdministrator facilityAdministrator;
    private BCryptPasswordEncoder encoder;



    public void createAndAddAdministrator() {


        Administrator admin = Administrator.builder()
                .username("admin")
                .password(encoder.encode("p"))
                .authorities(new ArrayList<>())
                .build();

        admin.getAuthorities().addAll(Arrays.asList(
                AdministratorAuthority.builder()
                        .authority("ROLE_ADMIN")
                        .administrator(admin)
                        .build(),
                AdministratorAuthority.builder()
                        .authority("ROLE_USER")
                        .administrator(admin)
                        .build()
        ));
        adminRepo.save(admin);
    }

    private void creatAndAddFacilityAdministrator() {
        FacilityAdministrator facilityAdministrator = FacilityAdministrator.builder()
                .username("gl")
                .password("p")
                .facilityAuthorities(new ArrayList<>())
                .firstName("Geoff")
                .lastName("Lawson")
                .build();

        this.facilityAdministrator = facilityAdministratorRepositoryService.registerFacAdmin(facilityAdministrator);
    }

    public void setUpAccount() {

        creatAndAddFacilityAdministrator();

        List<BuildingNumberPOJO> buildingNumberPOJOList = buildingNumberPOJO.retrievePOJOList();
        List<CleaningTimesDatabasePOJO> cleaningTimesDatabasePOJOList = cleaningTimesDatabasePOJO.retrievePOJOList();
        List<CustodianPOJO> custodianPOJOList = custodianPOJO.retrievePOJOList();

        for (BuildingNumberPOJO buildingPOJO : buildingNumberPOJOList) {
            facilityRepositoryService.addFacility(facilityAdministrator.getId(), Facility.builder()
                    .facilityName(buildingPOJO.getName())
                    .buildingNumber(buildingPOJO.getNumber())
                    .build());
        }

        List<Zone> zones = new ArrayList<>();

        for (CleaningTimesDatabasePOJO cleaningTimesDatabasePOJO : cleaningTimesDatabasePOJOList) {

            Facility tempFacility = facilityRepositoryService.getFacilityByFacilityAdminIdAndBuildingNumber(facilityAdministrator.getId(), cleaningTimesDatabasePOJO.getBuildingNumber());

            String zoneName = "Unassigned";
            for (CustodianPOJO custodianPOJO : custodianPOJOList) {
                if (custodianPOJO.getAssignedCustodianId().equals(cleaningTimesDatabasePOJO.getAssignedCustodianId())) {
                    zoneName = custodianPOJO.getFirstName().replaceAll("\\s+","") + custodianPOJO.getLastName().replaceAll("\\s+","");
                    break;
                }
            }

            Zone tempZone = zoneRepositoryService.addZone(tempFacility, Zone.builder()
                    .zoneName(zoneName)
                    .build());


            if (tempZone == null) {
                tempZone = zoneRepositoryService.getZoneByFacilityIdAndZoneName(tempFacility.getId(), zoneName);
            } else {
                zones.add(tempZone);
            }

            roomRepositoryService.addRoom(tempZone, Room.builder()
                    .roomNumber(cleaningTimesDatabasePOJO.getRoomNumber() + (cleaningTimesDatabasePOJO.getRoomLetter() != null && !cleaningTimesDatabasePOJO.getRoomLetter().equals("") ? " " + cleaningTimesDatabasePOJO.getRoomLetter() : ""))
                    .roomType(cleaningTimesDatabasePOJO.getSpaceType())
                    .squareFootage(cleaningTimesDatabasePOJO.getTotalSqft())
                    .build());

        }

        for (CustodianPOJO custodianPOJO :  custodianPOJOList) {

            Long currentZoneId = 0L;
            for (Zone zone: zones) {
                if (zone.getZoneName().equals(custodianPOJO.getFirstName().replaceAll("\\s+","") + custodianPOJO.getLastName().replaceAll("\\s+",""))) {
                    currentZoneId = zone.getId();
                    break;
                }
            }

            if (currentZoneId == 0) {
                Facility facility = facilityRepositoryService.getByFacilityNameAndAdminId(facilityAdministrator.getId(), "Unassigned");
                Zone zone = zoneRepositoryService.getZoneByFacilityIdAndZoneName(facility.getId(), "Unassigned");
                currentZoneId = zone.getId();
            }

            Custodian custodian = Custodian.builder()
                    .facilityAdministratorId(facilityAdministrator.getId())
                    .custodianAuthorities(new ArrayList<>())
                    .username(custodianPOJO.getFirstName().replaceAll("\\s+","")+custodianPOJO.getLastName().replaceAll("\\s+",""))
                    .password(encoder.encode("p"))
                    .firstName(custodianPOJO.getFirstName())
                    .lastName(custodianPOJO.getLastName())
                    .zoneId(currentZoneId)
                    .build();

            custodian.getCustodianAuthorities().addAll(Arrays.asList(
                    CustodianAuthority.builder()
                            .custodian(custodian)
                            .custodianAuthority("ROLE_CUSTODIAN")
                            .build(),
                    CustodianAuthority.builder()
                            .custodian(custodian)
                            .custodianAuthority("ROLE_USER")
                            .build()
            ));

            custodianRepository.save(custodian);
        }
    }
}
