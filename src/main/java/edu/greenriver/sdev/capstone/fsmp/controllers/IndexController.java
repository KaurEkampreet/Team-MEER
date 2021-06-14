package edu.greenriver.sdev.capstone.fsmp.controllers;

import edu.greenriver.sdev.capstone.fsmp.service.CustodianRepositoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

/**
 * Controller for The Index/Homepage of the F.M.S.P.
 *
 * @author Team M.E.E.R.
 * @version 1.0
 */
@AllArgsConstructor
@Controller
public class IndexController extends AuthenticationInformation{
    CustodianRepositoryService custodianRepositoryService;

    /**
     * @return the index page (landing) of the website
     */
    @RequestMapping(path={"", "/", "index", "index.html"})
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("home")
    public String home(Model model) {
        ArrayList<String> roles =  userRoles();

        for(String role : roles) {
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin";
            } else if (role.equals("ROLE_FACILITY_ADMIN")) {
                return "redirect:/facility_admin";
            } else if (role.equals("ROLE_CUSTODIAN")){
                return "redirect:/manage_custodian/id/" + custodianRepositoryService.getCustodianByUsername(loggedInUsername()).getId();
            }
        }

        model.addAttribute("pageTitle", "Home");
        return "index";
    }
}
