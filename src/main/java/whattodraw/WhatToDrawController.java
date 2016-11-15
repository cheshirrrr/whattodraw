package whattodraw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import whattodraw.characters.Character;
import whattodraw.characters.CharacterRepository;

@Controller
@RequestMapping("/whattodraw")
public class WhatToDrawController {
    @Autowired
    CharacterRepository characters;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStatic(Model model) {
        return "whattodraw";
    }

    @RequestMapping(value = "/character", method = RequestMethod.GET)
    String getCharacter(Model model) {
        Character character = characters.getRandom();
        model.addAttribute("char", character);

        return "results::character";
    }
}
