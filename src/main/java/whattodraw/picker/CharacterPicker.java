package whattodraw.picker;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import whattodraw.model.Character;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("character_picker")
public class CharacterPicker extends Picker<Character> {

    @Value("classpath:characters/names")
    private Resource characterSource;

    @Value("classpath:characters/traits")
    private Resource traitSource;

    @Value("classpath:characters/settings")
    private Resource settingSource;

    private List<String> characters = new ArrayList<String>();
    private List<String> traits = new ArrayList<String>();
    private List<String> settings = new ArrayList<String>();

    @PostConstruct
    public void loadValues() {
        try (InputStream is = characterSource.getInputStream()) {
            characters = (List<String>) IOUtils.readLines(is);
        } catch (IOException e) {

        }

        try (InputStream is = traitSource.getInputStream()) {
            traits = (List<String>) IOUtils.readLines(is);
        } catch (IOException e) {

        }

        try (InputStream is = settingSource.getInputStream()) {
            settings = (List<String>) IOUtils.readLines(is);
        } catch (IOException e) {

        }
    }

    @Override
    public Character getRandom() {
        Random random = new Random();
        String trait = traits.get(random.nextInt(traits.size() - 1));
        String setting = settings.get(random.nextInt(settings.size() - 1));
        String name = characters.get(random.nextInt(characters.size() - 1));
        return new Character(trait, setting, name);
    }
}
