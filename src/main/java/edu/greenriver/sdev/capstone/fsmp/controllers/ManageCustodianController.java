package edu.greenriver.sdev.capstone.fsmp.controllers;

import edu.greenriver.sdev.capstone.fsmp.model.Custodian;
import edu.greenriver.sdev.capstone.fsmp.model.Facility;
import edu.greenriver.sdev.capstone.fsmp.model.Room;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;
import edu.greenriver.sdev.capstone.fsmp.model.Zone;
import edu.greenriver.sdev.capstone.fsmp.service.*;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ToString
@Controller
@RequestMapping("/manage_custodian")
public class ManageCustodianController extends AuthenticationInformation {

    CustodianRepositoryService custodianRepositoryService;
    CustodianRoomsService custodianRoomsService;
    ZoneRepositoryService zoneRepositoryService;
    FacilityRepositoryService facilityRepositoryService;
    RoomRepositoryService roomRepositoryService;
    FacilityAdministratorRepositoryService facilityAdministratorRepositoryService;
    UserNamePasswordValidator userNamePasswordValidator;

    @GetMapping("/id/{custodianId}")
    public String custodianById(@PathVariable Long custodianId, Model model) {

        Custodian custodian = custodianRepositoryService.getCustodianById(custodianId);
        Zone zone = zoneRepositoryService.getZoneById(custodian.getZoneId());

        List<Facility> facilitiesByFacAdminId = facilityRepositoryService.getFacilitiesByFacAdminId(custodian.getFacilityAdministratorId());

        List<Zone> allZonesByFacAdmin = new ArrayList<>();
        for (Facility facility : facilitiesByFacAdminId) {
            allZonesByFacAdmin.addAll(zoneRepositoryService.getAllZonesByFacilityID(facility.getId()));
        }

        model.addAttribute("pageTitle", "Custodian #" + custodianId);
        model.addAttribute("custodian", custodian);
        model.addAttribute("custodianToEdit", custodian);
        model.addAttribute("roomsForCustodian", custodianRoomsService.getCustodianRooms(custodian));
        model.addAttribute("zoneForCustodian", zoneRepositoryService.getZoneById(custodian.getZoneId()));
        model.addAttribute("facilityForCustodian", facilityRepositoryService.getFacilityById(zone.getFacilityId()));
        model.addAttribute("facilitiesByFacAdminId", facilitiesByFacAdminId);
        model.addAttribute("allZonesList", allZonesByFacAdmin);
  
        return "general/custodian_by_id";
    }


    @GetMapping("viewzone/{zoneId}")
    public String manageZone(@PathVariable Long zoneId, Model model) {
        Zone zone = zoneRepositoryService.getZoneById(zoneId);

        model.addAttribute("pageTitle", "view zone");
        model.addAttribute("zone", zone);
        model.addAttribute("rooms", roomRepositoryService.getRoomsByZoneId(zoneId));

        return "zone/manage_zone";
    }
  
    @Transactional
    @PostMapping("/edit")
    public String editCustodian(@Valid Custodian custodian,
                                BindingResult bindingResult,
                                Model model){

        Long custodianId = custodian.getId();

        if (bindingResult.hasErrors() ||
                (custodianRepositoryService.custodianUsernameChanged(custodian.getId(), custodian.getUsername()) &&
                        !userNamePasswordValidator.userNameUnique(custodian.getUsername()))) {
            if (custodianRepositoryService.custodianUsernameChanged(custodian.getId(), custodian.getUsername()) &&
                    !userNamePasswordValidator.userNameUnique(custodian.getUsername())) {
                model.addAttribute("usernameExists", "Username exists");
            }

            model.addAttribute("errors", bindingResult.getAllErrors());
        } else if (custodianRepositoryService.updateCustodian(custodian)){
            return "redirect:/manage_custodian/id/" + custodian.getId();
        }

        Custodian oldCustodian = custodianRepositoryService.getCustodianById(custodianId);
        Zone zone = zoneRepositoryService.getZoneById(oldCustodian.getZoneId());

        List<Facility> facilitiesByFacAdminId = facilityRepositoryService.getFacilitiesByFacAdminId(oldCustodian.getFacilityAdministratorId());

        List<Zone> allZonesByFacAdmin = new ArrayList<>();

        for (Facility facility: facilitiesByFacAdminId) {
            allZonesByFacAdmin.addAll(zoneRepositoryService.getAllZonesByFacilityID(facility.getId()));
        }

        model.addAttribute("pageTitle", "Custodian #" + custodianId);
        model.addAttribute("custodian", oldCustodian);
        model.addAttribute("roomsForCustodian", custodianRoomsService.getCustodianRooms(custodianRepositoryService.getCustodianById(custodianId)));
        model.addAttribute("zoneForCustodian", zoneRepositoryService.getZoneById(oldCustodian.getZoneId()));
        model.addAttribute("facilityForCustodian", facilityRepositoryService.getFacilityById(zone.getFacilityId()));
        model.addAttribute("facilitiesByFacAdminId", facilitiesByFacAdminId);
        model.addAttribute("allZonesList", allZonesByFacAdmin);

        model.addAttribute("pageTitle", "Custodian" + custodian.getUsername());
        model.addAttribute("custodianToEdit", custodian);

        return "general/custodian_by_id";

    }

    @Transactional
    @PostMapping("assignment/change/")
    public String changeAssignment(Custodian custodian) {

        Custodian oldCustodian = custodianRepositoryService.getCustodianByUsername(custodian.getUsername());
        oldCustodian.setZoneId(custodian.getZoneId());
        custodianRepositoryService.updateCustodian(oldCustodian);
        return "redirect:/manage_custodian/id/" + oldCustodian.getId();

    }
}
