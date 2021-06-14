package edu.greenriver.sdev.capstone.fsmp.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.greenriver.sdev.capstone.fsmp.jsonMappings.BuildingNumberPOJO;
import edu.greenriver.sdev.capstone.fsmp.jsonMappings.CleaningTimesDatabasePOJO;
import edu.greenriver.sdev.capstone.fsmp.jsonMappings.CustodianPOJO;
import edu.greenriver.sdev.capstone.fsmp.repository.AdministratorRepository;
import edu.greenriver.sdev.capstone.fsmp.repository.CustodianRepository;
import edu.greenriver.sdev.capstone.fsmp.service.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * For adding initial lenders at startup.
 *
 * @author Robert Cox
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class AccountSetup implements CommandLineRunner {

    private final AdministratorRepository adminRepo;
    private FacilityAdministratorRepositoryService facilityAdministratorRepositoryService;
    private FacilityRepositoryService facilityRepositoryService;
    private CustodianRepositoryService custodianRepositoryService;
    private CustodianRepository custodianRepository;
    private ZoneRepositoryService zoneRepositoryService;
    private RoomRepositoryService roomRepositoryService;
    private ObjectMapper mapper;
    private BuildingNumberPOJO buildingNumberPOJO;
    private CleaningTimesDatabasePOJO cleaningTimesDatabasePOJO;
    private CustodianPOJO custodianPOJO;
    private AccountSetupService accountSetupService;
    @Override
    public void run(String... args) {

        if(adminRepo.getAdministratorByUsername("admin").isPresent()) {
            return;
        }

        accountSetupService.createAndAddAdministrator();
        accountSetupService.setUpAccount();

        try {

            File custodianBackupFile = new File("./src/main/resources/static/json/fmspJsonDb/fmspCustodians.json");
            if (custodianBackupFile.createNewFile()) {
                //custodianBackupFile created, add to the custodianBackupFile here
                System.out.println(custodianBackupFile.getPath());
            } else {
                //custodianBackupFile exists, edit the custodianBackupFile here
                System.out.println(custodianBackupFile.getPath());
            }

            FileWriter custodianFileWriter = new FileWriter(custodianBackupFile);
            List<Custodian> allCustodians = custodianRepositoryService.allCustodians();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            custodianFileWriter.write(mapper.writeValueAsString(allCustodians));
            custodianFileWriter.close();

            TypeReference<List<Custodian>> custodianTypeReference = new TypeReference<>(){};
            InputStream custodianInputStream = TypeReference.class.getResourceAsStream("/static/json/fmspJsonDb/fmspCustodians.json");
            List<Custodian> custodianList = mapper.readValue(custodianInputStream, custodianTypeReference);
            for (Custodian custodian : custodianList) {
                System.out.println("Custodian id: " + custodian.getId() + ", username: " + custodian.getUsername() + " - Retrieved");
            }

            File facilityBackupFile = new File("./src/main/resources/static/json/fmspJsonDb/fmspFacilities.json");
            if (facilityBackupFile.createNewFile()) {
                //facilityBackupFile created, add to the custodianBackupFile here
                System.out.println(facilityBackupFile.getPath());
            } else {
                //facilityBackupFile exists, edit the custodianBackupFile here
                System.out.println(facilityBackupFile.getPath());
            }
            FileWriter facilityFileWriter = new FileWriter(facilityBackupFile);
            List<Facility> allFacilities = facilityRepositoryService.getAllFacilities();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            facilityFileWriter.write(mapper.writeValueAsString(allFacilities));
            facilityFileWriter.close();

            TypeReference<List<Facility>> facilityTypeReference = new TypeReference<>(){};
            InputStream facilityInputStream = TypeReference.class.getResourceAsStream("/static/json/fmspJsonDb/fmspFacilities.json");
            List<Facility> facilityList2 = mapper.readValue(facilityInputStream, facilityTypeReference);
            for (Facility facility : facilityList2) {
                System.out.println("Facility id: " + facility.getId() + ", name: " + facility.getFacilityName() + " - Retrieved");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "AccountSetup{" +
                "repo=" + adminRepo +
                '}';
    }
}
