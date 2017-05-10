package cl.zeke.framework.jsf.converters.jsonconverter;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by eduardo on 21-02-17.
 */
public class SimpleConverterExclusionStrategy implements ExclusionStrategy {

    private static final Type[] TYPE_PERMITIDOS_ARRAY = new Type[]{
            Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, java.util.Date.class, String.class, BigInteger.class, BigDecimal.class
    };

    public boolean shouldSkipField(FieldAttributes f) {
        boolean esTipoPermitido = Arrays.asList(TYPE_PERMITIDOS_ARRAY).contains(f.getDeclaredType());
        return !esTipoPermitido;
    }

    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }
}
