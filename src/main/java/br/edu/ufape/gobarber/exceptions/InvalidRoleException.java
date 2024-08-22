package br.edu.ufape.gobarber.exceptions;

public class InvalidRoleException extends IllegalArgumentException{
    public InvalidRoleException(String s) {
        super(s);
    }
}
