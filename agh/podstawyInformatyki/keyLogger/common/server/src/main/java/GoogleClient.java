import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

import java.io.IOException;
import java.util.HashMap;

public class GoogleClient {
    private final HashMap<String, Person> Cache ;

    public GoogleClient()
    {
        this.Cache = new HashMap<String, Person>();
    }

    public Person GetPerson(String accessCode) {
        if(this.Cache.containsKey(accessCode))
        {
            return  this.Cache.get(accessCode);
        }
        var jf = JacksonFactory.getDefaultInstance();
        var net = new NetHttpTransport();
        var plus = new Plus.Builder(net, jf, null).setApplicationName("480347143153-ubv9bp6omu7komacci3jp7qqt5be30v1.apps.googleusercontent.com").build();
        Person person = null;
        try {
            var getRequest = plus.people().get("me");
            getRequest.setOauthToken(accessCode);
            person = getRequest.execute();
            this.Cache.put(accessCode, person);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return person;
    }
}
