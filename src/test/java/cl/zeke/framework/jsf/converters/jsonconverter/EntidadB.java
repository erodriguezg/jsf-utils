package cl.zeke.framework.jsf.converters.jsonconverter;

/**
 * Created by takeda on 03-01-16.
 */

import java.util.List;

public class EntidadB {

    private Long id;
    private EntidadA entidadA;
    private List<EntidadA> entidadAList;

    public EntidadA getEntidadA() {
        return entidadA;
    }

    public void setEntidadA(EntidadA entidadA) {
        this.entidadA = entidadA;
    }

    public List<EntidadA> getEntidadAList() {
        return entidadAList;
    }

    public void setEntidadAList(List<EntidadA> entidadAList) {
        this.entidadAList = entidadAList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}