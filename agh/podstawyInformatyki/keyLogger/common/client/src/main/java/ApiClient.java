import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private final String BaseUrl;
    private final HttpClient Client;
    private final Gson Gson;

    public ApiClient(String baseUrl) {
        this.BaseUrl = baseUrl;
        this.Client = HttpClient.newHttpClient();
        this.Gson = new Gson();

    }

    public void Report(String key, String apiKey) {
        var reportPath = "client/report/key";
        var erm = new EventRequestModel();
        erm.Key = key;
        erm.ApiKey = apiKey;

        var parameter = this.Gson.toJson(erm);
        var request = HttpRequest.newBuilder().uri(URI.create(this.BaseUrl + reportPath))
                .POST(HttpRequest.BodyPublishers.ofString(parameter))
                .build();

        try {
            this.Client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public User Authorize(String apiKey) {
        var authorizePath = "client/authorize";
        var authRequest = new AuthorizeRequestModel();
        authRequest.Key = apiKey;
        var json = this.Gson.toJson(authRequest);

        User result = null;
        var request = HttpRequest.newBuilder()
                .uri(URI.create(this.BaseUrl + authorizePath))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            var responseJson = this.Client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            if(responseJson == "")
            {
                return null;
            }
            var response = this.Gson.fromJson(responseJson, AuthorizeResponseModel.class);

            if (response != null && response.apiKey != null) {
                result = new User();
                result.Authorized = true;
                result.Name = response.name;
                result.ApiKey = response.apiKey;
                result.Name = response.name;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = new User();
            result.Email = "contact@support.com";
            result.Name = "Anonymous";
            result.ApiKey = "Not authorized";
            result.Authorized = false;
        }

        return result;
    }
}
