package whattodraw.model;

/**
 * Created by cheshirrrr on 13.11.2016.
 */
public class Character {
    private String trait;
    private String setting;
    private String name;

    public Character(String trait, String setting, String name) {
        this.trait = trait;
        this.setting = setting;
        this.name = name;
    }

    public String getTrait() {
        return trait;
    }

    public String getSetting() {
        return setting;
    }

    public String getName() {
        return name;
    }
}
