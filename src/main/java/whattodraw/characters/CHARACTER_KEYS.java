package whattodraw.characters;

enum CHARACTER_KEYS {
    TRAITS("char.traits","suggested.char.traits"),
    SETTINGS("char.settings","suggested.char.settings"),
    NAMES("char.names","suggested.char.names");


    public final String key;
    public final String suggestion;

    CHARACTER_KEYS(String key, String suggestion){
        this.key = key;
        this.suggestion = suggestion;
    }


}
