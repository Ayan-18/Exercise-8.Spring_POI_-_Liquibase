package com.example.exerciseeight.controller;


import com.example.exerciseeight.service.MeterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/api/v1.0")
@RequiredArgsConstructor
public class MeterController {
    private final MeterService meterService;

    @GetMapping(path = "/report")
    public ModelAndView report() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("report");
        modelAndView.addObject("reports", meterService.report());
        modelAndView.addObject("total", meterService.reportTotal());
        return modelAndView;
    }


}
