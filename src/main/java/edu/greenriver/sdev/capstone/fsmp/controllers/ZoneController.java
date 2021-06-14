package edu.greenriver.sdev.capstone.fsmp.controllers;


import edu.greenriver.sdev.capstone.fsmp.model.Facility;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;
import edu.greenriver.sdev.capstone.fsmp.model.Zone;
import edu.greenriver.sdev.capstone.fsmp.service.FacilityRepositoryService;
import edu.greenriver.sdev.capstone.fsmp.service.ZoneRepositoryService;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@AllArgsConstructor
@ToString
@Controller
@RequestMapping("/facility/zone")
public class ZoneController extends AuthenticationInformation{
    private ZoneRepositoryService zoneRepService;
    private FacilityRepositoryService facRepService;

    @GetMapping("new/{facilityId}")
    public String newZone(@PathVariable Long facilityId, Model model) {

        model.addAttribute("pageTitle", "New Facility");
        model.addAttribute("zone", new Zone());
        model.addAttribute("facilityId", facilityId);

        return "zone/new_zone";
    }

    @PostMapping("new/{facilityId}")
    public String addNewZone(@PathVariable Long facilityId,
                             @Valid Zone zone,
                                 BindingResult bindingResult,
                                 Model model) {

        //Right Now all new zones are given to unassigned
        Facility currentFacility = facRepService.getFacilityById(facilityId);
        model.addAttribute("pageTitle", "New Facility");

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Name can't be blank");
            return "facility/new_facility";
        }

        zone = zoneRepService.addZone(currentFacility, zone);

        if (zone != null) {
            return "redirect:/facility/view/" + facilityId;
        }

        model.addAttribute("error", "You already have a zone with that name");
        return "zone/new_zone";
    }

    @Transactional
    @PostMapping("delete")
    public String deleteZoneButNotRooms(Zone zone, Model model){

        zone = zoneRepService.getZoneById(zone.getId());

        if(zoneRepService.deleteZone(zone)){
            return "redirect:/facility/view/"+zone.getFacilityId();
        }

        model.addAttribute("pageTitle", "delete zone");

        return "zone/failed_delete";

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
