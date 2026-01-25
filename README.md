# Sistema de Biblioteca con Supabase

Este proyecto integra un sistema de biblioteca Java con Supabase como backend.

## Configuración en IntelliJ IDEA

Este proyecto está desarrollado en **IntelliJ IDEA**.

### Cómo ejecutar el proyecto en IntelliJ:

1. **Abrir el proyecto:**
   - File → Open → Seleccionar la carpeta `/Globant`
   - IntelliJ detectará automáticamente el archivo `Globant.iml`

2. **Configurar JDK:**
   - File → Project Structure → Project
   - Asegurarse de tener JDK 8 o superior configurado

3. **Ejecutar el programa:**
   - Abrir `src/Principal/Main.java`
   - Click derecho en la clase `Main`
   - Seleccionar "Run 'Main.main()'"
   - O usar el atajo: Shift + F10

4. **Ver la salida:**
   - Los resultados se mostrarán en la consola de IntelliJ (Run window)

### Estructura del Proyecto en IntelliJ

## Validación de Libros

### API Externa de Libros

El sistema valida que los libros existan en una API externa antes de guardarlos en Supabase. Se utiliza la **Google Books API** para verificar la existencia de libros por ISBN.

**Importante:** Solo se guardarán libros que existan en la API de libros. Si el ISBN no se encuentra en la API, el libro NO será guardado en Supabase.

### Auto-completado de Información

**¡NUEVO!** Ahora solo necesitas proporcionar el **ISBN** del libro. El sistema automáticamente:
- Busca el libro en Google Books API
- Extrae el título y los autores
- Guarda la información completa en Supabase

### Flujo de Validación y Auto-completado

1. El usuario solo proporciona el **ISBN**
2. El sistema consulta la Google Books API con ese ISBN
3. Si el libro existe (totalItems > 0):
   - Se extrae automáticamente el título
   - Se extraen automáticamente los autores
   - Se guarda en Supabase con toda la información
4. Si el libro NO existe, se muestra un error y NO se guarda

### Ejemplo de Uso

**Interfaz de Usuario (Terminal):**
```
=== GESTIÓN DE LIBROS ===
1. Listar Libros
2. Agregar Libro
3. Volver
Seleccione una opción: 2

ISBN: 9788420412146
Buscando libro en Google Books API...
✓ Libro encontrado en la API:
  - Título: Don Quijote de la Mancha. Edición RAE / Don Quixote de la Mancha. RAE
  - Autor(es): Miguel de Cervantes
Libro agregado a Supabase exitosamente.
```

**Código programático:**
```java
BibliotecaService bibliotecaService = new BibliotecaService();

// Solo necesitas el ISBN - el título y autor se obtienen automáticamente de la API
Libro libro = new Libro("9788420412146", "", "", "disponible");
bibliotecaService.agregarLibro(libro); 
// ✓ Se obtiene: Título: "Don Quijote de la Mancha...", Autor: "Miguel de Cervantes"

// ISBN inválido - NO se guardará en Supabase
Libro libroInvalido = new Libro("000-0-00-000000-0", "", "", "disponible");
bibliotecaService.agregarLibro(libroInvalido); // ✗ Error: Libro no encontrado en la API
```

### Ejemplo de API Response

El sistema utiliza la Google Books API con el siguiente formato:
```
https://www.googleapis.com/books/v1/volumes?q=isbn:9788420412146&key=API_KEY
```

De la respuesta JSON, el sistema extrae:
- `volumeInfo.title` → Título del libro
- `volumeInfo.authors` → Lista de autores (se concatenan con comas)


## Estructura del Proyecto

### Modelos (src/modelo/)
- **Usuario.java**: Modelo de usuario con id, correo, contraseña, oath (62 caracteres), y rol
- **Libro.java**: Modelo de libro con ISBN, título, autor y estado
- **Prestamo.java**: Modelo de préstamo con relación a usuario y libro, y campo comprado

### Servicios (src/servicio/)
- **SupabaseConfig.java**: Configuración de URLs y API key de Supabase
- **SupabaseClient.java**: Cliente HTTP para realizar peticiones GET, POST, PATCH, DELETE a Supabase
- **LibrosAPIValidator.java**: Validador que consulta Google Books API para verificar existencia de libros por ISBN
- **UsuarioService.java**: Servicio para gestionar usuarios (registrar, listar, buscar, actualizar rol)
- **BibliotecaService.java**: Servicio para gestionar libros (agregar con validación, listar, buscar, actualizar estado)
- **PrestamoService.java**: Servicio para gestionar préstamos (crear, listar, aprobar, buscar por usuario)

