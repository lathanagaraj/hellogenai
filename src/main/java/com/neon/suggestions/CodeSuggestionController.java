package com.neon.suggestions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/suggest")
public class CodeSuggestionController {

    private CodeSuggestionService codeSuggestionService;

    @Autowired
    public CodeSuggestionController(CodeSuggestionService codeSuggestionService) {
        this.codeSuggestionService = codeSuggestionService;
    }
    @GetMapping("/code")
    public String suggestCode(@RequestParam String query) {
        return codeSuggestionService.suggestCode(query);
    }
}
