import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

import java.io.IOException;

public class GoogleClient {
    public Person GetPerson(String accessCode) {
        var jf = JacksonFactory.getDefaultInstance();
        var net = new NetHttpTransport();
        var plus = new Plus.Builder(net, jf, null).setApplicationName("480347143153-ubv9bp6omu7komacci3jp7qqt5be30v1.apps.googleusercontent.com").build();
        Person person = null;
        try {
            var getRequest = plus.people().get("me");
            getRequest.setOauthToken(accessCode);
            person = getRequest.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return person;
    }
}