## Tablas en Supabase

### usuarios
- `id`: Serial (autoincremental 1, 2, 3...)
- `correo`: Text (único)
- `contrasena`: Text
- `oath`: Text (exactamente 62 caracteres o NULL)
- `rol`: Enum ('usuario', 'admin', 'super_admin')

### libros
- `isbn`: Text (Primary Key, único)
- `titulo`: Text
- `autor`: Text
- `estado`: Enum ('disponible', 'no disponible', 'temporalmente no disponible')

### prestamos
- `id_prestamo`: BigSerial (Primary Key)
- `usuario_id`: Integer (FK a usuarios.id)
- `libro_isbn`: Text (FK a libros.isbn)
- `fecha_adquisicion`: Timestamp
- `fecha_solicitud`: Timestamp
- `fecha_aprobacion`: Timestamp
- `fecha_expiracion`: Timestamp (NULL si comprado=true)
- `comprado`: Boolean

### adquisiciones
- `id`: BigSerial (Primary Key)
- `usuario_id`: Integer (FK a usuarios.id)
- `libro_isbn`: Text (FK a libros.isbn)
- `fuente`: Text ('compra' o 'prestamo')
- `referencia_prestamo`: BigInt
- `fecha_inicio`: Timestamp
- `fecha_fin`: Timestamp

## Compilar y Ejecutar

### En IntelliJ IDEA (Recomendado)

1. Abrir el proyecto en IntelliJ
2. Navegar a `src/Principal/Main.java`
3. Click derecho → "Run 'Main.main()'"
4. Ver resultados en la consola de IntelliJ

### Desde línea de comandos

#### Compilar
```bash
javac -d out src/modelo/*.java src/servicio/*.java src/Principal/*.java
```

#### Ejecutar
```bash
java -cp out Principal.Main
```

## Uso de los Servicios

### Registrar Usuario
```java
UsuarioService usuarioService = new UsuarioService();
Usuario usuario = new Usuario("correo@ejemplo.com", "password123", "usuario");
usuarioService.registrarUsuario(usuario);
```

### Agregar Libro (con auto-completado desde API)
```java
BibliotecaService bibliotecaService = new BibliotecaService();

// ¡NUEVO! Solo necesitas el ISBN, el título y autor se obtienen automáticamente
Libro libro = new Libro("9788420412146", "", "", "disponible");
bibliotecaService.agregarLibro(libro); 
// Sistema consulta API → Obtiene: "Don Quijote de la Mancha..." y "Miguel de Cervantes"
// Guarda en Supabase con toda la información completa
```

### Crear Préstamo
```java
PrestamoService prestamoService = new PrestamoService();
Prestamo prestamo = new Prestamo(1, "978-3-16-148410-0", false); // false = préstamo, true = compra
prestamoService.crearPrestamo(prestamo);
```

### Aprobar Préstamo
```java
// Para préstamo (con fecha de expiración)
prestamoService.aprobarPrestamo(1L, "2024-01-10T10:00:00Z", "2024-02-10T10:00:00Z");

// Para compra (sin fecha de expiración)
prestamoService.aprobarPrestamo(2L, "2024-01-10T10:00:00Z", null);
```

## API Endpoints

Los servicios se conectan a los siguientes endpoints de Supabase:
- Usuarios: `https://sgmjnvhnxjlmbkzrenfg.supabase.co/rest/v1/usuarios`
- Libros: `https://sgmjnvhnxjlmbkzrenfg.supabase.co/rest/v1/libros`
- Préstamos: `https://sgmjnvhnxjlmbkzrenfg.supabase.co/rest/v1/prestamos`
- Adquisiciones: `https://sgmjnvhnxjlmbkzrenfg.supabase.co/rest/v1/adquisiciones`

## Notas Importantes

1. **Campo oath**: Debe tener exactamente 62 caracteres o ser NULL
2. **Compras**: Cuando `comprado=true`, el campo `fecha_expiracion` debe ser NULL (no expira)
3. **Estados de libro**: 'disponible', 'no disponible', 'temporalmente no disponible'
4. **Roles de usuario**: 'usuario', 'admin', 'super_admin'
5. **Interfaz**: El sistema funciona por terminal/consola, sin interfaz gráfica
