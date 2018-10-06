import com.google.gson.Gson;

import java.util.Date;

import static spark.Spark.path;
import static spark.Spark.post;

public class ClientController {

    private DatabaseClient DatabaseClient;
    private GoogleClient GoogleClient;
    private String BaseUrl;

    public ClientController(DatabaseClient dc, GoogleClient gc, String baseUrl) {
        this.DatabaseClient = dc;
        this.GoogleClient = gc;
        BaseUrl = baseUrl;
    }

    public void RegisterPaths() {
        var g = DateGson.Get();
        path("/client", () -> {
            post("/report/key", (req, res) -> {
                var body = req.body();
                var erm = g.fromJson(body, EventRequestModel.class);
                var gkey =this.DatabaseClient.GetGoogleKey(erm.ApiKey);
                if(gkey == null)
                {
                    return "";
                }
                var user = this.GoogleClient.GetPerson(gkey);
                if(user != null)
                {
                    var email = user.getEmails().get(0).getValue();
                    this.DatabaseClient.LogUserKeyPress(email, erm.Key, erm.Date);
                    System.out.println("Key " + erm.Key + " was hit on client with id " + erm.ApiKey + ".");
                }

                return "";
            });

            post("/authorize", (req, res) -> {
                var body = req.body();
                var userKey = g.fromJson(body, AuthorizeRequestModel.class);
                var key = this.DatabaseClient.GetGoogleKey(userKey.Key);

                var user = this.GoogleClient.GetPerson(key);
                if(user == null)
                {
                    return "";
                }
                var response = new AuthorizeResponseModel();

                if (user != null) {
                    response.ApiKey = key;
                    response.Email = user.getEmails().get(0).getValue();
                    response.Name =  user.getDisplayName();
                    return g.toJson(response);
                }
                return "";
            });
        });
    }
}
