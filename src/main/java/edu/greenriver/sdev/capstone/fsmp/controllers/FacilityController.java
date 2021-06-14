package edu.greenriver.sdev.capstone.fsmp.controllers;

import edu.greenriver.sdev.capstone.fsmp.model.Facility;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;

import edu.greenriver.sdev.capstone.fsmp.model.Room;
import edu.greenriver.sdev.capstone.fsmp.model.Zone;
import edu.greenriver.sdev.capstone.fsmp.service.FacilityRepositoryService;
import edu.greenriver.sdev.capstone.fsmp.service.RoomRepositoryService;
import edu.greenriver.sdev.capstone.fsmp.service.ZoneRepositoryService;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ToString
@Controller
@RequestMapping("/facility")
public class FacilityController extends AuthenticationInformation{
    private FacilityRepositoryService facRepService;
    private ZoneRepositoryService zoneRepositoryService;
    private RoomRepositoryService roomRepositoryService;


    @GetMapping("all")
    public String allFacilities(Model model){

        model.addAttribute("facilities", facRepService.getFacilitiesByFacAdminId(facilityAdminID()));
        model.addAttribute("pageTitle", "All Facilities");

        return "facility/all_facilities";
    }

    @GetMapping("new")
    public String newFacility(Model model) {

        model.addAttribute("pageTitle", "New Facility");
        model.addAttribute("facility", new Facility());

        return "facility/new_facility";
    }

    @PostMapping("new")
    public String addNewFacility(@Valid Facility facility,
                                 BindingResult bindingResult,
                                 Model model) {

        model.addAttribute("pageTitle", "New Facility");

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Name can't be blank");
            return "facility/new_facility";
        }

        facility = facRepService.addFacility(facilityAdminID(), facility);


        if (facility != null) {
            return "redirect:/facility/all";
        }

        model.addAttribute("error", "You already have a facility with that name");
        return "facility/new_facility";
    }




    @GetMapping("view/{facilityId}")
    public String facilityById(
            @PathVariable Long facilityId,
            Zone tempZone,
            Model model){

        Facility facility = facRepService.getFacilityById(facilityId);

        if(facility.getFacilityAdministratorId() != facilityAdminID()){
            return "redirect:/facility_admin/";
        }

        List<Zone> zones = zoneRepositoryService.getAllZonesByFacilityID(facilityId);

        List<Room> rooms = new ArrayList<>();

        for (Zone zone :
                zones) {
            rooms.addAll(roomRepositoryService.getRoomsByZoneId(zone.getId()));
        }

        model.addAttribute("facility", facility);
        model.addAttribute("zones", zones);
        model.addAttribute("rooms", rooms);
        model.addAttribute("pageTitle", facilityId);
        model.addAttribute("tempZone", tempZone);

        return "facility/specific_facility";
    }

    @ModelAttribute("facilityAdminID")
    public Long facilityAdminID(){

        FacilityAdministrator currentUser = new FacilityAdministrator();

        currentUser = facRepService.getFacilityAdminByUsername(loggedInUsername()).orElse(null);

        if(currentUser != null) {
            return currentUser.getId();
        }

        return (long) -1;

    }



}
