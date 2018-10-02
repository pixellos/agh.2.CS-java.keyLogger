import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;
import java.util.Date;

@DatabaseTable(tableName = "keyLogsEntries")
public class KeyLogsEntry {

    @DatabaseField(generatedId = true)
    public int Id;

    @DatabaseField(canBeNull = false)
    public String UserEmail;

    @DatabaseField(canBeNull = false)
    public String Key;

    @DatabaseField(canBeNull = false)
    public Timestamp TimeStamp;
}

