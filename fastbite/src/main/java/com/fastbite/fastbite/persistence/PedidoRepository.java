package com.fastbite.fastbite.persistence;

import com.fastbite.fastbite.model.Pedido;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class PedidoRepository {

    private static final String CARPETA = "C:\\Users\\Pc\\Documents\\codigos\\ads\\savedata";
    private static final String ARCHIVO = CARPETA + "\\pedidos.json";

    // Formato de fecha para guardar LocalDateTime como texto simple
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Gson gson;

    public PedidoRepository() {
        // Adaptador que convierte LocalDateTime a texto y viceversa
        TypeAdapter<LocalDateTime> adaptadorFecha = new TypeAdapter<LocalDateTime>() {

            @Override
            public void write(JsonWriter out, LocalDateTime value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value.format(FORMATO)); // Guarda como "2026-05-18 22:02:00"
                }
            }

            @Override
            public LocalDateTime read(JsonReader in) throws IOException {
                String texto = in.nextString();
                return LocalDateTime.parse(texto, FORMATO); // Convierte el texto de vuelta
            }
        };

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, adaptadorFecha) // Registra el adaptador
                .create();

        // Crea la carpeta savedata si no existe
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
            System.out.println("Carpeta creada: " + CARPETA);
        }
    }

    public void guardarPedidos(ArrayList<Pedido> pedidos) {
        try {
            FileWriter writer = new FileWriter(ARCHIVO);
            gson.toJson(pedidos, writer);
            writer.close();
            System.out.println("Pedidos guardados en " + ARCHIVO);
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public ArrayList<Pedido> cargarPedidos() {
        try {
            FileReader reader = new FileReader(ARCHIVO);

            Type tipo = new TypeToken<ArrayList<Pedido>>() {}.getType();
            ArrayList<Pedido> pedidos = gson.fromJson(reader, tipo);
            reader.close();

            if (pedidos == null) {
                return new ArrayList<>();
            }

            System.out.println("Pedidos cargados desde " + ARCHIVO);
            return pedidos;

        } catch (IOException e) {
            System.out.println("No se encontró " + ARCHIVO + ". Lista vacía.");
            return new ArrayList<>();
        }
    }
}