package whattodraw.characters;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import whattodraw.commons.Repository;
import whattodraw.suggestions.Suggestion;
import whattodraw.suggestions.SuggestionRepository;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static whattodraw.characters.CHARACTER_KEYS.*;

@org.springframework.stereotype.Repository
public class CharacterRepository implements Repository<Character> {

    @Value("classpath:characters/names")
    private Resource characterSource;

    @Value("classpath:characters/traits")
    private Resource traitSource;

    @Value("classpath:characters/settings")
    private Resource settingSource;

    @Autowired
    @Qualifier("StringTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SuggestionRepository suggestions;

    private SetOperations<String, String> setOps;

    @PostConstruct
    private void init() {
        setOps = redisTemplate.opsForSet();

        if (setOps.size(NAMES.key).equals(0L)) {
            try (InputStream is = characterSource.getInputStream()) {
                ((List<String>) IOUtils.readLines(is)).forEach(value -> setOps.add(NAMES.key, value));
            } catch (IOException e) {

            }
        }

        if (setOps.size(TRAITS.key).equals(0L)) {
            try (InputStream is = traitSource.getInputStream()) {
                ((List<String>) IOUtils.readLines(is)).forEach(value -> setOps.add(TRAITS.key, value));
            } catch (IOException e) {

            }
        }

        if (setOps.size(SETTINGS.key).equals(0L)) {
            try (InputStream is = settingSource.getInputStream()) {
                ((List<String>) IOUtils.readLines(is)).forEach(value -> setOps.add(SETTINGS.key, value));
            } catch (IOException e) {

            }
        }
    }

    @Override
    public Character getRandom() {
        String trait = setOps.randomMember(TRAITS.key);
        String setting = setOps.randomMember(SETTINGS.key);
        String name = setOps.randomMember(NAMES.key);
        return new Character(trait, setting, name);
    }

    @Override
    public boolean removeVariant(String key, String value) {
        if (!isValid(value)) {
            return false;
        }
        switch (key) {
            case "traits":
                return setOps.remove(TRAITS.key, value) > 0;
            case "settings":
                return setOps.remove(SETTINGS.key, value) > 0;
            case "names":
                return setOps.remove(NAMES.key, value) > 0;
        }

        return false;
    }

    private boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    public boolean addSuggestion(Character character) {
        boolean names = suggestions.addSuggestion(NAMES.suggestion, character.getName());
        boolean settings = suggestions.addSuggestion(SETTINGS.suggestion, character.getSetting());
        boolean traits = suggestions.addSuggestion(TRAITS.suggestion, character.getTrait());
        return names || settings || traits;
    }

    public Map<String, Set<String>> getSuggestions() {
        Map<String, Set<String>> suggestionMap = new HashMap<>();
        suggestionMap.put("traits", suggestions.getSuggestions(TRAITS.suggestion));
        suggestionMap.put("settings", suggestions.getSuggestions(SETTINGS.suggestion));
        suggestionMap.put("names", suggestions.getSuggestions(NAMES.suggestion));
        return suggestionMap;
    }

    @Override
    public Map<String, Set<String>> getAllVariants() {
        Map<String, Set<String>> variantsMap = new HashMap<>();
        variantsMap.put("traits", setOps.members(TRAITS.key));
        variantsMap.put("settings", setOps.members(SETTINGS.key));
        variantsMap.put("names", setOps.members(NAMES.key));
        return variantsMap;
    }

    public boolean approveSuggestion(String key, String value) {
        switch (key) {
            case "traits":
                suggestions.removeSuggestion(TRAITS.suggestion, value);
                return setOps.add(TRAITS.key, value) > 0;
            case "settings":
                suggestions.removeSuggestion(SETTINGS.suggestion, value);
                return setOps.add(SETTINGS.key, value) > 0;
            case "names":
                suggestions.removeSuggestion(NAMES.suggestion, value);
                return setOps.add(NAMES.key, value) > 0;
        }
        return false;

    }

    public boolean removeSuggestion(String key, String value) {
        switch (key) {
            case "traits":
                return suggestions.removeSuggestion(TRAITS.suggestion, value);
            case "settings":
                return suggestions.removeSuggestion(SETTINGS.suggestion, value);
            case "names":
                return suggestions.removeSuggestion(NAMES.suggestion, value);
        }
        return false;

    }


}
