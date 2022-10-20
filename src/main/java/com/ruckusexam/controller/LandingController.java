package com.ruckusexam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author csu
 */
@Controller
public class LandingController {

    @GetMapping("/")
    public String getLanding(HttpSession session) {
        session.setAttribute("isLogin", false);
        return "index";
    }

}