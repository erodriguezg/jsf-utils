package cl.zeke.framework.jsf.converters;

/**
 * Created by takeda on 03-01-16.
 */

import org.junit.Test;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RutConverterTest {


    @Test
    public void testGetAsObject() {
        Converter converter = new RutConverter();

        // casos donde deberia fallar
        String[] rutsInvalidos = {
                "dadddas",
                "dadasdd",
                "15932446-5",
                "15932426-K",
                "11111111-3",
                "11.111.111-2"
        };

        for (String invalido : rutsInvalidos) {
            try {
                Integer numero = (Integer) converter.getAsObject(null, null, invalido);
                fail("Deberia haber fallado. esperado '" + invalido + "'retorno: '" + numero);
            } catch (ConverterException ex) {
            }
        }

        // casos que deberia funcionar

        Integer numero = (Integer) converter.getAsObject(null, null, "15932446-k");
        assertEquals(15932446, (int) numero);

        numero = (Integer) converter.getAsObject(null, null, "15932446-K");
        assertEquals(15932446, (int) numero);

        numero = (Integer) converter.getAsObject(null, null, "15.932.446-k");
        assertEquals(15932446, (int) numero);

        numero = (Integer) converter.getAsObject(null, null, "15.932.446-K");
        assertEquals(15932446, (int) numero);

        numero = (Integer) converter.getAsObject(null, null, "1-9");
        assertEquals(1, (int) numero);

        numero = (Integer) converter.getAsObject(null, null, "10.501.725-1");
        assertEquals(10501725, (int) numero);

        numero = (Integer) converter.getAsObject(null, null, "11.111.111-1");
        assertEquals(11111111, (int) numero);

        numero = (Integer) converter.getAsObject(null, null, null);
        assertEquals(null, numero);

        numero = (Integer) converter.getAsObject(null, null, "");
        assertEquals(null, numero);

        numero = (Integer) converter.getAsObject(null, null, "   ");
        assertEquals(null, numero);

    }

    @Test
    public void testGetAsString() {
        Converter converter = new RutConverter();

        //casos que deberia funciona

        String resultado = converter.getAsString(null, null, 15932446);
        assertEquals("15.932.446-K", resultado);

        resultado = converter.getAsString(null, null, 1);
        assertEquals("1-9", resultado);

        resultado = converter.getAsString(null, null, 10501725);
        assertEquals("10.501.725-1", resultado);

        resultado = converter.getAsString(null, null, null);
        assertEquals(null, resultado);
    }

}
