package com.github.erodriguezg.jsfutils.converters.jsonconverter;

/**
 * Created by takeda on 03-01-16.
 */

import java.util.Date;

public class EntidadA {

    private Long id;

    private String nombre;

    private Date fechaNacimiento;

    private Float porcentaje;

    private Double numero;

    private Short numeroPequeno;

    private Boolean boleano;

    private String nulo;

    @Excluido
    private String excluyeme;

    private Date fechaSql;

    public EntidadA(Long id, String nombre, Date fechaNacimiento, Float porcentaje,
                    Double numero, Short numeroPequeno, Boolean boleano, String nulo, String excluyeme,
                    Date fechaSql) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.porcentaje = porcentaje;
        this.numero = numero;
        this.numeroPequeno = numeroPequeno;
        this.boleano = boleano;
        this.nulo = nulo;
        this.excluyeme = excluyeme;
        this.fechaSql = fechaSql;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Double getNumero() {
        return numero;
    }

    public void setNumero(Double numero) {
        this.numero = numero;
    }

    public Short getNumeroPequeno() {
        return numeroPequeno;
    }

    public void setNumeroPequeno(Short numeroPequeno) {
        this.numeroPequeno = numeroPequeno;
    }

    public Boolean isBoleano() {
        return boleano;
    }

    public void setBoleano(Boolean boleano) {
        this.boleano = boleano;
    }

    public String getNulo() {
        return nulo;
    }

    public void setNulo(String nulo) {
        this.nulo = nulo;
    }

    public String getExcluyeme() {
        return excluyeme;
    }

    public void setExcluyeme(String excluyeme) {
        this.excluyeme = excluyeme;
    }

    public Date getFechaSql() {
        return fechaSql;
    }

    public void setFechaSql(Date fechaSql) {
        this.fechaSql = fechaSql;
    }
}
