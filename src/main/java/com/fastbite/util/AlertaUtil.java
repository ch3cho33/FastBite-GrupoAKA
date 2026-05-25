package com.fastbite.util;

import com.fastbite.exception.FastBiteException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

//Utilidad centralizada para mostrar alertas JavaFX.

public class AlertaUtil {

    private AlertaUtil() {}

    // Errores

    public static void error(String titulo, String mensaje) {
        mostrar(AlertType.ERROR, titulo, mensaje);
    }

    public static void error(String titulo, FastBiteException ex) {
        mostrar(AlertType.ERROR, titulo,
                "[" + ex.getCodigoError() + "] " + ex.getMessage());
    }

    public static void errorGenerico(Exception ex) {
        mostrar(AlertType.ERROR, "Error inesperado",
                "Ocurrió un error: " + ex.getMessage()
                        + "\n\nSi el problema persiste, contacta al administrador.");
    }

    // Información

    public static void info(String titulo, String mensaje) {
        mostrar(AlertType.INFORMATION, titulo, mensaje);
    }

    public static void exito(String mensaje) {
        mostrar(AlertType.INFORMATION, "Operación exitosa", mensaje);
    }

    // Advertencias

    public static void advertencia(String titulo, String mensaje) {
        mostrar(AlertType.WARNING, titulo, mensaje);
    }

    // Confirmación
    public static boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.YES;
    }

    // Métodos Privados

    private static void mostrar(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}