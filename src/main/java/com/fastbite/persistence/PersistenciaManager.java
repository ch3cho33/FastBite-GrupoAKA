package com.fastbite.persistence;

import com.fastbite.exception.PersistenciaException;
import com.fastbite.model.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


 // Gestiona la persistencia en archivos JSON para FastBite.

public class PersistenciaManager {

    private static final String DIRECTORIO_DATOS = "datos/";
    private static final String ARCHIVO_PEDIDOS = DIRECTORIO_DATOS + "pedidos.json";
    private static final String ARCHIVO_INVENTARIO  = DIRECTORIO_DATOS + "inventario.json";
    private static final String ARCHIVO_MOVIMIENTOS = DIRECTORIO_DATOS + "movimientos.json";
    private static final String ARCHIVO_ALERTAS = DIRECTORIO_DATOS + "alertas.json";
    private static final String ARCHIVO_PRODUCTOS = DIRECTORIO_DATOS + "productos.json";
    private static final String ARCHIVO_CLIENTES = DIRECTORIO_DATOS + "clientes.json";

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private static PersistenciaManager instancia;

    private PersistenciaManager() {
        crearDirectorioSiNoExiste();
    }

    public static PersistenciaManager getInstance() {
        if (instancia == null) {
            instancia = new PersistenciaManager();
        }
        return instancia;
    }

    private void crearDirectorioSiNoExiste() {
        try {
            Files.createDirectories(Paths.get(DIRECTORIO_DATOS));
        } catch (IOException e) {
            throw new PersistenciaException(DIRECTORIO_DATOS,
                    "No se pudo crear el directorio de datos", e);
        }
    }

    // Pedidos

    public void guardarPedidos(List<Pedido> pedidos) {
        guardarJson(ARCHIVO_PEDIDOS, pedidos);
    }

    public List<Pedido> cargarPedidos() {
        Type tipo = new TypeToken<List<Pedido>>() {}.getType();
        List<Pedido> lista = cargarJson(ARCHIVO_PEDIDOS, tipo);
        return lista != null ? lista : new ArrayList<>();
    }

    // Inventario

    public void guardarIngredientes(List<Ingrediente> ingredientes) {
        guardarJson(ARCHIVO_INVENTARIO, ingredientes);
    }

    public List<Ingrediente> cargarIngredientes() {
        Type tipo = new TypeToken<List<Ingrediente>>() {}.getType();
        List<Ingrediente> lista = cargarJson(ARCHIVO_INVENTARIO, tipo);
        return lista != null ? lista : new ArrayList<>();
    }

    // Movimientos

    public void guardarMovimientos(List<MovimientoInventario> movimientos) {
        guardarJson(ARCHIVO_MOVIMIENTOS, movimientos);
    }

    public List<MovimientoInventario> cargarMovimientos() {
        Type tipo = new TypeToken<List<MovimientoInventario>>() {}.getType();
        List<MovimientoInventario> lista = cargarJson(ARCHIVO_MOVIMIENTOS, tipo);
        return lista != null ? lista : new ArrayList<>();
    }

    // Alertas

    public void guardarAlertas(List<AlertaStock> alertas) {
        guardarJson(ARCHIVO_ALERTAS, alertas);
    }

    public List<AlertaStock> cargarAlertas() {
        Type tipo = new TypeToken<List<AlertaStock>>() {}.getType();
        List<AlertaStock> lista = cargarJson(ARCHIVO_ALERTAS, tipo);
        return lista != null ? lista : new ArrayList<>();
    }

    // Productos

    public void guardarProductos(List<Producto> productos) {
        guardarJson(ARCHIVO_PRODUCTOS, productos);
    }

    public List<Producto> cargarProductos() {
        Type tipo = new TypeToken<List<Producto>>() {}.getType();
        List<Producto> lista = cargarJson(ARCHIVO_PRODUCTOS, tipo);
        return lista != null ? lista : new ArrayList<>();
    }

    // Clientes

    public void guardarClientes(List<Object> clientes) {
        guardarJson(ARCHIVO_CLIENTES, clientes);
    }

    public <T> List<T> cargarClientes(Class<T> claseCliente) {
        Type tipo = TypeToken.getParameterized(List.class, claseCliente).getType();
        List<T> lista = cargarJson(ARCHIVO_CLIENTES, tipo);
        return lista != null ? lista : new ArrayList<>();
    }

    // utlidades

    private void guardarJson(String archivo, Object objeto) {
        try (Writer writer = new FileWriter(archivo)) {
            GSON.toJson(objeto, writer);
        } catch (IOException e) {
            throw new PersistenciaException(archivo,
                    "Error al guardar el archivo", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T cargarJson(String archivo, Type tipo) {
        File file = new File(archivo);
        if (!file.exists()) return null;
        try (Reader reader = new FileReader(file)) {
            return GSON.fromJson(reader, tipo);
        } catch (IOException e) {
            throw new PersistenciaException(archivo,
                    "Error al leer el archivo", e);
        } catch (JsonSyntaxException e) {
            throw new PersistenciaException(archivo,
                    "El archivo JSON está corrupto o tiene formato inválido", e);
        }
    }

    //Date Time

    private static class LocalDateTimeAdapter
            implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

        private static final DateTimeFormatter FORMATTER =
                DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(src.format(FORMATTER));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), FORMATTER);
        }
    }
}