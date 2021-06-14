package edu.greenriver.sdev.capstone.fsmp.controllers;

import edu.greenriver.sdev.capstone.fsmp.service.CustodianRepositoryService;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@ToString
@Controller
@RequestMapping("/custodian")
public class CustodianController extends AuthenticationInformation
{

    CustodianRepositoryService custodianRepositoryService;

    @GetMapping("")
    public String custodian(Model model)
    {
        model.addAttribute("pageTitle", "Custodian Home Page");

        return "general/custodian";
    }

}
