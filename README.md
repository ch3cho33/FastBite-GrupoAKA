# FastBite — Sistema de Gestión de Restaurante

FastBite es un sistema de punto de venta (POS) para restaurantes desarrollado en Java 21 y JavaFX. El proyecto fue realizado para el curso ADS de la Pontificia Universidad Javeriana por el Grupo AKA.

El sistema permite gestionar pedidos, cocina, inventario, clientes y productos en una sola aplicación. Además, implementa persistencia en archivos JSON y sigue una arquitectura MVC usando el patrón Entity–Boundary–Control (EBC).

---

## Integrantes

- Sergio Pulido Grimaldo
- Santiago Baez
- Johangel Carvajal Perez
- Nassin Suz Soler
- Juan Sebastian Martinez

---

## Objetivo del proyecto

El propósito de FastBite es digitalizar el flujo básico de operación de un restaurante:

- Registrar pedidos de clientes
- Gestionar pagos
- Controlar el estado de preparación en cocina
- Actualizar automáticamente el inventario
- Administrar productos del menú
- Manejar clientes y sistema de fidelización

El sistema fue construido aplicando principios de programación orientada a objetos, GRASP, MVC y persistencia de datos.

---

## Tecnologías utilizadas

- Java 21
- JavaFX 21 + FXML
- Maven
- Gson 2.10.1
- Git y GitHub
- IntelliJ IDEA
- Draw.io

---

## Cómo ejecutar el proyecto

Requisitos:

- Java 21 o superior
- Maven 3.8+

Clonar el repositorio:

```bash
git clone https://github.com/ch3cho33/FastBite-GrupoAKA.git
cd FastBite-GrupoAKA
```

Ejecutar el proyecto:

```bash
mvn javafx:run
```

La primera ejecución puede tardar un poco porque Maven descarga las dependencias necesarias.

---

## Funcionalidades principales

### Gestión de pedidos

El cajero puede:

- Ver el menú disponible
- Buscar productos por nombre
- Filtrar productos por categoría
- Agregar productos al carrito
- Modificar cantidades
- Ver el total actualizado automáticamente
- Procesar pagos

El sistema calcula automáticamente:

- Subtotal
- IVA del 19%
- Total final
- Cambio en pagos en efectivo

---

### Cocina

El módulo de cocina permite:

- Ver pedidos pendientes
- Cambiar el estado de los pedidos
- Marcar productos como preparados

Flujo de estados:

Pendiente → En preparación → Listo → Entregado

Cuando un pedido se marca como listo, el inventario se actualiza automáticamente.

---

### Inventario

El sistema administra ingredientes y stock.

Funciones principales:

- Registrar ingredientes
- Controlar cantidades disponibles
- Definir stock mínimo
- Generar alertas automáticas
- Registrar movimientos de entrada y salida

Tipos de alerta:

- Stock bajo
- Stock crítico

---

### Clientes y fidelización

El sistema incluye registro de clientes y acumulación de puntos.

Características:

- Validación de correos duplicados
- Historial de puntos
- Categorías automáticas según puntos acumulados

Categorías:

- Bronce
- Plata
- Oro

---

### Gestión de productos

Permite administrar el menú del restaurante mediante operaciones CRUD:

- Crear productos
- Editar productos
- Eliminar productos
- Activar o desactivar disponibilidad

Cada producto tiene:

- Nombre
- Descripción
- Precio
- Categoría

---

## Arquitectura del sistema

El proyecto utiliza arquitectura MVC junto con el patrón EBC.

### Model

Contiene las entidades del dominio:

- Pedido
- Producto
- Cliente
- Ingrediente
- Inventario
- Pago
- EstadoPedido

---

### Controller

Contiene la lógica de negocio del sistema.

Algunos controladores:

- PedidoController
- CocinaController
- InventarioController
- PagoController
- ClienteController

---

### View

La interfaz gráfica fue desarrollada con JavaFX y FXML.

Las vistas:

- Capturan eventos del usuario
- Muestran información
- Delegan la lógica a los controllers

---

### Persistencia

La información se guarda automáticamente en archivos JSON dentro de la carpeta:

```text
datos/
```

Archivos principales:

- pedidos.json
- productos.json
- inventario.json
- clientes.json
- movimientos.json

La persistencia se implementa usando Gson.

---

## Manejo de excepciones

El sistema implementa excepciones personalizadas para controlar errores comunes.

Ejemplos:

- ValidacionException
- StockInsuficienteException
- PersistenciaException
- PedidoNotFoundException

Las alertas y mensajes al usuario se manejan mediante `AlertaUtil`.

---

## Estructura del proyecto

```text
FastBite/
├── src/main/java/com/fastbite/
│   ├── model/
│   ├── controller/
│   ├── persistence/
│   ├── exception/
│   ├── util/
│   └── MainApp.java
│
├── src/main/resources/com/fastbite/views/
│
├── datos/
│
└── pom.xml
```

---

## Persistencia de datos

Los datos permanecen guardados entre ejecuciones.

Si el sistema inicia sin archivos JSON existentes, se cargan automáticamente datos de prueba para facilitar las demostraciones y pruebas del sistema.

---

## Principios aplicados

Durante el desarrollo se aplicaron:

- Programación orientada a objetos
- Arquitectura MVC
- Patrón EBC
- Principios GRASP
- Persistencia de datos
- Separación de responsabilidades

---

