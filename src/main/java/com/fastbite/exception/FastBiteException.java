package com.fastbite.exception;


 // Excepción base del sistema FastBite.
 // Todas las excepciones de negocio heredan de esta clase.

public class FastBiteException extends RuntimeException {

    private final String codigoError;

    public FastBiteException(String mensaje) {
        super(mensaje);
        this.codigoError = "GENERAL";
    }

    public FastBiteException(String codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public FastBiteException(String codigoError, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }
}