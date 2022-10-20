package com.ruckusexam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author csu
 */
@RestController
public class SessionController {

    @RequestMapping(value = "/Hello", method = RequestMethod.GET)
    public String openForm() {
        return "hello admin";
    }

    @RequestMapping(value = "/HelloWithCount", method = RequestMethod.POST)
    public ObjectNode processForm(@ModelAttribute("id") String id, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        int count = 0;
        if (session.getAttribute("count") != null) {
            count = (int) session.getAttribute("count");
        }
        session.setAttribute("count", count + 1);
        jObj.put("count", count);
        return jObj;
    }
}