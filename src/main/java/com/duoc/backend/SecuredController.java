package com.duoc.backend;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class SecuredController {

    @GetMapping(value = "/greetings", produces = MediaType.TEXT_HTML_VALUE)
    public String greetings(
            @RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello {" + HtmlUtils.htmlEscape(name) + "}";
    }
}