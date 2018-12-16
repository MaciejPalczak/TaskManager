package com.github.maciejpalczak.springtodolist.services;


import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class DateValidator {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private DateValidator() {
    }

    public static boolean isValid(String dateToValidate) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);

        try {
            sdf.parse(dateToValidate);
            return true;

        } catch (ParseException e) {
            return false;
        }
    }
}