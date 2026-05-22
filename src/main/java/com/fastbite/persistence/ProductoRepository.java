package com.fastbite.persistence;
import com.fastbite.model.Producto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class ProductoRepository {
    private static final String ARCHIVO = "productos.json";
    private Gson gson;
    public ProductoRepository() {
        gson = new Gson();
    }

    public void guardar(List<Producto> productos) {
        try (FileWriter writer = new FileWriter(ARCHIVO)) {
            gson.toJson(productos, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Producto> cargar() {
        try (FileReader reader = new FileReader(ARCHIVO)) {
            Type tipoLista = new TypeToken<List<Producto>>() {}.getType();
            return gson.fromJson(reader, tipoLista);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
