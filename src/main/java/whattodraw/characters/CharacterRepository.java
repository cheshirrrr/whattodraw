package whattodraw.characters;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import whattodraw.commons.Repository;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

@org.springframework.stereotype.Repository
public class CharacterRepository implements Repository<Character> {

    private static String TRAITS_KEY = "traits";
    private static String SETTINGS_KEY = "settings";
    private static String NAMES_KEY = "names";

    @Value("classpath:characters/names")
    private Resource characterSource;

    @Value("classpath:characters/traits")
    private Resource traitSource;

    @Value("classpath:characters/settings")
    private Resource settingSource;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ListOperations<String,String> listOps;

    @PostConstruct
    private void init(){
        listOps = redisTemplate.opsForList();

        if(listOps.size(NAMES_KEY).equals(0L)) {
            try (InputStream is = characterSource.getInputStream()) {
                List<String> names = (List<String>) IOUtils.readLines(is);
                listOps.leftPushAll(NAMES_KEY, names);
            } catch (IOException e) {

            }
        }

        if(listOps.size(TRAITS_KEY).equals(0L)) {
            try (InputStream is = traitSource.getInputStream()) {
                List<String> traits = (List<String>) IOUtils.readLines(is);
                listOps.leftPushAll(TRAITS_KEY, traits);
            } catch (IOException e) {

            }
        }

        if(listOps.size(SETTINGS_KEY).equals(0L)) {
            try (InputStream is = settingSource.getInputStream()) {
                List<String> settings = (List<String>) IOUtils.readLines(is);
                listOps.leftPushAll(SETTINGS_KEY, settings);
            } catch (IOException e) {

            }
        }
    }

    public void addTrait(String trait){
        listOps.leftPush(TRAITS_KEY,trait);
    }

    public void addSetting(String setting){
        listOps.leftPush(SETTINGS_KEY,setting);
    }

    public void addName(String name){
        listOps.leftPush(NAMES_KEY,name);
    }

    @Override
    public Character getRandom() {
        Random random = new Random();
        String trait =  listOps.index(TRAITS_KEY,random.nextInt(listOps.size(TRAITS_KEY).intValue() - 1));
        String setting = listOps.index(SETTINGS_KEY,random.nextInt(listOps.size(SETTINGS_KEY).intValue() - 1));
        String name = listOps.index(NAMES_KEY,random.nextInt(listOps.size(NAMES_KEY).intValue() - 1));
        return new Character(trait, setting, name);
    }
}
