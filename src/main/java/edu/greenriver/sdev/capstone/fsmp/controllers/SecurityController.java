package edu.greenriver.sdev.capstone.fsmp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController extends AuthenticationInformation{

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("message", "Invalid Credentials");
        }

        if (logout != null) {
            model.addAttribute("message", "User logged out");
        }

        model.addAttribute("pageTitle", "Login");

        return "general/login";
    }

    /**
     * @param model for adding attributes
     * @return route to access denied page
     */
    @GetMapping("/access_denied")
    public String denied(Model model) {

        model.addAttribute("pageTitle", "Access Denied");
        return "general/access_denied";
    }
}
