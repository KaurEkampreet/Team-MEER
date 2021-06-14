package edu.greenriver.sdev.capstone.fsmp.controllers;

import edu.greenriver.sdev.capstone.fsmp.model.FacilityAdministrator;
import edu.greenriver.sdev.capstone.fsmp.service.UserNamePasswordValidator;
import edu.greenriver.sdev.capstone.fsmp.service.FacilityAdministratorRepositoryService;
import edu.greenriver.sdev.capstone.fsmp.service.FacilityRepositoryService;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;

@AllArgsConstructor
@ToString
@Controller
@RequestMapping("/admin")
public class AdministratorController extends AuthenticationInformation{

    FacilityAdministratorRepositoryService facilityAdministratorRepositoryService;
    FacilityRepositoryService facilityRepositoryService;
    UserNamePasswordValidator userNamePasswordValidator;

    @GetMapping("")
    public String admin(Model model) {

        model.addAttribute("pageTitle", "Admin Home");

        return "general/admin";
    }

    @GetMapping("/register_facility_administrator")
    public String newFacilityAdmin(FacilityAdministrator facilityAdministrator,
                                   Model model) {

        model.addAttribute("facilityAdministrator", facilityAdministrator);
        model.addAttribute("pageTitle", "Register New Facility Administrator");

        return "general/facility_administrator_registration";
    }

    @PostMapping("/register_facility_administrator")
    public String registerFacilityAdmin(@Valid FacilityAdministrator facilityAdministrator,
                                        BindingResult bindingResult,
                                        Model model) {

        boolean isValid = true;

        if (bindingResult.hasErrors()) {
            isValid = false;
        }

        if (!userNamePasswordValidator.userNameUnique(facilityAdministrator.getUsername())) {
            isValid = false;
            model.addAttribute("usernameExists", "Username exists");
        }

        if (!userNamePasswordValidator.isPasswordValid(facilityAdministrator.getPassword())) {
            isValid = false;
            model.addAttribute("passwordError", "8-20 characters and one of the following a-z A-Z 0-9, no space or <>");
        }

        if (isValid) {
            facilityAdministrator = facilityAdministratorRepositoryService.registerFacAdmin(facilityAdministrator);

            if (facilityAdministrator != null) {
                return "redirect:/admin/facility_administrator_added";
            }

            model.addAttribute("pageTitle", "Register New Facility Administrator");
            return "general/facility_administrator_registration";
        }

        model.addAttribute("pageTitle", "Register New Facility Administrator");
        return "general/facility_administrator_registration";
    }

    @GetMapping("/facility_administrator_added")
    public String facilityAdminAdded(Model model) {

        model.addAttribute("pageTitle", "Facility Administrator Added");

        return "general/facility_administrator_added";
    }

    @GetMapping("/view_all_facility_admins")
    public String viewAllFacilityAdmin(FacilityAdministrator facilityAdministrator,
                                       Model model) {

        model.addAttribute("pageTitle", "All Facility Administrators");
        model.addAttribute("facilityAdministrator", facilityAdministrator);

        return "general/all_facility_administrators";
    }


    @Transactional
    @PostMapping("/view_all_facility_admins")
    public String editFacAdmin(@Valid FacilityAdministrator facilityAdministrator,
                               BindingResult bindingResult,
                               Model model){

        if (bindingResult.hasErrors() ||
                (facilityAdministratorRepositoryService.facilityAdminUsernameChanged(facilityAdministrator.getId(), facilityAdministrator.getUsername()) &&
                !userNamePasswordValidator.userNameUnique(facilityAdministrator.getUsername()))) {
            if (facilityAdministratorRepositoryService.facilityAdminUsernameChanged(facilityAdministrator.getId(), facilityAdministrator.getUsername()) &&
                    !userNamePasswordValidator.userNameUnique(facilityAdministrator.getUsername())) {
                model.addAttribute("usernameExists", "Username exists");
            }

            model.addAttribute("error", bindingResult.getModel());
        } else if (facilityAdministratorRepositoryService.updateFacAdmin(facilityAdministrator)){
            /*model.addAttribute("success", "form submission success");*/
            return "redirect:/admin/view_all_facility_admins";
        }

        model.addAttribute("pageTitle", "All Facility Administrators");
        model.addAttribute("facilityAdministrator", facilityAdministrator);

        return "general/all_facility_administrators";

    }

    @Transactional
    @PostMapping("/delete_facility_admin")
    public String deleteFacilityAdmin(FacilityAdministrator facilityAdministrator, Model model){

        if(facilityAdministratorRepositoryService.deleteFacilityAdmin(facilityAdministrator)) {
            return "redirect:/admin/view_all_facility_admins";
        }

        model.addAttribute("pageTitle", "All Facility Administrators");
        model.addAttribute("deleteError", "Problem while deleting, try again.");
        return "general/all_facility_administrators";
    }

    @ModelAttribute("allFacilityAdmins")
    public List<FacilityAdministrator> allFacilityAdmins() {
        List<FacilityAdministrator> allFacilityAdmins = facilityAdministratorRepositoryService.getAll();
        if (!allFacilityAdmins.isEmpty()) {

            return allFacilityAdmins;
        }
        return null;
    }

    @ModelAttribute("fields")
    public Field[] fieldsForAllFacilityAdmins() {

        List<FacilityAdministrator> allFacilityAdmins = facilityAdministratorRepositoryService.getAll();
        if (!allFacilityAdmins.isEmpty()) {
            return facilityAdministratorRepositoryService.getAll().get(0).getClass().getDeclaredFields();
        }
        return null;
    }
}
