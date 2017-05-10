package com.github.erodriguezg.jsfutils.utils.impl;

/**
 * Created by takeda on 03-01-16.
 */

import com.github.erodriguezg.jsfutils.utils.JsfUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Map;
import java.util.UUID;

public class JsfUtilsImpl implements JsfUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsfUtilsImpl.class);

    @Override
    public void salvarMensajesJSFenFlash() {
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().getFlash().setKeepMessages(true);
    }

    @Override
    public void jsfRedirect(String viewId, Map<String, String[]> parameters) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String redirectUrl = fc.getApplication().getViewHandler().getActionURL(fc, viewId);
        String queryString = "";
        if (parameters != null && !parameters.isEmpty()) {
            queryString = crearQueryString(parameters);
        }
        try {
            fc.getExternalContext().redirect(redirectUrl + queryString);
            fc.responseComplete();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String crearQueryString(Map<String, String[]> parametersMap) {
        StringBuilder queryString = new StringBuilder();
        boolean primeroParametro = true;
        String parametro;
        for (Map.Entry<String, String[]> entry : parametersMap.entrySet()) {
            parametro = entry.getKey();
            if (primeroParametro) {
                queryString.append("?");
                primeroParametro = false;
            } else {
                queryString.append("&");
            }
            queryString.append(parametro);
            boolean primerValorParametro = true;
            for (String valor : entry.getValue()) {
                if (primerValorParametro) {
                    queryString.append("=").append(valor);
                    primerValorParametro = false;
                } else {
                    queryString.append("&").append(parametro).append("=").append(valor);
                }
            }
        }
        return queryString.toString();
    }

    @Override
    public String[] getRequestParameterValues(String paramName) {
        return obtenerHttpServletRequest().getParameterValues(paramName);
    }

    @Override
    public String getRequestParameter(String paramName) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(paramName);
    }

    @Override
    public void setRequestAttribute(String key, Object object) {
        obtenerHttpServletRequest().setAttribute(key, object);
    }

    @Override
    public Object getRequestAttribute(String key) {
        return obtenerHttpServletRequest().getAttribute(key);
    }

    @Override
    public void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    @Override
    public void addInfoMessage(String componentId, String messsage) {
        FacesContext.getCurrentInstance().addMessage(
                componentId,
                new FacesMessage(FacesMessage.SEVERITY_INFO, messsage, null));
    }

    @Override
    public FacesMessage createInfoMessage(String message) {
        return new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
    }

    @Override
    public void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    @Override
    public void addErrorMessage(String componentId, String messsage) {
        FacesContext.getCurrentInstance().addMessage(
                componentId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, messsage, null));
    }

    @Override
    public FacesMessage createErrorMessage(String message) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
    }

    @Override
    public HttpServletRequest obtenerHttpServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    @Override
    public HttpServletResponse obtenerHttpServletResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    @Override
    public void setFlashAttribute(String key, Object object) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow(key, object);
    }

    @Override
    public Object getFlashAttribute(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(key);
    }

    @Override
    public void setSessionAttribute(String key, Object object) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, object);
    }

    @Override
    public Object getSessionAttribute(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }

    @Override
    public void setCookie(String name, String value) {
        HttpServletResponse response = obtenerHttpServletResponse();
        response.addCookie(new Cookie(name, value));
    }

    @Override
    public String getCookie(String name) {
        HttpServletRequest request = obtenerHttpServletRequest();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void removeSessionAttribute(String key) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
    }

    @Override
    public HttpSession obtenerHttpSession() {
        return obtenerHttpServletRequest().getSession();
    }

    @Override
    public Flash obtenerFlash() {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash();
    }

    @Override
    public Object getSpElAttribute(String spEl) {
        String spelAux = "#{@spel}"
                .replace("@spel", spEl);

        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getApplication()
                .getExpressionFactory()
                .createValueExpression(fc.getELContext(), spelAux, Object.class)
                .getValue(fc.getELContext());
    }

    @Override
    public void setSpElAttribute(String spEl, Object object) {
        String spelAux = "#{@spel}"
                .replace("@spel", spEl);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getApplication()
                .getExpressionFactory()
                .createValueExpression(fc.getELContext(), spelAux, Object.class)
                .setValue(fc.getELContext(), object);
    }

    @Override
    public void download(InputStream archivoInputStream, String fileName, String contentType) throws IOException {
        File fileTemp = File.createTempFile(UUID.randomUUID().toString(), ".download.tmp");
        try (OutputStream outputStream = new FileOutputStream(fileTemp)) {
            IOUtils.copy(archivoInputStream, outputStream);
            download(fileTemp, fileName, contentType);
        } finally {
            if (fileTemp != null && !fileTemp.delete()) {
                LOG.warn("No se pudo eliminar el archivo temporal: {}", fileTemp);
            }
        }
    }

    @Override
    public void download(File archivo, String fileName, String contentType) throws IOException {
        HttpServletResponse httpResponse = this.obtenerHttpServletResponse();
        httpResponse.setHeader("Content-Disposition", "attachment; filename=\"@filename\"".replace("@filename", fileName));
        httpResponse.setContentType(fileName);
        try (InputStream inputStream = new FileInputStream(archivo);
             OutputStream outputStream = httpResponse.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    @Override
    public FacesContext getFacesContextCurrentInstance() {
        return FacesContext.getCurrentInstance();
    }
}