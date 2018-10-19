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
        //secure("server/src/cert/keystore.jks", "PASSWORD", null,null);
        var schema = "http";
        var baseUrl ="127.0.0.1";
        ipAddress(baseUrl);
        var port = "8080";
        baseUrl = baseUrl + ":" + port + "/";
        port(8080);
        Spark.before((request, response) -> {
            // Todo: Add user request logging
        });
        var dc = new DatabaseClient();
        var gc = new GoogleClient();
        dc.Initialize();

        var ac = new AuthorizationController(dc, gc, baseUrl, schema);
        var cc = new ClientController(dc, gc, baseUrl, schema);

        cc.RegisterPaths();
        ac.RegisterPaths();

        get("/list/admin", (req, res) -> KeyLogsEntry.ToHtmlString(dc.GetLogs().stream()));

    }
}


