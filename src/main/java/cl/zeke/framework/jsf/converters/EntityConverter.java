package cl.zeke.framework.jsf.converters;

/**
 * Created by takeda on 03-01-16.
 */

import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().length() == 0) {
            return null;
        }

        List<Object> objetosComponenteList = new ArrayList<Object>();
        if (component instanceof HtmlSelectOneMenu) {
            for (UIComponent child : component.getChildren()) {
                if (child instanceof UISelectItems) {
                    // found it, now get the real SelectItem list
                    UISelectItems items = (UISelectItems) child;
                    List<SelectItem> realitems = (List<SelectItem>) items.getValue();
                    // search for the selected person and return it
                    for (Object item : realitems) {
                        objetosComponenteList.add(item);
                    }
                }
            }
        }

        Class classEntity = getClazz(context, component);

        if (classEntity.getAnnotation(javax.persistence.Entity.class) == null) {
            throw new IllegalArgumentException("No es javax.persistence.Entity!");
        }

        for (Field field : classEntity.getDeclaredFields()) {
            Annotation annotationId = null;
            annotationId = field.getAnnotation(javax.persistence.Id.class);
            if (annotationId == null) {
                annotationId = field.getAnnotation(javax.persistence.EmbeddedId.class);
            }
            if (annotationId == null) {
                continue;
            }

            //tiene el field que esta anotado como id.
            //obtener el nombre del field
            String nombreGetterField = "get" + StringUtils.capitalize(field.getName());
            String nombreSetterField = "set" + StringUtils.capitalize(field.getName());

            Method getterMethod = null;
            Method setterMethod = null;
            try {
                getterMethod = classEntity.getMethod(nombreGetterField, null);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }

            //del getter obtener la Class del Id
            Class<?> classId = getterMethod.getReturnType();

            try {
                setterMethod = classEntity.getMethod(nombreSetterField, classId);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }

            //del getter obtener el valor del Id
            Object instanciaId = obtenerValorDeLaId(classId, value);
            try {
                Object nuevaInstancia = classEntity.newInstance();
                setterMethod.invoke(nuevaInstancia, instanciaId);

                int index = objetosComponenteList.indexOf(nuevaInstancia);
                if (index == -1) return null;
                return objetosComponenteList.get(index);
            } catch (Exception ex) {
                throw new RuntimeException("error al instanciar objeto");
            }

        }

        throw new IllegalArgumentException("No se encontre ID en la Entidad!");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof Integer) {
            return value + "";
        }
        if (value instanceof Long) {
            return value + "";
        }
        if (value instanceof Short) {
            return value + "";
        }

        Class classEntity = value.getClass();

        if (classEntity.getAnnotation(javax.persistence.Entity.class) == null) {
            throw new IllegalArgumentException("No es javax.persistence.Entity!");
        }

        for (Field field : classEntity.getDeclaredFields()) {
            Annotation annotationId = null;
            annotationId = field.getAnnotation(javax.persistence.Id.class);
            if (annotationId == null) {
                annotationId = field.getAnnotation(javax.persistence.EmbeddedId.class);
            }
            if (annotationId == null) {
                continue;
            }

            //tiene el field que esta anotado como id.
            //obtener el nombre del field
            String nombreGetterField = "get" + StringUtils.capitalize(field.getName());
            Method getterMethod = null;
            try {
                getterMethod = classEntity.getMethod(nombreGetterField, null);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }

            //del getter obtener la Class del Id
            Class<?> classId = getterMethod.getReturnType();

            //del getter obtener el valor del Id
            Object objectId;
            try {
                objectId = getterMethod.invoke(value, null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            if (objectId == null) {
                return "";
            }

            //Obtener el Objeto real con classEntity , Class del Id con Valor del Id
            return objectId.toString();
        }

        throw new IllegalArgumentException("No se encontre ID en la Entidad!");
    }

    // Gets the class corresponding to the context in jsf page
    @SuppressWarnings("unchecked")
    private Class getClazz(FacesContext facesContext, UIComponent component) {
        return component.getValueExpression("value").getType(facesContext.getELContext());
    }

    /*
     * Este metodo se puede sobreescribir para crear un converter de una entidad
     * compuesta o no numerica
     */
    protected <T> T obtenerValorDeLaId(Class<T> claseId, String valorCadena) {
        if (claseId.isInstance(new Integer(0))) {
            return claseId.cast(Integer.valueOf(valorCadena));
        } else if (claseId.isInstance(new Long(0))) {
            return claseId.cast(Long.valueOf(valorCadena));
        } else if (claseId.isInstance(new Short((short) 0))) {
            return claseId.cast(Short.valueOf(valorCadena));
        } else if (claseId.isInstance("")) {
            return claseId.cast(String.valueOf(valorCadena));
        }
        throw new RuntimeException("Tipo de dato no manejado por EntityConverter");
    }
}