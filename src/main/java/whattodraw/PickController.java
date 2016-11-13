package whattodraw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import whattodraw.model.Character;
import whattodraw.picker.CharacterPicker;

@Controller
@RequestMapping("/whattodraw")
public class PickController {

    @Autowired
    CharacterPicker characterPicker;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String page(Model model) {
        return "whattodraw";
    }

    @RequestMapping(value = "/character", method = RequestMethod.GET)
    public String character(Model model) {
        Character character = characterPicker.getRandom();
        model.addAttribute("char", character);

        return "results::character";
    }

}
