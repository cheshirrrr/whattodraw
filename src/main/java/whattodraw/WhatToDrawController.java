package whattodraw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import whattodraw.characters.Character;
import whattodraw.characters.CharacterRepository;

@Controller
@RequestMapping("/whattodraw")
public class WhatToDrawController {
    @Autowired
    CharacterRepository characters;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage(Model model) {
        return "whattodraw";
    }

    @RequestMapping(value = "/control", method = RequestMethod.GET)
    public String getControlPage(Model model) {
        return "control";
    }

    @RequestMapping(value = "/character", method = RequestMethod.GET)
    public String getCharacter(Model model) {
        Character character = characters.getRandom();
        model.addAttribute("char", character);

        return "results::character";
    }

    @RequestMapping(value = "/characters", method = RequestMethod.GET)
    public String getVariants(Model model) {
        model.addAllAttributes(characters.getAllVariants());
        return "control::variants";
    }

    @RequestMapping(value = "/characters", method = RequestMethod.POST)
    public ResponseEntity removeCharacterVariant(@RequestParam String key, @RequestParam String value) {
        boolean result = characters.removeVariant(key, value);

        if (result) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("confirmed");
        } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Suggestion doesn't exist");
        }
    }

    @RequestMapping(value = "/suggestions/characters", method = RequestMethod.PUT)
    public ResponseEntity suggestCharacter(@RequestBody Character character) {
        boolean result = characters.addSuggestion(character);
        if (result) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Suggestion accepted");
        } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Suggestion already exists");
        }
    }

    @RequestMapping(value = "/suggestions/characters", method = RequestMethod.GET)
    public String getSuggestions(Model model) {
        model.addAllAttributes(characters.getSuggestions());
        return "suggestions::suggestions";
    }


    @RequestMapping(value = "/suggestions/characters", method = RequestMethod.POST)
    public ResponseEntity changeSuggestion(@RequestParam String action, @RequestParam String key, @RequestParam String value) {
        boolean result = false;

        switch (action) {
            case "APPROVE":
                result = characters.approveSuggestion(key, value);
                break;
            case "REMOVE":
                result = characters.removeSuggestion(key, value);
                break;
            default:
                result = false;
        }

        if (result) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("confirmed");
        } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Suggestion doesn't exist");
        }
    }

}
