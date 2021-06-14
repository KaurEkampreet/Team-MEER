package edu.greenriver.sdev.capstone.fsmp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserNamePasswordValidator {

    FacilityAdministratorRepositoryService facilityAdministratorRepositoryService;
    CustodianRepositoryService custodianRepositoryService;

    public boolean isPasswordValid(String password) {
        if (password == null || password.equals("") || password.length() > 20 || password.length() < 8) return false;

        Character[] requiredSymbols = {'!', '@', '#', '#', '$', '%', '^', '&', '*', '(', ')', '?'};
        Character[] excludedSymbols = {'<', '>', ' '};

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasNum = false;
        boolean hasSymbol = false;

        for (int i = 0; i < password.length(); i++) {

            char current = password.charAt(i);

            for (int k = 0; k < excludedSymbols.length; k++) {
                if (current == excludedSymbols[k]) return false;
            }

            if (!hasUpper && Character.isUpperCase(current)) hasUpper = true;
            if (!hasLower && Character.isLowerCase(current)) hasLower = true;
            if (!hasNum && Character.isDigit(current)) hasNum = true;
            if (!hasSymbol) {
                for (int j = 0; j < requiredSymbols.length; j++) {
                    if (current == requiredSymbols[j]) {
                        hasSymbol = true;
                        break;
                    }
                }
            }
        }

        if (hasUpper && hasLower && hasNum && hasSymbol) return true;

        return false;
    }


    public boolean userNameUnique(String username) {
        return !facilityAdministratorRepositoryService.facilityAdminUsernameExists(username) &&
                !custodianRepositoryService.custodianUsernameExists(username);
    }
}
