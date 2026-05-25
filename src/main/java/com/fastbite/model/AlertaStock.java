package com.fastbite.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


 // Entity: Representa una alerta generada cuando un ingrediente baja su stock por debajo del nivel mínimo.

public class AlertaStock {

    public enum NivelAlerta {
        CRITICO("Crítico"),
        BAJO("Bajo"),
        INFORMATIVO("Informativo");

        private final String descripcion;
        NivelAlerta(String d) { this.descripcion = d; }
        public String getDescripcion() { return descripcion; }

        @Override
        public String toString() { return descripcion; }
    }

    private String id;
    private String ingredienteId;
    private String nombreIngrediente;
    private String mensaje;
    private NivelAlerta nivel;
    private LocalDateTime fechaGeneracion;
    private boolean resuelta;

    public AlertaStock() {
        this.id = UUID.randomUUID().toString();
        this.fechaGeneracion = LocalDateTime.now();
        this.resuelta = false;
    }

    public AlertaStock(Ingrediente ingrediente) {
        this();
        this.ingredienteId = ingrediente.getId();
        this.nombreIngrediente = ingrediente.getNombre();

        // Determina nivel según qué tan bajo está el stock
        double porcentaje = ingrediente.getStockMinimo() > 0
                ? ingrediente.getCantidad() / ingrediente.getStockMinimo()
                : 1.0;

        if (porcentaje <= 0.25) {
            this.nivel = NivelAlerta.CRITICO;
            this.mensaje = "¡CRÍTICO! Stock de '" + ingrediente.getNombre()
                    + "' es " + String.format("%.2f", ingrediente.getCantidad())
                    + " " + ingrediente.getUnidad() + " (mín: "
                    + ingrediente.getStockMinimo() + ").";
        } else if (porcentaje <= 0.75) {
            this.nivel = NivelAlerta.BAJO;
            this.mensaje = "Stock bajo de '" + ingrediente.getNombre()
                    + "': " + String.format("%.2f", ingrediente.getCantidad())
                    + " " + ingrediente.getUnidad() + " disponibles.";
        } else {
            this.nivel = NivelAlerta.INFORMATIVO;
            this.mensaje = "Revisar stock de '" + ingrediente.getNombre() + "'.";
        }
    }

    public void resolver() {
        this.resuelta = true;
    }

    public String getFechaFormateada() {
        return fechaGeneracion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIngredienteId() { return ingredienteId; }
    public void setIngredienteId(String id) { this.ingredienteId = id; }

    public String getNombreIngrediente() { return nombreIngrediente; }
    public void setNombreIngrediente(String n) { this.nombreIngrediente = n; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public NivelAlerta getNivel() { return nivel; }
    public void setNivel(NivelAlerta nivel) { this.nivel = nivel; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime f) { this.fechaGeneracion = f; }

    public boolean isResuelta() { return resuelta; }
    public void setResuelta(boolean resuelta) { this.resuelta = resuelta; }

    @Override
    public String toString() {
        return "[" + nivel + "] " + mensaje;
    }
}