package org.lanren.idgenerator.controller;

import org.lanren.idgenerator.core.GeneratorServive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneratorController {

    @Autowired
    private GeneratorServive generatorServive;
    @GetMapping("/getId")
    public String getId(){
        return generatorServive.generatorId();
    }
}
