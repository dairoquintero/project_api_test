package co.com.api.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class PruebaTest {

    @Test
    public void testAddPass() {

        assertEquals("sum two values",  5, Prueba.suma(2,3) );
    }

}