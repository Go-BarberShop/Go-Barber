package br.edu.ufape.gobarber.exceptions;


public class AppointmentException extends IllegalArgumentException{
    public AppointmentException(String message) {
        super(message);
    }
}
