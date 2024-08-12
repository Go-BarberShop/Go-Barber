package br.edu.ufape.gobarber.exceptions;

import java.sql.SQLException;

public class DataBaseConstraintException extends SQLException {
    public DataBaseConstraintException(String message) {
        super(message);
    }
}
