package com.github.erodriguezg.jsfutils.converters;

import com.github.erodriguezg.javautils.RutConverterException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class RutConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) throws ConverterException {
        try {
            return new com.github.erodriguezg.javautils.RutConverter().asInteger(value);
        } catch (RutConverterException ex) {
            FacesMessage message = new FacesMessage();
            message.setDetail(ex.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(message);
        }
    }

    public String getAsString(FacesContext context, UIComponent component,
                              Object objectRut) throws ConverterException {
        try {
            return new com.github.erodriguezg.javautils.RutConverter().asString((Integer) objectRut);
        } catch (RutConverterException ex) {
            FacesMessage message = new FacesMessage();
            message.setDetail(ex.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(message);
        }
    }
}
