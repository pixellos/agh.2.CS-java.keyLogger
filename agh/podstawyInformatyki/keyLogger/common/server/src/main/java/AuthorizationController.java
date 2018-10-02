import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.*;

public class AuthorizationController{

    private static final String SESSION_NAME = "username";
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";
    private static Random Random;
    private final GoogleClient GoogleClient;

    private Dictionary<UUID, SessionData> Sessions ;
    private DatabaseClient DatabaseClient;

    public AuthorizationController(DatabaseClient dc, GoogleClient gc)
    {
        this.DatabaseClient = dc;
        this.GoogleClient = gc;
        this.Sessions = new Hashtable<>();
        this.Random = new Random();
    }

    public String Authorize(Request request, Response response) {
        var coll = new ArrayList<String>();
        coll.add("https://www.googleapis.com/auth/userinfo.email");
        coll.add("https://www.googleapis.com/auth/plus.me");
        coll.add("https://www.googleapis.com/auth/plus.login");
        coll.add("profile");
        coll.add("email");

        var acru = new AuthorizationCodeRequestUrl(
                "https://accounts.google.com/o/oauth2/auth",
                "480347143153-k0q7pdjdsb67tom36vpi4jaesct5cpd3.apps.googleusercontent.com"
        )
                .setRedirectUri("http://localhost:8080/oauth/google/callback")
                .setScopes(coll)
                .set("approval_prompt", "force");
        var url = acru.build();
        response.redirect(url);
        return "";
    }

    private SessionData SetSession(Request request)
    {
        var session = request.session().attribute(SESSION_NAME);
        if(session != null)
        {
            return this.Sessions.get(session);
        }
        return null;
    }

    private void SetSession(Request request, String accessCode)
    {
        Person person = this.GoogleClient.GetPerson(accessCode);

        if(person != null)
        {
            var guid = UUID.randomUUID();
            request.session().attribute(SESSION_NAME, guid);
            var sessionObject = new SessionData();
            sessionObject.email = person.getEmails().get(0).getValue();
            sessionObject.code = accessCode;
            this.Sessions.put(guid, sessionObject);
        }
    }



    public String GetLoginState(Request request, Response response) {
        var session = this.SetSession(request);
            var gson = new Gson();
            return gson.toJson(session);
    }


    private String getToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(this.Random.nextInt(CHARS.length())));
        }
        return token.toString();
    }
    
    public String OnAuthorizationCallback(Request request, Response response)
    {
        var r = request.raw().getRequestURL().toString() + "?" + request.raw().getQueryString();
        var codeResponse = new AuthorizationCodeResponseUrl(r);
        var jf = JacksonFactory.getDefaultInstance();
        var net = new NetHttpTransport();
        var coll = new ArrayList<String>();
        coll.add("https://www.googleapis.com/auth/userinfo.email");
        coll.add("https://www.googleapis.com/auth/plus.me");
        coll.add("https://www.googleapis.com/auth/plus.login");
        coll.add("profile");
        coll.add("email");
        var rs  = new GoogleAuthorizationCodeTokenRequest(
                net,
                jf,
                "480347143153-k0q7pdjdsb67tom36vpi4jaesct5cpd3.apps.googleusercontent.com",
                "NkRWXwq4VnD1Ob3MDhe2tfzf",
                codeResponse.getCode(),
                "http://localhost:8080/oauth/google/callback" );
        try {
            var res = rs.execute();
            var corelationToken = this.getToken(5);
            this.DatabaseClient.SaveCorelationToken(res.getAccessToken(),corelationToken);
            this.SetSession(request,res.getAccessToken());
            return "Hi, you are authorized to use this app, copy this code: '" + corelationToken + "'";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "There was some problem. Try again.";
    }

}
