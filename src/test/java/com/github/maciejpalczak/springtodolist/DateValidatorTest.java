package com.github.maciejpalczak.springtodolist;

import com.github.maciejpalczak.springtodolist.services.DateValidator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateValidatorTest {
    @Test
    public void testDateIsNull() {
        assertFalse(DateValidator.isValid(null));
    }

    @Test
    public void testDayIsInvalid() {
        assertFalse(DateValidator.isValid("32/02/2012"));
    }

    @Test
    public void testMonthIsInvalid() {
        assertFalse(DateValidator.isValid("31/20/2012"));
    }

    @Test
    public void testYearIsInvalid() {
        assertFalse(DateValidator.isValid("31/20/19991"));
    }

    @Test
    public void testDateFormatIsInvalid() {
        assertFalse(DateValidator.isValid("2012/02/20"));
    }

    @Test
    public void testDateFeb29_2012() {
        assertTrue(DateValidator.isValid("29/02/2012"));
    }

    @Test
    public void testDateFeb29_2011() {
        assertFalse(DateValidator.isValid("29/02/2011"));
    }

    @Test
    public void testDateFeb28() {
        assertTrue(DateValidator.isValid("28/02/2011"));
    }

    @Test
    public void testDateIsValid_1() {
        assertTrue(DateValidator.isValid("31/01/2012"));
    }

    @Test
    public void testDateIsValid_2() {
        assertTrue(DateValidator.isValid("30/04/2012"));
    }

    @Test
    public void testDateIsValid_3() {
        assertTrue(DateValidator.isValid("31/05/2012"));
    }

}
