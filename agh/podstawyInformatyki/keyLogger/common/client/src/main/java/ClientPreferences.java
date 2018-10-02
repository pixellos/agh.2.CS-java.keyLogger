import java.util.prefs.Preferences;

public class ClientPreferences {
    private static final String AUTH_KEY = "AUTH_KEY";
    private java.util.prefs.Preferences Preferences;

    public ClientPreferences() {
        this.Preferences = Preferences.userNodeForPackage(ClientPreferences.class);
    }

    String getKey() {
        var result = this.Preferences.get(ClientPreferences.AUTH_KEY, "Anonymous");
        return result;
    }

    void setKey(String key) {
        this.Preferences.put(ClientPreferences.AUTH_KEY, key);
    }

}
