package br.edu.ufape.gobarber.exceptions;

import java.sql.SQLException;

public class DataBaseException extends SQLException {
    public DataBaseException(String message) {
        super(message);
    }
}
