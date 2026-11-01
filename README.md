# AprendeApp

Aplicación Android en Kotlin para gestionar recursos de aprendizaje (libros, videos, artículos y tutoriales de Ingeniería de Sistemas). Desarrollada para la materia Desarrollo de Software para Móviles (Universidad Don Bosco).

**Autor:** Gladis del Carmen Rivas Miranda :rm191684

## Video de defensa
[Ver video demostrativo](PEGA_AQUI_EL_ENLACE_DEL_VIDEO)
## Funcionalidades
- Registro con validación segura de contraseña (mín. 12 caracteres, mayúscula, minúscula, número y carácter especial).
- Login y logout con sesión persistente. Contraseña cifrada con SHA-256.
- Dos roles: **Estudiante** (ver, buscar, favoritos, calificar) y **Docente** (CRUD completo de recursos).
- Lista de recursos en tarjetas con imagen, título, tipo, descripción y calificación.
- Búsqueda por ID, título, descripción o tipo; filtro por tipo; ordenamiento.
- Favoritos guardados localmente por usuario.
- Calificación de 1 a 5 estrellas con promedio compartido (PUT a la API).
- CRUD con POST, PUT y DELETE, formularios validados y confirmación antes de eliminar.
- Feedback visual: toasts, loaders y diálogos.

## Arquitectura (MVC)
- `model/`: Recurso, Usuario
- `view/`: Activities y Adapters
- `controller/`: AuthController, RecursoController, AdminController
- `network/`: ApiService y RetrofitClient (Retrofit + Gson)
- `util/`: sesión, favoritos, validador de contraseña, constantes

## Tecnologías
Kotlin, Android Studio, Retrofit 2, Gson, Picasso, ViewBinding, Material Design, MockAPI.io, SharedPreferences.

## API
MockAPI.io con los recursos `recursos` y `usuario`.

## Cómo ejecutar
1. Clonar el repositorio y abrirlo en Android Studio.
2. Ajustar `BASE_URL` en `util/Constants.kt` con la URL de MockAPI.
3. Ejecutar en un emulador o dispositivo (Android 8.0+).

## Uso de IA
Se usó IA como apoyo al aprendizaje; el autor comprende y puede explicar el código.