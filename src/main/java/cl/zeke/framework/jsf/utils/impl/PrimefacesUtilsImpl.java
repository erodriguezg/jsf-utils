package cl.zeke.framework.jsf.utils.impl;

import cl.zeke.framework.jsf.utils.PrimefacesUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import javax.faces.application.FacesMessage;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by takeda on 03-01-16.
 */
public class PrimefacesUtilsImpl implements PrimefacesUtils {

    @Override
    public void execute(String script) {
        RequestContext.getCurrentInstance().execute(script);
    }

    @Override
    public void addCallbackParam(String name, Object value) {
        RequestContext.getCurrentInstance().addCallbackParam(name, value);
    }

    @Override
    public void closeDialog(Object data) {
        RequestContext.getCurrentInstance().closeDialog(data);
    }

    @Override
    public boolean isAjaxRequest() {
        return RequestContext.getCurrentInstance().isAjaxRequest();
    }

    @Override
    public void openDialog(String outcome) {
        RequestContext.getCurrentInstance().openDialog(outcome);
    }

    @Override
    public void openDialog(String outcome, Map<String, Object> options, Map<String, List<String>> params) {
        RequestContext.getCurrentInstance().openDialog(outcome, options, params);
    }

    @Override
    public void scrollTo(String clientId) {
        RequestContext.getCurrentInstance().scrollTo(clientId);
    }

    @Override
    public void showMessageInDialog(FacesMessage facesMessage) {
        RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
    }

    @Override
    public void update(String name) {
        RequestContext.getCurrentInstance().update(name);
    }

    @Override
    public void update(Collection<String> collection) {
        RequestContext.getCurrentInstance().update(collection);
    }

    @Override
    public File fileUploadEventToTmpFile(FileUploadEvent event) throws IOException {
        File file = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
        try (InputStream is = event.getFile().getInputstream();
             OutputStream os = new FileOutputStream(file);) {
            IOUtils.copy(is, os);
        }
        return file;
    }

    @Override
    public RequestContext getRequestContextCurrentInstance() {
        return RequestContext.getCurrentInstance();
    }
}
