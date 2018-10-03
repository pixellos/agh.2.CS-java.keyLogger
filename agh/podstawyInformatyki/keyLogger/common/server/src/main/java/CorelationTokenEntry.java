import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "corelationTokenEntries")
public class CorelationTokenEntry{
    @DatabaseField(generatedId = true)
    public int Id;

    @DatabaseField(canBeNull = false)
    public String GoogleApiKey;

    @DatabaseField(canBeNull = false)
    public String CorrelationToken;
}
