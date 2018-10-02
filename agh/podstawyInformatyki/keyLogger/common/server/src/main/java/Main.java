import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import spark.Spark;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import static spark.Spark.*;

public class Main {
    public static String AccessToken = "";

    public static void main(String[] args) {
        port(8080);
        ipAddress("127.0.0.1");
        Spark.before((request, response) -> {
            // Todo: Add user request loging
        });
        var dc = new DatabaseClient();
        var gc = new GoogleClient();
        dc.Initialize();

        var ac = new AuthorizationController(dc, gc);
        var cc = new ClientController(dc, gc);

        cc.RegisterPaths();

        get("/list", (req, res) -> dc.GetLogs().stream().map(x -> "Id: " + x.Id + ", Email: " + x.UserEmail  + " Key: " + x.Key + " Stamp:" + x.TimeStamp.toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME)).collect(java.util.stream.Collectors.joining("<br/>")));
        get("/login", (req, res) -> ac.Authorize(req, res));
        get("/login/status", (req, res) -> ac.GetLoginState(req, res));
        get("/oauth/google/callback", (req, res) -> ac.OnAuthorizationCallback(req, res));
    }
}


