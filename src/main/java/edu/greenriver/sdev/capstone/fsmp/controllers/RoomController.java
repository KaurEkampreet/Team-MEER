package edu.greenriver.sdev.capstone.fsmp.controllers;

import edu.greenriver.sdev.capstone.fsmp.model.*;
import edu.greenriver.sdev.capstone.fsmp.service.*;
import edu.greenriver.sdev.capstone.fsmp.util.RoomValidator;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@AllArgsConstructor
@ToString
@Controller
@RequestMapping("/facility/room")
public class RoomController extends AuthenticationInformation {

    RoomRepositoryService roomRepositoryService;
    ZoneRepositoryService zoneRepositoryService;
    FacilityRepositoryService facilityRepositoryService;
    RoomValidator roomValidator;

    @GetMapping("/new/{facilityId}/{zoneId}")
    public String newRoom(@PathVariable Long facilityId,
                          @PathVariable Long zoneId,
                          Model model) {

        Facility facility = facilityRepositoryService.getFacilityById(facilityId);
        ArrayList<Zone> zones = zoneRepositoryService.getAllZonesByFacilityID(facilityId);

        model.addAttribute("pageTitle", "New Room");
        model.addAttribute("room", new Room());
        model.addAttribute("facility", facility);
        model.addAttribute("currentZoneId", zoneId);
        model.addAttribute("zones", zones);


        return "room/new_room";
    }


    @PostMapping("/new/{facilityId}")
    public String addNewRoom(@PathVariable Long facilityId,
                             @Valid Room room,
                             BindingResult bindingResult,
                             Model model) {

        Facility facility = facilityRepositoryService.getFacilityById(facilityId);
        ArrayList<Zone> zones = zoneRepositoryService.getAllZonesByFacilityID(facilityId);

        model.addAttribute("facility", facility);
        model.addAttribute("zones", zones);
        model.addAttribute("currentZoneId", room.getZoneId());


        boolean validRoom = true;

        model.addAttribute("pageTitle", "New Room");
        Long currentZoneId = room.getZoneId();
        Zone currentZone = zoneRepositoryService.getZoneById(currentZoneId);

        if(!roomValidator.hasValidZone(facilityId, currentZoneId)){
            validRoom = false;
            model.addAttribute("zoneError", "invalid zone");
        }

        if(!roomValidator.hasValidRoomType(room.getRoomType())){
            validRoom = false;
            model.addAttribute("roomTypeError", "roomtype cannot be blank");
        }

        if(!roomValidator.hasValidRoomNumber(room.getRoomNumber())){
            validRoom = false;
            model.addAttribute("roomNumberError", "room number cannot be blank");
        }

        if(!roomValidator.hasValidSquareFootage(room.getSquareFootage())){
            validRoom = false;
            model.addAttribute("squareFootageError", "square footage cannot be blank");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Error Occured");
            validRoom = false;
        }

        if(!validRoom){
            model.addAttribute("failed", "failed to add room");
            return "room/new_room";
        }

        currentZone = roomRepositoryService.addRoom(currentZone, room);

        if(currentZone == null){
            return "room/new_room";
        }



        return "redirect:/facility/view/"+facilityId;



    }

    @GetMapping("/view/{roomId}")
    public String roomById(@PathVariable Long roomId, Model model){

        Room room = roomRepositoryService.getRoomById(roomId);
        Zone zone = zoneRepositoryService.getZoneById(room.getZoneId());
        Facility facility = facilityRepositoryService.getFacilityById(zone.getFacilityId());

        model.addAttribute("pageTitle", roomId);
        model.addAttribute("room", room);
        model.addAttribute("zone", zone);
        model.addAttribute("facility", facility);

        return "room/room_by_id";
    }






}