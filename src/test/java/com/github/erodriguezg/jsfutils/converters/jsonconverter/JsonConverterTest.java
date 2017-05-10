package com.github.erodriguezg.jsfutils.converters.jsonconverter;

/**
 * Created by takeda on 03-01-16.
 */


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

/**
 * @author github
 */
public class JsonConverterTest {

    private JsonConverter converter;

    @Before
    public void init() {
        converter = new JsonConverter();
        converter.setExclusionStrategy(new JpaConverterExclusionStrategy());
    }

    @Test
    public void converterPrimitivasTest() {
        EntidadA entidadA = crearEntidadA();
        String jsonEntidadA = converter.getAsString(null, null, entidadA);
        EntidadA entidadA2 = (EntidadA) converter.getAsObject(null, null, jsonEntidadA);
        Assert.assertTrue(entidadA.getId().equals(entidadA2.getId()));
        Assert.assertTrue(entidadA.getNombre().equals(entidadA2.getNombre()));
        Assert.assertTrue(entidadA.getFechaNacimiento().equals(entidadA2.getFechaNacimiento()));
        Assert.assertTrue(entidadA.getPorcentaje().equals(entidadA2.getPorcentaje()));
        Assert.assertTrue(entidadA.getNumero().equals(entidadA2.getNumero()));
        Assert.assertTrue(entidadA.getNumeroPequeno().equals(entidadA.getNumeroPequeno()));
        Assert.assertTrue(entidadA.isBoleano().equals(entidadA2.isBoleano()));
        Assert.assertTrue(entidadA.getFechaSql().equals(entidadA2.getFechaSql()));
        Assert.assertNotNull(entidadA.getExcluyeme());
        Assert.assertNull(entidadA2.getExcluyeme());
    }

    @Test
    public void converterColeccionesTest() {
        EntidadB b = new EntidadB();
        b.setId(Long.MAX_VALUE);
        b.setEntidadA(crearEntidadA());
        b.setEntidadAList(Arrays.asList(new EntidadA[]{crearEntidadA(), crearEntidadA(), crearEntidadA()}));
        String jsonEntidadB = converter.getAsString(null, null, b);
        EntidadB b2 = (EntidadB) converter.getAsObject(null, null, jsonEntidadB);
        Assert.assertEquals(b.getId(), b2.getId());
        Assert.assertNotNull(b.getEntidadA());
        Assert.assertNull(b2.getEntidadA());
        Assert.assertNotNull(b.getEntidadAList());
        Assert.assertNull(b2.getEntidadAList());
    }

    @Test
    public void converterSeleccionarTest() {
        Object salida = converter.getAsObject(null, null, "Seleccionar");
        Assert.assertNull(salida);
    }

    private EntidadA crearEntidadA() {
        return new EntidadA(23l, "Hola Mundo Que Sucede?! ñáéíóú yo voto XHTML <>&</>",
                new Date(), 1.45f, 0.37463985388, (short) 123, true, null, "esto se excluye",
                new java.sql.Date(new Date().getTime()));
    }

}


