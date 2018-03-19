package com.github.erodriguezg.jsfutils.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public interface JsfUtils {

    void salvarMensajesJSFenFlash();

    void jsfRedirect(String viewId, Map<String, String[]> parameters);

    Map<String,String[]> getRequestParametersMap();

    String[] getRequestParameterValues(String paramName);

    String getRequestParameter(String paramName);

    void setRequestAttribute(String key, Object object);

    Object getRequestAttribute(String key);

    void addInfoMessage(String message);

    void addInfoMessage(String componentId, String messsage);

    FacesMessage createInfoMessage(String message);

    void addErrorMessage(String message);

    void addErrorMessage(String componentId, String messsage);

    FacesMessage createErrorMessage(String message);

    HttpServletRequest obtenerHttpServletRequest();

    HttpServletResponse obtenerHttpServletResponse();

    void setFlashAttribute(String key, Object object);

    Object getFlashAttribute(String key);

    void setSessionAttribute(String key, Object object);

    Object getSessionAttribute(String key);

    void setCookie(String name, String value);

    String getCookie(String name);

    void removeSessionAttribute(String key);

    HttpSession obtenerHttpSession();

    Flash obtenerFlash();

    Object getSpElAttribute(String spEl);

    void setSpElAttribute(String spEl, Object object);

    void download(InputStream archivo, String fileName, String contentType) throws IOException;

    void download(File archivo, String fileName, String contentType) throws IOException;

    FacesContext getFacesContextCurrentInstance();

    String getBundleMsg(String bundleAlias, String key);

    String getBundleMsg(String bundleAlias, String key, Object ... params);

    String getBundleMsg(String baseName, String key, Locale locale);

    ResourceBundle getResourceBundle(String bundleAlias);

}
