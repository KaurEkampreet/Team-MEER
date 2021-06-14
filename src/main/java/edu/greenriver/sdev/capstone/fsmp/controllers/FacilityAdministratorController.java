package edu.greenriver.sdev.capstone.fsmp.controllers;

import edu.greenriver.sdev.capstone.fsmp.model.Custodian;
import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;
import edu.greenriver.sdev.capstone.fsmp.repository.FacilityAdministratorRepository;
import edu.greenriver.sdev.capstone.fsmp.service.*;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@ToString
@Controller
@RequestMapping("/facility_admin")
public class FacilityAdministratorController extends AuthenticationInformation {

    FacilityAdministratorRepositoryService facilityAdministratorRepositoryService;
    /*FacilityAdministratorRepository facilityAdministratorRepository;*/
    FacilityRepositoryService facilityRepositoryService;
    CustodianRepositoryService custodianRepositoryService;
    ZoneRepositoryService zoneRepositoryService;
    UserNamePasswordValidator userNamePasswordValidator;

    @GetMapping("")
    public String facilityAdmin(Model model) {

        model.addAttribute("pageTitle", "Facility Admin Home");

        return "general/facility_administrator";
    }

    @GetMapping("/register_custodian")
    public String newCustodian(Custodian custodian, Model model) {

        model.addAttribute("custodian", custodian);
        model.addAttribute("pageTitle", "Register New Custodian");

        return "general/custodian_registration";
    }

    @PostMapping("/register_custodian")
    public String registerCustodian(@Valid Custodian custodian,
                                    BindingResult bindingResult,
                                    Model model) {

        boolean isValidCustodian = true;

        if (bindingResult.hasErrors()) {
            isValidCustodian = false;
        }

        if (!userNamePasswordValidator.userNameUnique(custodian.getUsername())) {
            isValidCustodian = false;
            model.addAttribute("usernameExists", "Username exists");
        }

        if (!userNamePasswordValidator.isPasswordValid(custodian.getPassword())) {
            isValidCustodian = false;
            model.addAttribute("passwordError", "8-20 characters and one of the following a-z A-Z 0-9, no space or <>");
        }

        if (isValidCustodian) {
            /*custodian = custodianRepositoryService.addCustodian(custodian);*/
            custodian = custodianRepositoryService.registerCustodian(facilityAdminID(), custodian);

            if (custodian != null) {
                return "redirect:/facility_admin/custodian_added";
            }

            model.addAttribute("pageTitle", "Register New Custodian");
            return "general/custodian_registration";
        }

        model.addAttribute("pageTitle", "Register New Custodian");
        return "general/custodian_registration";
    }

    @GetMapping("/custodian_added")
    public String custodianAdded(Model model) {

        model.addAttribute("pageTitle", "Custodian Added");

        return "general/custodian_added";
    }

    @GetMapping("/view_all_custodians")
    public String viewAllCustodians(Model model) {


        List<Custodian> custodians = custodianRepositoryService.custodiansByFacilityAdminID(facilityAdminID());
        /*if (!custodians.isEmpty()) {
            Field[] fields = custodians.get(0).getClass().getDeclaredFields();
            model.addAttribute("fields", fields);
        }*/

        model.addAttribute("custodians", custodians);
        model.addAttribute("pageTitle", "All Custodians");

        return "general/all_custodians";

    }

    @ModelAttribute("facilityAdminID")
    public Long facilityAdminID() {
        String currentUsername = loggedInUsername();

        Optional<FacilityAdministrator> currentUserOp = facilityRepositoryService.getFacilityAdminByUsername(currentUsername);

        FacilityAdministrator currentUser = new FacilityAdministrator();

        if (currentUserOp.isPresent()) {
            currentUser = currentUserOp.get();
        }

        return currentUser.getId();
    }
}
