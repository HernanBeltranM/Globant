# Sistema de Biblioteca con Supabase

Este proyecto integra un sistema de biblioteca Java con Supabase como backend.

## Estructura del Proyecto

### Modelos (src/modelo/)
- **Usuario.java**: Modelo de usuario con id, correo, contraseña, oath (62 caracteres), y rol
- **Libro.java**: Modelo de libro con ISBN, título, autor y estado
- **Prestamo.java**: Modelo de préstamo con relación a usuario y libro, y campo comprado

### Servicios (src/servicio/)
- **SupabaseConfig.java**: Configuración de URLs y API key de Supabase
- **SupabaseClient.java**: Cliente HTTP para realizar peticiones GET, POST, PATCH, DELETE a Supabase
- **UsuarioService.java**: Servicio para gestionar usuarios (registrar, listar, buscar, actualizar rol)
- **BibliotecaService.java**: Servicio para gestionar libros (agregar, listar, buscar, actualizar estado)
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

### Compilar
```bash
javac -d out src/modelo/*.java src/servicio/*.java src/Principal/*.java
```

### Ejecutar
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

### Agregar Libro
```java
BibliotecaService bibliotecaService = new BibliotecaService();
Libro libro = new Libro("978-3-16-148410-0", "Título", "Autor", "disponible");
bibliotecaService.agregarLibro(libro);
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
