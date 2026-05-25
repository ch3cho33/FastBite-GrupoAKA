package com.fastbite.persistence;

import com.fastbite.exception.PersistenciaException;
import com.fastbite.model.MovimientoPuntos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


 // Repository: Gestiona la persistencia del historial de puntos en JSON.
 // Archivo: datos/movimientos_puntos.json

public class MovimientoPuntosRepository {

    private static final String ARCHIVO = "datos/movimientos_puntos.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MovimientoPuntosRepository() {
        try {
            Files.createDirectories(Paths.get("datos/"));
        } catch (IOException e) {
            throw new PersistenciaException("datos/", "No se pudo crear el directorio", e);
        }
    }

    public void guardar(ArrayList<MovimientoPuntos> movimientos) {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            GSON.toJson(movimientos, writer);
        } catch (IOException e) {
            throw new PersistenciaException(ARCHIVO,
                    "No se pudo guardar el historial de puntos", e);
        }
    }

    public ArrayList<MovimientoPuntos> cargarTodos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(archivo)) {
            Type tipo = new TypeToken<ArrayList<MovimientoPuntos>>() {}.getType();
            ArrayList<MovimientoPuntos> lista = GSON.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[MovimientoPuntosRepository] Error cargando: "
                    + e.getMessage());
            return new ArrayList<>();
        }
    }
}