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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
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

    @Override
    public Map<String, String[]> getRequestParametersMap() {
        HttpServletRequest httpRequest = obtenerHttpServletRequest();
        return httpRequest.getParameterMap();
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
    public void addErrorMessage(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        if (messages.size() == 1) {
            addErrorMessage(messages.get(0));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (String message : messages) {
            sb.append("<li>").append(message).append("</li>");
        }
        sb.append("</ul>");
        addErrorMessage(sb.toString());
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
        String contentDispositionHeader = "attachment; filename=\"@filename\"".replace("@filename", fileName);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Disposition", contentDispositionHeader);
        download(archivoInputStream, contentType, headers);
    }

    @Override
    public void download(InputStream archivoInputStream, String contentType, Map<String, String> headerMap) throws IOException {
        File fileTemp = File.createTempFile(UUID.randomUUID().toString(), ".download.tmp");
        try (OutputStream outputStream = new FileOutputStream(fileTemp)) {
            IOUtils.copy(archivoInputStream, outputStream);
            download(fileTemp, contentType, headerMap);
        } finally {
            try {
                Files.delete(fileTemp.toPath());
            } catch (IOException ex) {
                LOG.warn("No se pudo eliminar el archivo temporal: {}", fileTemp);
            }
        }
    }

    @Override
    public void download(File archivo, String fileName, String contentType) throws IOException {
        String contentDispositionHeader = "attachment; filename=\"@filename\"".replace("@filename", fileName);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Disposition", contentDispositionHeader);
        download(archivo, contentType, headers);
    }

    @Override
    public void download(File archivo, String contentType, Map<String, String> headerMap) throws IOException {
        //cookie primefaces DOWNLOAD

        FacesContext facesContext = this.getFacesContextCurrentInstance();
        facesContext.getExternalContext().addResponseCookie(
                org.primefaces.util.Constants.DOWNLOAD_COOKIE,
                "true",
                Collections.<String, Object>emptyMap()
        );

        //download

        HttpServletResponse httpResponse = this.obtenerHttpServletResponse();
        for (Map.Entry<String, String> header : headerMap.entrySet()) {
            httpResponse.setHeader(header.getKey(), header.getValue());
        }

        httpResponse.setContentType(contentType);
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

    @Override
    public String getBundleMsg(String bundleAlias, String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, bundleAlias);
        return bundle.getString(key);
    }

    @Override
    public String getBundleMsg(String bundleAlias, String key, Object... params) {
        String msg = getBundleMsg(bundleAlias, key);
        return MessageFormat.format(msg, params);
    }

    @Override
    public String getBundleMsg(String baseName, String key, Locale locale) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, loader);
        return bundle.getString(key);
    }

    @Override
    public ResourceBundle getResourceBundle(String bundleAlias) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getResourceBundle(context, bundleAlias);
    }

}