package edu.greenriver.sdev.capstone.fsmp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("schedules")
public class ScheduleController extends AuthenticationInformation{

    @GetMapping("{scheduleId}")
    public String getSchedule(
            @PathVariable String scheduleId,
            Model model) {

        if(scheduleId.equals("NoSchedule")){ // replace this condition to instead check if schedule exists
            model.addAttribute("error", "schedule not found");
            model.addAttribute("pageTitle", "no schedule");
            return "schedule/error";
        }

        model.addAttribute("scheduleId", scheduleId); // Replace scheduleId with Schedule and get Schedule object from the Id
        model.addAttribute("pageTitle", scheduleId);


        return "schedule/view_schedule";
    }
}
