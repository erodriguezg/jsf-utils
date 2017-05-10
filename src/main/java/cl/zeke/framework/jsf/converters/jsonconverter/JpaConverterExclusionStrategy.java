package cl.zeke.framework.jsf.converters.jsonconverter;

import com.google.gson.FieldAttributes;

import javax.persistence.*;

/**
 * Created by eduardo on 21-02-17.
 */
public class JpaConverterExclusionStrategy extends SimpleConverterExclusionStrategy{

    private static final Class[] ANOTACIONES_PARA_EXCLUSION = new Class[]{
            OneToOne.class, ManyToOne.class, ManyToMany.class, OneToMany.class, Transient.class, Excluido.class
    };

    public boolean shouldSkipField(FieldAttributes f) {
        boolean tieneAnotacionDeExclusion = false;

        for (Class c : ANOTACIONES_PARA_EXCLUSION) {
            if (f.getAnnotation(c) != null) {
                tieneAnotacionDeExclusion = true;
                break;
            }
        }
        return super.shouldSkipField(f) || tieneAnotacionDeExclusion;
    }

    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }
}
