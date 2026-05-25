package com.fastbite.exception;


 // Lanzada cuando los datos ingresados por el usuario no son válidos.

public class ValidacionException extends FastBiteException {

    private final String campo;

    public ValidacionException(String campo, String mensaje) {
        super("VALIDACION_ERROR", "Campo '" + campo + "': " + mensaje);
        this.campo = campo;
    }

    public String getCampo() { return campo; }
}