package cl.zeke.framework.jsf.converters.jsonconverter;

/**
 * Created by takeda on 03-01-16.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Excluido {
    // Field tag only annotation
}
