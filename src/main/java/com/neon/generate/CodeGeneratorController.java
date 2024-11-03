package com.neon.generate;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code")
public class CodeGeneratorController {

    private final CodeGeneratorService codeGenerator;

    @Autowired
    public CodeGeneratorController(CodeGeneratorService codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @PostMapping("/generate")
    public String generateCode(@RequestBody String message) {
        return codeGenerator.generateCode(message);
    }
}