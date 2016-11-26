package whattodraw.characters;

public class Character{
    private String trait;
    private String setting;
    private String name;

    public Character(){}

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

    @Override
    public String toString() {
        return String.join(" ",trait,setting,name);
    }
}
