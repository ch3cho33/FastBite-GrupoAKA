package com.fastbite.exception;

    // Lanzada cuando falla la lectura o escritura de archivos JSON.

public class PersistenciaException extends FastBiteException {

    private final String archivo;

    public PersistenciaException(String archivo, String mensaje, Throwable causa) {
        super("PERSISTENCIA_ERROR",
                "Error en archivo '" + archivo + "': " + mensaje, causa);
        this.archivo = archivo;
    }

    public String getArchivo() { return archivo; }
}