package edu.greenriver.sdev.capstone.fsmp.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;

/**
 * @author Robert Cox
 * @version 1.0
 */
public class AuthenticationInformation {

    /**
     * Getting in the weeds here
     *
     * @return boolean
     */
    @ModelAttribute("validUserLoggedIn")
    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return auth != null && auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken);
    }

    /**
     * this is a rabbit hole
     *
     * @return logged in user's name
     */
    @ModelAttribute("loggedInUsername")
    public String loggedInUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @ModelAttribute("testVar")
    public String testVar() {
        return SecurityContextHolder
                .getContext()
                .toString();
    }

    @ModelAttribute("roles")
    public ArrayList<String> userRoles() {
        ArrayList<String> roles = new ArrayList<>();
        SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getAuthorities()
            .forEach(auth->{ roles.add(auth.getAuthority());
            });
            return roles;
    }
}
