package com.fastbite.persistence;

import com.fastbite.exception.PersistenciaException;
import com.fastbite.model.Cliente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


 // Repository: Gestiona la persistencia de Cliente en JSON.
 // Archivo: datos/clientes.json

public class ClienteRepository {

    private static final String ARCHIVO = "datos/clientes.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ClienteRepository() {
        try {
            Files.createDirectories(Paths.get("datos/"));
        } catch (IOException e) {
            throw new PersistenciaException("datos/", "No se pudo crear el directorio", e);
        }
    }

    // Guarda la lista completa de clientes en clientes.json
    public void guardar(ArrayList<Cliente> clientes) {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            GSON.toJson(clientes, writer);
        } catch (IOException e) {
            throw new PersistenciaException(ARCHIVO, "No se pudo guardar los clientes", e);
        }
    }

    //Carga todos los clientes desde clientes.json
    public ArrayList<Cliente> cargarTodos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(archivo)) {
            Type tipo = new TypeToken<ArrayList<Cliente>>() {}.getType();
            ArrayList<Cliente> lista = GSON.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[ClienteRepository] Error cargando: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Retorna el mayor ID existente para continuar el contador
    public int obtenerMaxId() {
        ArrayList<Cliente> lista = cargarTodos();
        return lista.stream()
                .mapToInt(Cliente::getId)
                .max()
                .orElse(0);
    }
}