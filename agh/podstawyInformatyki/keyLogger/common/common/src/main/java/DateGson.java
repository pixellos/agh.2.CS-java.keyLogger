import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class DateGson {
    public static Gson Get()
    {
        var builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        var gson = builder.create();
        return gson;
    }
}
