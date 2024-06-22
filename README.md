# LAB | Java Spring Security (Spring Seguridad)

## Introducción

Acabamos de aprender cómo usar Spring Security para agregar autenticación y autorización a la aplicación, así que practiquemos un poco más.

<br>

## Requisitos

1. Haz un fork de este repositorio.
2. Clona este repositorio.
3. Añade a tu instructor y a los calificadores de la clase como colaboradores de tu repositorio. Si no estás seguro de quiénes son los calificadores de tu clase, pregunta a tu instructor o consulta la presentación del primer día.
4. En el repositorio, crea un proyecto de Java y añade el código para las siguientes tareas.

## Entrega

Una vez que termines la tarea, envía un enlace URL a tu repositorio o tu solicitud de extracción en el campo de abajo.

<br>

## Instrucciones

Para esta actividad, construirás un sistema de gestión de contenido para un blog con múltiples autores.

1. **Crear tablas**: Crea las tablas para la publicación del blog y el autor en tu base de datos.
2. **Usar Spring Security**: Usa Spring Security para crear un rol de administrador.
3. **Crear rutas**: Crea las siguientes rutas:

    - Obtener la publicación y el autor
    - Agregar publicación
    - Agregar autor
    - Actualizar publicación
    - Actualizar autor
    - Eliminar publicación
    - Eliminar autor

4. **Restringir acceso**: Usa Spring Security para asegurarte de que solo los administradores puedan acceder a todas las rutas excepto la ruta GET, que debería estar disponible públicamente.
5. **Crear pruebas**: Crea pruebas para todas las rutas y documenta tu API.

<br>

**Blog Post Table** (Tabla de publicación del blog)

| id  | author_id | title                                     | post                                                   |
| --- | --------- | ----------------------------------------- | ------------------------------------------------------ |
| 1   | 1         | Boost Your Productivity with 10 Easy Tips | Productivity - we all want it but it seems ...         |
| 2   | 1         | How to Focus                              | Do you ever sit down to work and find yourself ...     |
| 3   | 2         | Learn to Speed Read in 30 Days            | Knowledge, not ability, is the great determiner of ... |

<br>

**Author Table** (Tabla de autores)

| id  | name          |
| --- | ------------- |
| 1   | Aiko Tanaka   |
| 2   | Jonas Schmidt |
| 3   | Cas Van Dijk  |

<br>