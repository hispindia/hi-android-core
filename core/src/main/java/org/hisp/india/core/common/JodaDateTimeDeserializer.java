package org.hisp.india.core.common;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.lang.reflect.Type;

/**
 * Created by nhancao on 5/5/17.
 */
public class JodaDateTimeDeserializer implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {

    @Override
    public DateTime deserialize(final JsonElement je, final Type type,
                                final JsonDeserializationContext jdc) throws JsonParseException {
        long dateAsString = je.getAsLong();

        return new DateTime(dateAsString).withZone(DateTimeZone.UTC);
    }

    @Override
    public JsonElement serialize(final DateTime src, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        return new JsonPrimitive(src == null ? 0 : src.getMillis());
    }
}
