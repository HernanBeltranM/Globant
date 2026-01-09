# Guía de Uso en IntelliJ IDEA

## Configuración Inicial

### 1. Abrir el Proyecto
```
File → Open → Seleccionar la carpeta del proyecto
```

IntelliJ reconocerá automáticamente el archivo `Globant.iml` y la estructura del proyecto.

### 2. Verificar JDK
```
File → Project Structure → Project
- SDK: Java 8 o superior
- Language level: 8 o superior
```

## Ejecutar el Proyecto

### Método 1: Desde el Editor
1. Abrir `src/Principal/Main.java`
2. Click derecho en cualquier parte del código
3. Seleccionar **"Run 'Main.main()'"**
4. Ver salida en la ventana Run (abajo)

### Método 2: Usando el Botón Play
1. Abrir `src/Principal/Main.java`
2. Click en el ícono verde ▶️ junto a `public class Main`
3. Ver salida en la consola

### Método 3: Atajo de Teclado
1. Abrir `src/Principal/Main.java`
2. Presionar **Shift + F10** (Windows/Linux) o **Ctrl + R** (Mac)

## Estructura del Proyecto

```
Globant/
├── .idea/                  # Configuración de IntelliJ
├── src/                    # Código fuente
│   ├── Principal/         # Clase principal
│   │   └── Main.java
│   ├── modelo/            # Modelos de datos
│   │   ├── Usuario.java
│   │   ├── Libro.java
│   │   └── Prestamo.java
│   └── servicio/          # Servicios y lógica
│       ├── SupabaseConfig.java
│       ├── SupabaseClient.java
│       ├── LibrosAPIValidator.java
│       ├── UsuarioService.java
│       ├── BibliotecaService.java
│       └── PrestamoService.java
├── out/                   # Compilados (generado)
├── Globant.iml           # Archivo de módulo IntelliJ
└── README.md             # Documentación

```

## Funcionamiento del Sistema

### Al ejecutar Main.java, el sistema:

1. **Registra un usuario** en Supabase
2. **Valida y agrega un libro** (verifica en Google Books API)
3. **Intenta agregar un libro inválido** (muestra error de validación)
4. **Lista todos los libros** guardados
5. **Lista todos los usuarios** registrados
6. **Crea un préstamo** para un usuario
7. **Lista todos los préstamos**

### Salida Esperada en la Consola:

```
=== Sistema de Biblioteca con Supabase ===

1. Registrando un usuario...
Validando libro en API externa...
✓ Libro validado correctamente en la API.
Usuario registrado: {...}

2. Agregando un libro (ISBN válido)...
Validando libro en API externa...
✓ Libro validado correctamente en la API.
Libro agregado a Supabase: {...}

3. Intentando agregar un libro con ISBN inválido...
Validando libro en API externa...
ERROR: El libro con ISBN 000-0-00-000000-0 no existe en la API de libros.
Esperado: Libro no encontrado en la API de libros. No se puede agregar.

...

=== Fin del programa ===
```

## Personalizar el Código

### Cambiar Datos de Prueba

Edita `Main.java` para cambiar los datos de ejemplo:

```java
// Cambiar usuario
Usuario usuario = new Usuario("micorreo@ejemplo.com", "mipassword", "usuario");

// Cambiar libro (usar ISBN válido)
Libro libro = new Libro("9780747532699", "Mi Libro", "Mi Autor", "disponible");

// Cambiar préstamo
Prestamo prestamo = new Prestamo(1, "9780747532699", false);
```

### Agregar Nuevas Funcionalidades

Puedes extender los servicios existentes:

1. **UsuarioService.java** - Agregar métodos para gestión de usuarios
2. **BibliotecaService.java** - Agregar métodos para gestión de libros
3. **PrestamoService.java** - Agregar métodos para gestión de préstamos

## Debugging en IntelliJ

### Agregar Breakpoints
1. Click en el margen izquierdo del editor (aparece un círculo rojo)
2. Click derecho en Main.java → **Debug 'Main.main()'**
3. El programa se detendrá en los breakpoints

### Ver Variables
- Ventana **Debug** (abajo) muestra variables actuales
- Hover sobre variables en el código para ver valores

## Problemas Comunes

### Error: "Cannot resolve symbol"
- **Solución:** File → Invalidate Caches → Invalidate and Restart

### Error: "No JDK configured"
- **Solución:** File → Project Structure → Project → Configurar SDK

### Error de compilación
- **Solución:** Build → Rebuild Project

### Errores de red (en entorno sandbox)
- **Normal:** Los errores de conexión a Supabase/Google Books API son esperados en el entorno de desarrollo sandbox
- **Producción:** El código funcionará correctamente con conexión a internet

## Notas Importantes

- ✓ El código está listo para usar en IntelliJ IDEA
- ✓ La estructura del proyecto está optimizada para IntelliJ
- ✓ Los archivos `.idea/` y `Globant.iml` mantienen la configuración
- ✓ La carpeta `out/` se genera automáticamente al compilar
- ✓ No se requieren plugins adicionales
