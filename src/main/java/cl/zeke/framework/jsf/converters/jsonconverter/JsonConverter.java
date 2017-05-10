package cl.zeke.framework.jsf.converters.jsonconverter;

/**
 * Created by takeda on 03-01-16.
 */

import cl.zeke.framework.utils.CodecUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zeke
 */
public class JsonConverter implements Converter {

    private static final Logger LOG = LoggerFactory.getLogger(JsonConverter.class);

    private static final String SEPARADOR_JSON = "_JSON_";

    private final CodecUtils codecUtils;

    private ExclusionStrategy exclusionStrategy;

    public JsonConverter() {
        this.codecUtils = new CodecUtils();
    }

    public Object getAsObject(FacesContext fc, UIComponent uic, String objetSerialized) {
        if (objetSerialized == null || objetSerialized.trim().isEmpty()) {
            return null;
        }
        Pattern p = Pattern.compile("^(.*)" + SEPARADOR_JSON + "(.*)");

        try {
            objetSerialized = codecUtils.decodiarBase64(objetSerialized);
        } catch (IOException ex) {
            LOG.error("error", ex);
            throw new ConverterException("Error de conversión");
        }

        Matcher m = p.matcher(objetSerialized);
        if (!m.matches()) {
            return null;
        }
        String className = m.group(1);
        String json = m.group(2);
        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            LOG.error("error", ex);
            throw new ConverterException("Error de conversión");
        }
        Gson gson = createGson();
        return gson.fromJson(json, clazz);
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null) {
            return null;
        }
        Gson gson = createGson();
        String json = gson.toJson(o);
        String clazz = o.getClass().getName();
        return codecUtils.encodiarBase64(clazz + SEPARADOR_JSON + json);
    }

    private Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.util.Date.class, new JavaUtilDateSerializer());
        gsonBuilder.registerTypeAdapter(java.util.Date.class, new JavaUtilDateDeserializer());
        gsonBuilder.registerTypeAdapter(java.sql.Date.class, new SqlDateSerializer());
        gsonBuilder.registerTypeAdapter(java.sql.Date.class, new SqlDateDeserializer());
        gsonBuilder.registerTypeAdapter(java.sql.Time.class, new TimeSerializer());
        gsonBuilder.registerTypeAdapter(java.sql.Time.class, new TimeDeserializer());
        if(this.exclusionStrategy != null) {
            gsonBuilder.setExclusionStrategies(this.exclusionStrategy);
        }
        return gsonBuilder.create();
    }

    private class JavaUtilDateSerializer implements JsonSerializer<java.util.Date> {
        public JsonElement serialize(Date t, Type type, JsonSerializationContext jsc) {
            if (t == null) {
                return null;
            }
            return new JsonPrimitive(t.getTime());
        }
    }

    private class JavaUtilDateDeserializer implements JsonDeserializer<java.util.Date> {
        public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            if (json == null) {
                return null;
            }
            return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
        }
    }

    private class SqlDateSerializer implements JsonSerializer<java.sql.Date> {
        public JsonElement serialize(java.sql.Date t, Type type, JsonSerializationContext jsc) {
            if (t == null) {
                return null;
            }
            return new JsonPrimitive(t.getTime());
        }
    }

    private class SqlDateDeserializer implements JsonDeserializer<java.sql.Date> {
        public java.sql.Date deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            if (json == null) {
                return null;
            }
            return new java.sql.Date(json.getAsJsonPrimitive().getAsLong());
        }
    }

    private class TimeSerializer implements JsonSerializer<java.sql.Time> {
        public JsonElement serialize(java.sql.Time t, Type type, JsonSerializationContext jsc) {
            if (t == null) {
                return null;
            }
            return new JsonPrimitive(t.getTime());
        }
    }

    private class TimeDeserializer implements JsonDeserializer<java.sql.Time> {
        public java.sql.Time deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            if (json == null) {
                return null;
            }
            return new java.sql.Time(json.getAsJsonPrimitive().getAsLong());
        }
    }

    public ExclusionStrategy getExclusionStrategy() {
        return exclusionStrategy;
    }

    public void setExclusionStrategy(ExclusionStrategy exclusionStrategy) {
        this.exclusionStrategy = exclusionStrategy;
    }
}
