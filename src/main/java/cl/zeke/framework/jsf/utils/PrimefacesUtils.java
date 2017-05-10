package cl.zeke.framework.jsf.utils;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import javax.faces.application.FacesMessage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by eduardo on 05-01-16.
 */
public interface PrimefacesUtils {

    void execute(String script);

    void addCallbackParam(String name, Object value);

    void closeDialog(Object data);

    boolean isAjaxRequest();

    void openDialog(String outcome);

    void openDialog(String outcome, Map<String, Object> options, Map<String, List<String>> params);

    void scrollTo(String clientId);

    void showMessageInDialog(FacesMessage facesMessage);

    void update(String name);

    void update(Collection<String> collection);

    File fileUploadEventToTmpFile(FileUploadEvent event) throws IOException;

    RequestContext getRequestContextCurrentInstance();

}
