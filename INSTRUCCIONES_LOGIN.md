# Instrucciones para el Sistema de Biblioteca

## Creación del Usuario SuperAdmin Inicial

**⚠️ IMPORTANTE - SEGURIDAD**: Después de crear el usuario superadmin inicial, debe cambiar la contraseña inmediatamente en la base de datos. Las credenciales predeterminadas son solo para la configuración inicial.

Para crear el usuario superadmin inicial en la base de datos, ejecute el siguiente comando:

```bash
javac -d out -sourcepath src src/Principal/CrearSuperAdmin.java
java -cp out Principal.CrearSuperAdmin
```

Este script creará el usuario superadmin con las siguientes credenciales **TEMPORALES**:
- **Correo**: beltranmunozh@gmail.com
- **Contraseña**: Hernan2007* (CAMBIAR INMEDIATAMENTE)
- **Rol**: super_admin

**IMPORTANTE**: 
- Asegúrese de tener configuradas las variables de entorno de Supabase antes de ejecutar este script.
- Cambie la contraseña después de la primera ejecución por razones de seguridad.
- No ejecute este script múltiples veces, ya que intentará crear el usuario repetidamente.

## Iniciar el Sistema de Biblioteca

Para iniciar el sistema interactivo:

```bash
javac -d out -sourcepath src src/Principal/Main.java
java -cp out Principal.Main
```

## Sistema de Roles y Permisos

El sistema implementa tres niveles de roles con diferentes permisos:

### SuperAdmin
- **Puede crear**: super_admin, admin, y usuarios normales
- **Acceso completo** a todas las funcionalidades del sistema

### Admin
- **Puede crear**: solo usuarios normales (rol: usuario)
- Acceso a gestión de libros y préstamos

### Usuario
- **No puede crear** usuarios
- Acceso a gestión de libros y préstamos

## Uso del Sistema

1. **Iniciar Sesión**: Ingrese su correo y contraseña
2. **Menú Principal**: Después del login, tendrá acceso a:
   - Gestión de Usuarios (crear, listar, buscar)
   - Gestión de Libros (agregar, listar)
   - Gestión de Préstamos (crear, listar)
3. **Cerrar Sesión**: Regresa al menú de login

## Seguridad

- Las contraseñas se almacenan usando hash SHA-256 con salt
- Cada contraseña tiene un salt único generado aleatoriamente
- Las contraseñas nunca se almacenan en texto plano
