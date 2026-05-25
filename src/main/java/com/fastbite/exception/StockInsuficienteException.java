package com.fastbite.exception;


 // Lanzada cuando un ingrediente no tiene suficiente stock para completar una operación.

public class StockInsuficienteException extends FastBiteException {

    private final String nombreIngrediente;
    private final double stockDisponible;
    private final double stockRequerido;

    public StockInsuficienteException(String nombreIngrediente,
                                      double stockDisponible,
                                      double stockRequerido) {
        super("STOCK_INSUFICIENTE",
                "Stock insuficiente para '" + nombreIngrediente + "'. "
                        + "Disponible: " + stockDisponible
                        + " | Requerido: " + stockRequerido);
        this.nombreIngrediente = nombreIngrediente;
        this.stockDisponible = stockDisponible;
        this.stockRequerido = stockRequerido;
    }

    public String getNombreIngrediente() { return nombreIngrediente; }
    public double getStockDisponible() { return stockDisponible; }
    public double getStockRequerido() { return stockRequerido; }
}