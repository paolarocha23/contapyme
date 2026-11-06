# ContaPyme REST API — Documentación de integración

> **Versión del API:** `2.0.0`
> **Última actualización:** 2026-10-29
> **Audiencia:** equipos que consumirán esta API desde otro sistema (web, móvil, escritorio, ERP, etc.)

Esta guía describe **todas** las rutas HTTP disponibles, el formato exacto de las peticiones y respuestas, el flujo de autenticación en dos pasos (2FA), los códigos de error y los modelos de datos.

---

## Índice

1. [Información general](#1-información-general)
2. [Convenciones de la API](#2-convenciones-de-la-api)
3. [Autenticación](#3-autenticación)
4. [Flujo completo de login con 2FA](#4-flujo-completo-de-login-con-2fa)
5. [Rate limiting (límite de peticiones)](#5-rate-limiting-límite-de-peticiones)
6. [Errores transversales](#6-errores-transversales)
7. [Endpoints de autenticación](#7-endpoints-de-autenticación)
8. [Endpoints de roles](#8-endpoints-de-roles)
9. [Endpoints de usuarios](#9-endpoints-de-usuarios)
10. [Endpoints de bitácora](#10-endpoints-de-bitácora)
11. [Modelos de datos](#11-modelos-de-datos)
12. [Catálogo de permisos y roles](#12-catálogo-de-permisos-y-roles)
13. [Catálogo de acciones de bitácora](#13-catálogo-de-acciones-de-bitácora)
14. [Ejemplos de integración](#14-ejemplos-de-integración)
15. [Consideraciones importantes (gotchas)](#15-consideraciones-importantes-gotchas)
16. [Checklist de integración](#16-checklist-de-integración)

---

## 1. Información general

### 1.1 Base URL

| Entorno | Base URL |
|---|---|
| Local (backend directo) | `http://localhost:5000/api` |
| Local (frontend Vite con proxy) | `http://localhost:5173/api` → redirige a `http://localhost:5000` |
| Producción | `https://TU_DOMINIO/api` |

El puerto del backend se define con la variable de entorno `PORT` (por defecto `5000`).

### 1.2 Health check / descubrimiento

```http
GET /api
```

No requiere autenticación. Devuelve metadatos y el listado de rutas:

```json
{
  "name": "ContaPyme REST API",
  "version": "2.0.0",
  "status": "ONLINE",
  "author": "ContaPyme Security Team",
  "endpoints": {
    "auth": {
      "login": "POST /api/auth/login",
      "verify2FA": "POST /api/auth/verify-2fa",
      "resend2FA": "POST /api/auth/resend-2fa",
      "logout": "POST /api/auth/logout",
      "forgotPassword": "POST /api/auth/forgot-password",
      "resetPassword": "POST /api/auth/reset-password",
      "changePassword": "POST /api/auth/change-password (Requires Bearer token)",
      "me": "GET /api/auth/me (Requires Bearer token)"
    },
    "roles": {
      "list": "GET /api/roles (?nombre=...)",
      "get": "GET /api/roles/:id",
      "create": "POST /api/roles",
      "update": "PUT /api/roles/:id",
      "toggleStatus": "PATCH /api/roles/:id/status",
      "assignRole": "PUT /api/roles/assign-user/:id_usuario"
    },
    "users": {
      "list": "GET /api/users (?search=...&id_rol=...&estatus=...)",
      "get": "GET /api/users/:id",
      "create": "POST /api/users",
      "update": "PUT /api/users/:id",
      "toggleStatus": "PATCH /api/users/:id/status",
      "adminResetPassword": "POST /api/users/:id/reset-password-admin"
    },
    "bitacora": {
      "list": "GET /api/bitacora (?accion=...&nombre_usuario=...&limit=50&offset=0)"
    }
  }
}
```

> **Nota:** `GET /api` sirve también como *smoke test*: si responde, el proceso Node está vivo. No garantiza que la base de datos esté conectada.

### 1.3 CORS

El servidor acepta peticiones desde cualquier origen:

```js
origin: true,
credentials: true,
methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'],
allowedHeaders: ['Content-Type', 'Authorization', 'X-Requested-With']
```

- Si usas `fetch` con `credentials: 'include'`, el navegador enviará cookies, aunque la API **no usa cookies**: la sesión va por `Authorization: Bearer`.
- Desde un backend (Node, Python, PHP, C#) CORS no aplica.

### 1.4 Formato y límites

| Aspecto | Valor |
|---|---|
| Content-Type de entrada | `application/json` (también acepta `application/x-www-form-urlencoded`) |
| Tamaño máximo del body | `2mb` |
| Content-Type de salida | `application/json; charset=utf-8` |
| Codificación de texto | UTF-8 |
| Zona horaria de fechas | Las fechas se devuelven en ISO 8601; internamente la BD usa `TIMESTAMPTZ` para expiraciones y `TIMESTAMP` para `fecha_creacion` / bitácora |

### 1.5 Requisitos de arranque del backend

El servidor ejecuta `initializeDatabase()` antes de escuchar peticiones. Si la base de datos no está disponible, el proceso **termina con código 1** y no atiende HTTP. Para verificar la conexión:

```bash
npm run db:check
```

Variables relevantes (`.env`):

```env
PORT=5000
NODE_ENV=development
DATABASE_URL=postgresql://postgres.PROJECT_REF:PASSWORD@HOST:5432/postgres
DB_SSL=true
DB_POOL_MAX=15
JWT_SECRET=...
JWT_EXPIRES_IN=8h
JWT_2FA_SECRET=...
SMTP_HOST=
SMTP_PORT=587
SMTP_SECURE=false
SMTP_USER=
SMTP_PASS=
EMAIL_FROM="ContaPyme Seguridad" <no-reply@contapyme.com>
INACTIVITY_TIMEOUT_MINUTES=15
```

> Si `SMTP_HOST` / `SMTP_USER` no están configurados, el código 2FA **no se envía por correo**: se imprime en la consola del servidor (`🔑 CÓDIGO DE VERIFICACIÓN 2FA: [ 123456 ]`). Útil para pruebas, inaceptable en producción.

---

## 2. Convenciones de la API

### 2.1 Envelope de respuesta

Todas las respuestas son JSON. El campo `success` es el discriminador principal:

**Éxito:**

```json
{
  "success": true,
  "message": "Texto opcional legible",
  "data": { }
}
```

**Error:**

```json
{
  "success": false,
  "message": "Descripción legible del error",
  "code": "CODIGO_OPCIONAL"
}
```

Campos adicionales según el endpoint:

| Campo | Dónde aparece | Significado |
|---|---|---|
| `data` | Listados y detalle | Recurso o arreglo de recursos |
| `total` | `GET /api/bitacora` | Total de registros en la tabla (ver gotcha 15.6) |
| `token` | `verify-2fa` | JWT de sesión definitiva |
| `user` | `verify-2fa`, `auth/me` | Datos del usuario autenticado |
| `tempToken` | `login` | JWT temporal de 2FA (10 min) |
| `emailMasked` | `login` | Correo enmascarado (ej. `j***n@correo.com`) |
| `requires2FA` | `login` | Siempre `true` |
| `code` | Errores de auth | `USER_INACTIVE`, `ROLE_INACTIVE`, `TOKEN_EXPIRED` |
| `estatus` | Toggles | Nuevo estatus aplicado (`1` o `0`) |
| `usuariosAfectados` | Toggle de rol | Cantidad de usuarios con ese rol |
| `devToken` | `forgot-password` | Solo si `NODE_ENV !== 'production'` |

### 2.2 Códigos HTTP usados

| Código | Uso |
|---|---|
| `200 OK` | Operación exitosa (incluye login/verify/resend) |
| `201 Created` | Recurso creado (`POST /roles`, `POST /users`) |
| `400 Bad Request` | Datos faltantes, inválidos, código 2FA incorrecto/usado/expirado, reglas de negocio |
| `401 Unauthorized` | Credenciales incorrectas, token ausente/inválido/expirado |
| `403 Forbidden` | Usuario o rol inactivo, sin privilegios de Administrador |
| `404 Not Found` | Recurso inexistente o ruta inexistente |
| `429 Too Many Requests` | Rate limit excedido |
| `500 Internal Server Error` | Error no controlado |

### 2.3 Tipos y formatos

| Tipo lógico | Representación JSON | Ejemplo |
|---|---|---|
| IDs | número entero | `12` |
| `estatus` | número `1` / `0` | `1` = activo, `0` = inactivo |
| `permisos` | arreglo de strings | `["usuarios", "roles"]` |
| Fechas | string ISO 8601 | `"2026-10-29T14:32:10.000Z"` |
| Booleanos | `true` / `false` | `true` |
| Texto nulo | `null` | `null` |

> **Importante:** `estatus` **no** es booleano. Se compara con `1` (activo) y `0` (inactivo).

### 2.4 Nombres de campos

Los nombres son **snake_case** y están en español: `id_usuario`, `nombre_usuario`, `contrasena`, `nueva_contrasena`, `apellido_paterno`, `id_rol`, `fecha_creacion`, `correo_o_usuario`, `contrasena_actual`.

---

## 3. Autenticación

### 3.1 Esquema

- **Autenticación en dos pasos obligatoria.** El `POST /auth/login` **no devuelve** el token de sesión; devuelve un `tempToken` para el segundo paso.
- El token definitivo es un **JWT** que se envía en cada petición protegida:

```http
Authorization: Bearer <token>
```

- **Duración del token de sesión:** `JWT_EXPIRES_IN` (por defecto `8h`).
- **Duración del `tempToken`:** 10 minutos.
- **No hay refresh token.** Al expirar, el usuario debe volver a autenticarse (login + 2FA).
- El JWT **no se almacena en el servidor**: no existe una lista de revocación. `POST /auth/logout` solo registra el evento en bitácora; el token sigue siendo válido hasta su expiración natural. Ver [gotcha 15.1](#151-logout-no-revoca-el-jwt).

### 3.2 Payload del JWT de sesión (informativo)

```json
{
  "id_usuario": 3,
  "nombre_usuario": "jperez",
  "id_rol": 2,
  "rol_nombre": "Encargado",
  "iat": 1761745930,
  "exp": 1761774730
}
```

El cliente **no debe** validar ni confiar en el contenido del JWT para autorización; la API revalida usuario y rol en la base de datos en cada petición protegida.

### 3.3 Payload del `tempToken` (informativo)

```json
{
  "id_usuario": 3,
  "nombre_usuario": "jperez",
  "step": "2fa_pending",
  "iat": 1761745930,
  "exp": 1761746530
}
```

### 3.4 Validación en cada petición protegida

El middleware `verifyToken` hace lo siguiente, en orden:

1. Exige `Authorization: Bearer <token>`.
2. Verifica la firma y expiración del JWT.
3. Busca el usuario y su rol en la base de datos.
4. Rechaza si el usuario no existe (`401`).
5. Rechaza si el usuario está inactivo (`403` + `code: "USER_INACTIVE"`).
6. Rechaza si el rol está inactivo (`403` + `code: "ROLE_INACTIVE"`).
7. Adjunta `req.user` con los datos del usuario y sus permisos.

Respuestas posibles:

```json
// Sin header
{ "success": false, "message": "No se proporcionó token de autorización" }
```

```json
// Token expirado
{ "success": false, "code": "TOKEN_EXPIRED", "message": "Tu sesión ha expirado. Por favor, inicia sesión nuevamente." }
```

```json
// Token inválido/manipulado
{ "success": false, "message": "Token inválido o manipulado" }
```

```json
// Usuario desactivado
{ "success": false, "code": "USER_INACTIVE", "message": "Tu cuenta ha sido desactivada por el Administrador. No puedes realizar operaciones." }
```

```json
// Rol desactivado
{ "success": false, "code": "ROLE_INACTIVE", "message": "El rol asignado a tu cuenta está inactivo. Tu acceso ha sido revocado." }
```

### 3.5 Autorización por rol

Las rutas de `roles`, `users` y `bitacora` requieren **además** que `rol_nombre === "Administrador"` (comparación exacta, sensible a mayúsculas/acentos):

```json
{ "success": false, "message": "Acceso denegado: se requieren privilegios de Administrador" }
```

> Un usuario con rol `Administrador` **inactivo** no pasa: el middleware de rol se evalúa antes y devuelve `403 ROLE_INACTIVE`.

### 3.6 Matriz de acceso

| Endpoint | Público | Token | Administrador |
|---|:---:|:---:|:---:|
| `GET /api` | ✅ | — | — |
| `POST /api/auth/login` | ✅ | — | — |
| `POST /api/auth/verify-2fa` | ✅ | — | — |
| `POST /api/auth/resend-2fa` | ✅ | — | — |
| `POST /api/auth/logout` | ✅ (token opcional) | — | — |
| `POST /api/auth/forgot-password` | ✅ | — | — |
| `POST /api/auth/reset-password` | ✅ | — | — |
| `GET /api/auth/me` | ❌ | ✅ | — |
| `POST /api/auth/change-password` | ❌ | ✅ | — |
| `GET /api/roles` | ❌ | ✅ | ✅ |
| `GET /api/roles/:id` | ❌ | ✅ | ✅ |
| `POST /api/roles` | ❌ | ✅ | ✅ |
| `PUT /api/roles/:id` | ❌ | ✅ | ✅ |
| `PATCH /api/roles/:id/status` | ❌ | ✅ | ✅ |
| `PUT /api/roles/assign-user/:id_usuario` | ❌ | ✅ | ✅ |
| `GET /api/users` | ❌ | ✅ | ✅ |
| `GET /api/users/:id` | ❌ | ✅ | ✅ |
| `POST /api/users` | ❌ | ✅ | ✅ |
| `PUT /api/users/:id` | ❌ | ✅ | ✅ |
| `PATCH /api/users/:id/status` | ❌ | ✅ | ✅ |
| `POST /api/users/:id/reset-password-admin` | ❌ | ✅ | ✅ |
| `GET /api/bitacora` | ❌ | ✅ | ✅ |

---

## 4. Flujo completo de login con 2FA

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │ 1) POST /api/auth/login  { nombre_usuario, contrasena }
       ▼
┌──────────────────────────────────────────────┐
│ API: valida credenciales, usuario y rol      │
│ activos; genera código de 6 dígitos;         │
│ lo guarda (10 min) y lo envía por correo     │
└──────┬───────────────────────────────────────┘
       │ 200 { success, requires2FA, tempToken, emailMasked }
       ▼
┌─────────────┐
│   Cliente   │ 2) El usuario recibe el código en su correo
└──────┬──────┘
       │ 3) POST /api/auth/verify-2fa  { tempToken, codigo }
       ▼
┌──────────────────────────────────────────────┐
│ API: busca el código exacto del usuario,     │
│ valida usado = false y expiracion > NOW();   │
│ lo marca como usado y emite JWT de sesión    │
└──────┬───────────────────────────────────────┘
       │ 200 { success, message, token, user }
       ▼
┌─────────────┐
│   Cliente   │ 4) Guarda token y lo envía en cada petición:
└─────────────┘    Authorization: Bearer <token>
```

### 4.1 Reglas del código 2FA

| Regla | Valor |
|---|---|
| Longitud | 6 dígitos numéricos (`100000`–`999999`) |
| Vigencia | 10 minutos desde su creación (calculada en PostgreSQL) |
| Un solo uso | Se marca `usado = true` al verificar correctamente |
| Códigos previos | Se invalidan automáticamente al generar uno nuevo |
| Reintentos | Limitados por IP (10 intentos / 5 min) |
| Formato de envío | Correo electrónico al `correo` registrado del usuario |
| `devCode` en la respuesta | **Eliminado**; ya no se devuelve en producción |

### 4.2 Errores específicos del 2FA

| Situación | HTTP | `message` |
|---|---|---|
| Código no coincide con ningún registro | `400` | `Código de verificación incorrecto. Verifica el número recibido en tu correo.` |
| Código ya utilizado | `400` | `Este código ya fue utilizado. Solicita un nuevo código.` |
| Código vencido (> 10 min) | `400` | `El código de verificación ha expirado. Solicita un nuevo código.` |
| `tempToken` vencido/inválido | `401` | `La sesión de verificación ha expirado. Por favor, ingresa tus credenciales nuevamente.` |
| Faltan datos | `400` | `Faltan datos requeridos (token temporal y código 2FA).` |

> **Doble envío:** si tu cliente envía `verify-2fa` dos veces con el mismo código, la segunda petición recibirá `400 "Este código ya fue utilizado..."`. Implementa un *lock* en el botón/auto-submit.

---

## 5. Rate limiting (límite de peticiones)

Los límites son **por IP** (`express-rate-limit`). Al excederse devuelven `429` con el cuerpo indicado.

| Limitador | Endpoints | Ventana | Máximo | Respuesta |
|---|---|---|---|---|
| `authLimiter` | `POST /auth/login`, `POST /auth/resend-2fa`, `POST /auth/forgot-password`, `POST /auth/reset-password` | 10 min | 30 | `{ "success": false, "message": "Demasiadas solicitudes desde esta dirección IP. Intenta nuevamente en 10 minutos." }` |
| `twoFactorLimiter` | `POST /auth/verify-2fa` | 5 min | 10 | `{ "success": false, "message": "Demasiados intentos de verificación de código. Espera 5 minutos." }` |

Headers estándar de rate limit (`RateLimit-*`) están habilitados (`standardHeaders: true`). Los headers `X-RateLimit-*` legacy están deshabilitados.

> **Detrás de un proxy:** si el backend está detrás de Nginx/Cloudflare/Render, configura `trust proxy` en Express para que la IP real se detecte correctamente. De lo contrario todos los clientes comparten el límite.

---

## 6. Errores transversales

### 6.1 Ruta inexistente

Cualquier ruta bajo `/api` que no exista:

```http
HTTP/1.1 404 Not Found
```

```json
{
  "success": false,
  "message": "Ruta API no encontrada: [GET] /api/no-existe"
}
```

### 6.2 Error no controlado

```http
HTTP/1.1 500 Internal Server Error
```

```json
{
  "success": false,
  "message": "Ocurrió un error inesperado en el servidor.",
  "error": "detalle solo si NODE_ENV=development"
}
```

> El campo `error` **solo** aparece cuando `NODE_ENV === 'development'`. No lo uses en lógica de negocio.

### 6.3 Body JSON malformado o demasiado grande

- JSON inválido: `express.json()` lanza un `SyntaxError` que cae en el manejador global y se responde como **`500`** con `{ "success": false, "message": "Ocurrió un error inesperado en el servidor." }` (no como `400`).
- Body mayor a `2mb`: igual, manejador global → **`500`**.

Valida siempre el JSON en el cliente y no envíes payloads grandes. No interpretes un `500` como "servidor caído": puede ser un body mal formado.

### 6.4 IDs no numéricos

Los endpoints con `:id` pasan el valor directamente a PostgreSQL. Un ID como `abc` produce un error de casteo y responde `500`. **Envía siempre IDs numéricos.**

---

## 7. Endpoints de autenticación

Prefijo: `/api/auth`

### 7.1 `POST /api/auth/login`

**Descripción:** Paso 1 del login. Valida credenciales y envía un código 2FA al correo del usuario.

| Propiedad | Valor |
|---|---|
| Auth | Pública |
| Rate limit | `authLimiter` (30 / 10 min) |
| Content-Type | `application/json` |

**Body:**

```json
{
  "nombre_usuario": "jperez",
  "contrasena": "MiClave123"
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `nombre_usuario` | string | ✅ | Acepta **nombre de usuario o correo electrónico** (la consulta es `nombre_usuario = $1 OR correo = $1`). Se aplica `trim()`. |
| `contrasena` | string | ✅ | Texto plano; se compara con bcrypt. |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "requires2FA": true,
  "message": "Código de verificación en 2 pasos enviado a tu correo electrónico.",
  "tempToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "emailMasked": "j***z@correo.com"
}
```

| Campo | Tipo | Descripción |
|---|---|---|
| `tempToken` | string | JWT temporal para `verify-2fa`. Válido 10 min. **No** sirve para rutas protegidas. |
| `emailMasked` | string | Correo enmascarado para mostrar al usuario |
| `requires2FA` | boolean | Siempre `true` |

**Errores:**

| HTTP | `code` | `message` |
|---|---|---|
| `400` | — | `Por favor, introduce tu nombre de usuario y contraseña.` |
| `401` | — | `Usuario o contraseña incorrectos.` (usuario inexistente **o** contraseña incorrecta) |
| `403` | `USER_INACTIVE` | `Tu cuenta está inactiva. Comunícate con el Administrador del sistema.` |
| `403` | `ROLE_INACTIVE` | `El rol (NOMBRE) asociado a tu cuenta está inactivo. Tu acceso ha sido restringido.` |
| `429` | — | Rate limit |
| `500` | — | `Error interno del servidor al procesar el inicio de sesión.` |

**Ejemplo cURL:**

```bash
curl -X POST http://localhost:5000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"nombre_usuario":"jperez","contrasena":"MiClave123"}'
```

> **Importante:** cada login exitoso **invalida los códigos 2FA anteriores** del usuario y genera uno nuevo. Si el usuario pide login dos veces, solo el último código sirve.

---

### 7.2 `POST /api/auth/verify-2fa`

**Descripción:** Paso 2 del login. Valida el código recibido por correo y emite el JWT de sesión.

| Propiedad | Valor |
|---|---|
| Auth | Pública (requiere `tempToken`) |
| Rate limit | `twoFactorLimiter` (10 / 5 min) |

**Body:**

```json
{
  "tempToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "codigo": "482913"
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `tempToken` | string | ✅ | El devuelto por `login` o `resend-2fa` |
| `codigo` | string | ✅ | 6 dígitos. Se aplica `trim()`. Enviarlo como string evita perder ceros. |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "¡Bienvenido al sistema ContaPyme!",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id_usuario": 3,
    "nombre": "Juan",
    "apellido_paterno": "Pérez",
    "apellido_materno": "López",
    "nombre_completo": "Juan Pérez López",
    "nombre_usuario": "jperez",
    "correo": "jperez@correo.com",
    "id_rol": 2,
    "rol_nombre": "Encargado",
    "estatus": 1,
    "fecha_creacion": "2026-01-15T10:20:30.000Z",
    "permisos": ["clientes", "proveedores", "productos", "inventario", "ventas", "compras", "ingresos", "gastos", "caja", "cxc", "cxp"]
  }
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `Faltan datos requeridos (token temporal y código 2FA).` |
| `400` | `Código de verificación incorrecto. Verifica el número recibido en tu correo.` |
| `400` | `Este código ya fue utilizado. Solicita un nuevo código.` |
| `400` | `El código de verificación ha expirado. Solicita un nuevo código.` |
| `401` | `La sesión de verificación ha expirado. Por favor, ingresa tus credenciales nuevamente.` |
| `403` | `Tu usuario ha sido desactivado.` |
| `403` | `El rol de tu usuario ha sido desactivado.` |
| `404` | `Usuario no encontrado.` |
| `429` | Rate limit |
| `500` | `Error al verificar el código de 2 pasos.` |

**Ejemplo:**

```bash
curl -X POST http://localhost:5000/api/auth/verify-2fa \
  -H "Content-Type: application/json" \
  -d '{"tempToken":"eyJ...","codigo":"482913"}'
```

---

### 7.3 `POST /api/auth/resend-2fa`

**Descripción:** Invalida el código vigente y envía uno nuevo al correo. Requiere un `tempToken` aún válido (10 min desde el login original).

| Propiedad | Valor |
|---|---|
| Auth | Pública (requiere `tempToken`) |
| Rate limit | `authLimiter` (30 / 10 min) |

**Body:**

```json
{
  "tempToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Se ha reenviado un nuevo código a tu correo electrónico."
}
```

> El `tempToken` **no se renueva** al reenviar. Si el `tempToken` original expira (10 min), el usuario debe volver a hacer `login`.

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `Token temporal no proporcionado.` |
| `401` | `La sesión temporal ha expirado.` |
| `404` | `Usuario no encontrado.` |
| `429` | Rate limit |
| `500` | `Error interno al reenviar el código.` |

---

### 7.4 `POST /api/auth/logout`

**Descripción:** Registra el cierre de sesión en bitácora. **No invalida el JWT.**

| Propiedad | Valor |
|---|---|
| Auth | Opcional (si envías `Authorization`, se registra el usuario en la bitácora) |
| Rate limit | Ninguno |

**Body:**

```json
{
  "motivo": "manual"
}
```

| Campo | Tipo | Obligatorio | Valores |
|---|---|:---:|---|
| `motivo` | string | ❌ | `"manual"` (por defecto) o `"inactividad"` |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Sesión cerrada con éxito."
}
```

Con `motivo: "inactividad"`:

```json
{
  "success": true,
  "message": "Sesión cerrada automáticamente por inactividad."
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `500` | `Error al procesar el cierre de sesión.` |

> El servidor acepta tokens expirados en este endpoint (`ignoreExpiration: true`) para poder registrar el evento. Si el token es inválido, simplemente se registra el logout sin `id_usuario`.

---

### 7.5 `POST /api/auth/forgot-password`

**Descripción:** Genera un token de recuperación (1 hora) y envía un correo con enlace y token.

| Propiedad | Valor |
|---|---|
| Auth | Pública |
| Rate limit | `authLimiter` (30 / 10 min) |

**Body:**

```json
{
  "correo_o_usuario": "jperez@correo.com"
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `correo_o_usuario` | string | ✅ | Acepta correo **o** nombre de usuario. Se aplica `trim()`. |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Hemos enviado un enlace y código de recuperación a tu correo electrónico.",
  "devToken": "a3f5...64_hex_chars"
}
```

| Campo | Notas |
|---|---|
| `devToken` | **Solo** se incluye si `NODE_ENV !== 'production'`. No debe usarse en producción. |

**Respuesta cuando el usuario no existe (misma forma, por seguridad) — `200`:**

```json
{
  "success": true,
  "message": "Si el correo o usuario está registrado y activo, recibirás instrucciones para restablecer tu contraseña."
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `Por favor, introduce tu correo electrónico o nombre de usuario.` |
| `403` | `Esta cuenta está inactiva. Contacta al Administrador.` |
| `429` | Rate limit |
| `500` | `Error al procesar la recuperación de contraseña.` |

**Enlace generado en el correo:**

```
{Origin header}/?reset_token={token}
```

Si tu sistema no envía el header `Origin`, se usa `http://localhost:5173`. **Envía `Origin`** si quieres que el enlace apunte a tu aplicación.

**Ejemplo:**

```bash
curl -X POST http://localhost:5000/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -H "Origin: https://miapp.com" \
  -d '{"correo_o_usuario":"jperez@correo.com"}'
```

---

### 7.6 `POST /api/auth/reset-password`

**Descripción:** Restablece la contraseña usando el token recibido por correo.

| Propiedad | Valor |
|---|---|
| Auth | Pública |
| Rate limit | `authLimiter` (30 / 10 min) |

**Body:**

```json
{
  "token": "a3f5...64_hex_chars",
  "nueva_contrasena": "NuevaClave456"
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `token` | string | ✅ | Token de 64 caracteres hex. Se aplica `trim()`. |
| `nueva_contrasena` | string | ✅ | Mínimo 6 caracteres |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Tu contraseña ha sido restablecida exitosamente. Ahora puedes iniciar sesión."
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `Faltan datos requeridos (token y nueva contraseña).` |
| `400` | `La nueva contraseña debe tener al menos 6 caracteres.` |
| `400` | `El enlace o token de recuperación no es válido o ya ha expirado.` |
| `429` | Rate limit |
| `500` | `Error al restablecer la contraseña.` |

**Reglas del token:**

- Un solo uso (se marca `usado = true`).
- Vigencia: 1 hora.
- Al solicitar uno nuevo, los anteriores se invalidan.
- Al restablecer por administrador, también se invalidan los tokens pendientes.

---

### 7.7 `POST /api/auth/change-password`

**Descripción:** Cambia la contraseña del usuario autenticado, exigiendo la contraseña actual.

| Propiedad | Valor |
|---|---|
| Auth | **Bearer token** |
| Rate limit | Ninguno |

**Headers:**

```http
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:**

```json
{
  "contrasena_actual": "MiClave123",
  "nueva_contrasena": "NuevaClave456"
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `contrasena_actual` | string | ✅ | Debe coincidir con la actual |
| `nueva_contrasena` | string | ✅ | Mínimo 6 caracteres |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Tu contraseña se ha actualizado correctamente."
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `Debes proporcionar la contraseña actual y la nueva contraseña.` |
| `400` | `La nueva contraseña debe contener al menos 6 caracteres.` |
| `400` | `La contraseña actual no es correcta.` |
| `401` | Token ausente/inválido/expirado |
| `403` | Usuario o rol inactivo |
| `404` | `Usuario no encontrado.` |
| `500` | `Error interno al cambiar la contraseña.` |

> **Nota:** el cambio de contraseña **no invalida** el JWT actual ni emite uno nuevo. El token sigue siendo válido hasta expirar.

---

### 7.8 `GET /api/auth/me`

**Descripción:** Devuelve los datos del usuario autenticado, revalidados contra la base de datos.

| Propiedad | Valor |
|---|---|
| Auth | **Bearer token** |
| Rate limit | Ninguno |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "user": {
    "id_usuario": 3,
    "nombre": "Juan",
    "apellido_paterno": "Pérez",
    "apellido_materno": "López",
    "nombre_completo": "Juan Pérez López",
    "nombre_usuario": "jperez",
    "correo": "jperez@correo.com",
    "id_rol": 2,
    "rol_nombre": "Encargado",
    "permisos": ["clientes", "ventas", "caja"]
  }
}
```

| Campo | Tipo | Descripción |
|---|---|---|
| `id_usuario` | number | ID del usuario |
| `nombre` | string | Nombre(s) |
| `apellido_paterno` | string | Apellido paterno |
| `apellido_materno` | string \| null | Apellido materno |
| `nombre_completo` | string | Concatenación con `trim()` |
| `nombre_usuario` | string | Nombre de usuario |
| `correo` | string | Correo |
| `id_rol` | number | ID del rol |
| `rol_nombre` | string | Nombre del rol |
| `permisos` | string[] | Permisos del rol (siempre arreglo, `[]` si inválido) |

**Errores:** `401`, `403`, `500` (ver sección 3.4).

> Este endpoint es ideal para "rehidratar" la sesión al abrir tu aplicación, en lugar de confiar en datos guardados localmente.

---

## 8. Endpoints de roles

Prefijo: `/api/roles`

> **Todas** las rutas requieren `Authorization: Bearer <token>` **y** rol `Administrador`.

### 8.1 `GET /api/roles`

**Descripción:** Lista roles con conteo de usuarios.

| Propiedad | Valor |
|---|---|
| Auth | Bearer + Administrador |
| Query params | `nombre` (opcional, búsqueda parcial `ILIKE`) |

**Ejemplo:**

```http
GET /api/roles?nombre=admin
```

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "data": [
    {
      "id_rol": 1,
      "nombre": "Administrador",
      "descripcion": "Acceso total al sistema",
      "estatus": 1,
      "permisos": ["usuarios", "roles", "clientes", "..."],
      "fecha_creacion": "2026-01-10T08:00:00.000Z",
      "total_usuarios": 2,
      "usuarios_activos": 2,
      "es_predefinido": true
    }
  ]
}
```

| Campo | Tipo | Descripción |
|---|---|---|
| `id_rol` | number | ID del rol |
| `nombre` | string | Nombre único |
| `descripcion` | string \| null | Descripción |
| `estatus` | number | `1` activo / `0` inactivo |
| `permisos` | string[] | Catálogo de permisos |
| `fecha_creacion` | string | ISO 8601 |
| `total_usuarios` | number | Usuarios asignados (activos e inactivos) |
| `usuarios_activos` | number | Solo usuarios con `estatus = 1` |
| `es_predefinido` | boolean | `true` para IDs `1`, `2`, `3` |

**Orden:** `id_rol ASC`.

**Errores:** `401`, `403`, `500` (`Error al consultar los roles.`).

---

### 8.2 `GET /api/roles/:id`

**Descripción:** Detalle de un rol.

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "data": {
    "id_rol": 2,
    "nombre": "Encargado",
    "descripcion": "Operaciones del día a día",
    "permisos": ["clientes", "ventas"],
    "estatus": 1,
    "fecha_creacion": "2026-01-10T08:00:00.000Z"
  }
}
```

> El `SELECT` es `SELECT *`, por lo que el objeto refleja todas las columnas existentes en la tabla `roles`. Los campos listados son los esperados por el esquema.

**Errores:**

| HTTP | `message` |
|---|---|
| `404` | `Rol no encontrado.` |
| `401` / `403` | Auth/rol |
| `500` | `Error al obtener el rol.` |

---

### 8.3 `POST /api/roles`

**Descripción:** Crea un rol.

| Propiedad | Valor |
|---|---|
| Auth | Bearer + Administrador |
| Éxito | `201 Created` |

**Body:**

```json
{
  "nombre": "Auditor",
  "descripcion": "Solo lectura de reportes",
  "permisos": ["reportes", "bitacora"]
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `nombre` | string | ✅ | Único, no vacío, `trim()` |
| `descripcion` | string | ❌ | `null` si se omite/vacío |
| `permisos` | string[] | ❌ | Si no es arreglo, se guarda `[]` |

**Respuesta exitosa — `201`:**

```json
{
  "success": true,
  "message": "Rol creado exitosamente.",
  "data": {
    "id_rol": 4,
    "nombre": "Auditor",
    "descripcion": "Solo lectura de reportes",
    "permisos": ["reportes", "bitacora"],
    "estatus": 1
  }
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `El nombre del rol es obligatorio.` |
| `400` | `Ya existe un rol con el nombre "X".` |
| `401` / `403` | Auth/rol |
| `500` | `Error al registrar el rol.` |

> El rol nuevo siempre se crea con `estatus = 1`.

---

### 8.4 `PUT /api/roles/:id`

**Descripción:** Actualiza nombre, descripción y permisos de un rol. **No** cambia el ID ni el estatus.

**Body:**

```json
{
  "nombre": "Auditor Interno",
  "descripcion": "Solo lectura",
  "permisos": ["reportes"]
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `nombre` | string | ✅ | Único entre roles, no vacío |
| `descripcion` | string | ❌ | `null` si vacío |
| `permisos` | string[] | ❌ | Si no es arreglo, se guarda `[]` |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Rol actualizado exitosamente."
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `El nombre del rol no puede estar vacío.` |
| `400` | `El nombre "X" ya está en uso por otro rol.` |
| `404` | `Rol no encontrado.` |
| `401` / `403` | Auth/rol |
| `500` | `Error al actualizar el rol.` |

> `permisos` se **reemplaza** completamente: si omites el campo se guarda `[]`. Envía siempre el arreglo completo.

---

### 8.5 `PATCH /api/roles/:id/status`

**Descripción:** Activa o desactiva un rol. Los usuarios con un rol inactivo no pueden iniciar sesión ni operar.

**Body:**

```json
{
  "estatus": 0
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `estatus` | number \| string | ✅ | `1` → activo; **cualquier otro valor** → `0` (inactivo) |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Rol \"Encargado\" desactivado. Los usuarios asignados a este rol no podrán acceder hasta que se reactive o cambie su rol.",
  "usuariosAfectados": 5,
  "estatus": 0
}
```

Al activar:

```json
{
  "success": true,
  "message": "Rol \"Encargado\" activado exitosamente.",
  "usuariosAfectados": 5,
  "estatus": 1
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `No es posible desactivar el rol principal de Administrador por seguridad del sistema.` (solo `id_rol = 1` con `estatus = 0`) |
| `404` | `Rol no encontrado.` |
| `401` / `403` | Auth/rol |
| `500` | `Error al cambiar el estatus del rol.` |

---

### 8.6 `PUT /api/roles/assign-user/:id_usuario`

**Descripción:** Cambia el rol de un usuario.

**Body:**

```json
{
  "id_rol": 3
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `id_rol` | number | ✅ | Debe existir y estar activo |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Rol asignado correctamente a jperez."
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `Debes especificar el nuevo id_rol.` |
| `400` | `No puedes asignar un rol desactivado a un usuario.` |
| `404` | `El rol especificado no existe.` |
| `404` | `Usuario no encontrado.` |
| `401` / `403` | Auth/rol |
| `500` | `Error al asignar el rol al usuario.` |

> **Efecto inmediato:** el usuario objetivo conserva su JWT actual, pero en su siguiente petición `verifyToken` leerá el nuevo rol de la BD.

---

## 9. Endpoints de usuarios

Prefijo: `/api/users`

> **Todas** las rutas requieren `Authorization: Bearer <token>` **y** rol `Administrador`.

### 9.1 `GET /api/users`

**Descripción:** Lista/busca usuarios. **No** devuelve contraseñas.

| Propiedad | Valor |
|---|---|
| Auth | Bearer + Administrador |
| Query params | `search`, `id_rol`, `estatus` (todos opcionales) |

**Query params:**

| Param | Tipo | Descripción |
|---|---|---|
| `search` | string | Búsqueda parcial (`ILIKE`) en `nombre`, `apellido_paterno`, `apellido_materno`, `nombre_usuario`, `correo` |
| `id_rol` | number | Filtra por rol exacto |
| `estatus` | number | `1` activos, `0` inactivos; cualquier otro valor se interpreta como `0` |

**Ejemplo:**

```http
GET /api/users?search=juan&id_rol=2&estatus=1
```

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "data": [
    {
      "id_usuario": 3,
      "nombre": "Juan",
      "apellido_paterno": "Pérez",
      "apellido_materno": "López",
      "nombre_completo": "Juan Pérez López",
      "nombre_usuario": "jperez",
      "correo": "jperez@correo.com",
      "id_rol": 2,
      "rol_nombre": "Encargado",
      "rol_estatus": 1,
      "estatus": 1,
      "fecha_creacion": "2026-01-15T10:20:30.000Z"
    }
  ]
}
```

**Orden:** `id_usuario DESC` (más recientes primero).

**Paginación:** **no existe**. Devuelve todos los registros que coincidan. Si tu sistema maneja muchos usuarios, pagina en el cliente o solicita al backend que se agregue.

**Errores:** `401`, `403`, `500` (`Error al consultar usuarios.`).

---

### 9.2 `GET /api/users/:id`

**Descripción:** Detalle de un usuario.

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "data": {
    "id_usuario": 3,
    "nombre": "Juan",
    "apellido_paterno": "Pérez",
    "apellido_materno": "López",
    "nombre_completo": "Juan Pérez López",
    "nombre_usuario": "jperez",
    "correo": "jperez@correo.com",
    "id_rol": 2,
    "rol_nombre": "Encargado",
    "rol_estatus": 1,
    "estatus": 1,
    "fecha_creacion": "2026-01-15T10:20:30.000Z"
  }
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `404` | `Usuario no encontrado.` |
| `401` / `403` | Auth/rol |
| `500` | `Error al obtener el usuario.` |

---

### 9.3 `POST /api/users`

**Descripción:** Crea un usuario con estatus activo (`1`).

| Propiedad | Valor |
|---|---|
| Auth | Bearer + Administrador |
| Éxito | `201 Created` |

**Body:**

```json
{
  "nombre": "Juan",
  "apellido_paterno": "Pérez",
  "apellido_materno": "López",
  "nombre_usuario": "jperez",
  "contrasena": "MiClave123",
  "correo": "jperez@correo.com",
  "id_rol": 2
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `nombre` | string | ✅ | `trim()` |
| `apellido_paterno` | string | ✅ | `trim()` |
| `apellido_materno` | string | ❌ | `null` si se omite |
| `nombre_usuario` | string | ✅ | Único, `trim()` |
| `contrasena` | string | ✅ | Mínimo 6 caracteres; se cifra con bcrypt |
| `correo` | string | ✅ | Único, `trim()` |
| `id_rol` | number | ✅ | Debe existir y estar **activo** |

**Respuesta exitosa — `201`:**

```json
{
  "success": true,
  "message": "Usuario registrado exitosamente con estatus activo.",
  "data": {
    "id_usuario": 8,
    "nombre": "Juan",
    "apellido_paterno": "Pérez",
    "apellido_materno": "López",
    "nombre_usuario": "jperez",
    "correo": "jperez@correo.com",
    "id_rol": 2,
    "rol_nombre": "Encargado",
    "estatus": 1
  }
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `Todos los campos obligatorios deben ser completados (...)` |
| `400` | `La contraseña debe contener al menos 6 caracteres.` |
| `400` | `El nombre de usuario "X" ya está registrado. Elige otro.` |
| `400` | `El correo electrónico "X" ya está registrado.` |
| `400` | `El rol seleccionado no existe.` |
| `400` | `El rol "X" se encuentra inactivo. Debes asignar un rol activo.` |
| `401` / `403` | Auth/rol |
| `500` | `Error interno al registrar usuario.` |

> La contraseña **nunca** se devuelve en ninguna respuesta.

---

### 9.4 `PUT /api/users/:id`

**Descripción:** Actualización **parcial**. Solo se modifican los campos enviados.

**Body (todos opcionales):**

```json
{
  "nombre": "Juan Carlos",
  "apellido_paterno": "Pérez",
  "apellido_materno": "López",
  "correo": "jcperez@correo.com",
  "id_rol": 3,
  "estatus": 1,
  "nueva_contrasena": "OtraClave789"
}
```

| Campo | Tipo | Notas |
|---|---|---|
| `nombre` | string | Se actualiza si es truthy |
| `apellido_paterno` | string | Se actualiza si es truthy |
| `apellido_materno` | string \| null | Se actualiza si la clave está presente (`""` → `null`) |
| `correo` | string | Único entre usuarios |
| `id_rol` | number | Debe existir y estar activo si cambia |
| `estatus` | number | `1` activo, cualquier otro → `0` |
| `nueva_contrasena` | string | Mínimo 6; se cifra con bcrypt |

> **No se puede cambiar `nombre_usuario`** con este endpoint (no está en la lista de campos).

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Usuario actualizado correctamente."
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `El correo "X" ya está en uso por otro usuario.` |
| `400` | `El rol seleccionado no existe.` |
| `400` | `No puedes asignar un rol inactivo.` |
| `400` | `La nueva contraseña debe tener al menos 6 caracteres.` |
| `400` | `No se enviaron datos para modificar.` |
| `404` | `Usuario no encontrado.` |
| `401` / `403` | Auth/rol |
| `500` | `Error al actualizar el usuario.` |

> **Ojo:** `estatus` en este endpoint **sí** permite desactivar la propia cuenta (no tiene la protección de `PATCH /status`).

---

### 9.5 `PATCH /api/users/:id/status`

**Descripción:** Activa o desactiva un usuario.

**Body:**

```json
{
  "estatus": 0
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `estatus` | number \| string | ✅ | `1` → activo; cualquier otro → `0` |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Usuario desactivado. El acceso ha sido bloqueado.",
  "estatus": 0
}
```

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `No puedes desactivar tu propia cuenta mientras estás en sesión activa.` |
| `404` | `Usuario no encontrado.` |
| `401` / `403` | Auth/rol |
| `500` | `Error al cambiar estatus del usuario.` |

> Desactivar un usuario no invalida su JWT, pero la siguiente petición protegida devolverá `403 USER_INACTIVE`.

---

### 9.6 `POST /api/users/:id/reset-password-admin`

**Descripción:** El administrador restablece la contraseña de un usuario sin conocer la actual.

**Body:**

```json
{
  "nueva_contrasena": "ClaveTemporal123"
}
```

| Campo | Tipo | Obligatorio | Notas |
|---|---|:---:|---|
| `nueva_contrasena` | string | ✅ | Mínimo 6 caracteres |

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "message": "Contraseña de \"jperez\" restablecida exitosamente por el Administrador."
}
```

**Efectos secundarios:**

- Cifra la nueva contraseña con bcrypt.
- Invalida todos los tokens de recuperación pendientes del usuario.
- **No** invalida el JWT activo del usuario ni sus códigos 2FA.

**Errores:**

| HTTP | `message` |
|---|---|
| `400` | `Debes proporcionar una nueva contraseña de al menos 6 caracteres.` |
| `404` | `Usuario no encontrado.` |
| `401` / `403` | Auth/rol |
| `500` | `Error al restablecer la contraseña del usuario.` |

---

## 10. Endpoints de bitácora

Prefijo: `/api/bitacora`

> Requiere `Authorization: Bearer <token>` **y** rol `Administrador`.

### 10.1 `GET /api/bitacora`

**Descripción:** Consulta los eventos de auditoría (inicios de sesión, cambios, etc.).

**Query params:**

| Param | Tipo | Default | Descripción |
|---|---|---|---|
| `accion` | string | — | Búsqueda parcial `ILIKE` en `accion` |
| `nombre_usuario` | string | — | Búsqueda parcial `ILIKE` en `nombre_usuario` |
| `limit` | number | `50` | Mínimo `1`. Valores inválidos → `50` |
| `offset` | number | `0` | Mínimo `0`. Valores inválidos → `0` |

**Ejemplo:**

```http
GET /api/bitacora?accion=INICIO_SESION&nombre_usuario=jperez&limit=20&offset=0
```

**Respuesta exitosa — `200`:**

```json
{
  "success": true,
  "total": 342,
  "data": [
    {
      "id_bitacora": 342,
      "id_usuario": 3,
      "nombre_usuario": "jperez",
      "accion": "INICIO_SESION_2FA_EXITOSO",
      "ip_origen": "187.190.10.25",
      "fecha": "2026-10-29T14:32:10.000Z",
      "detalle": "Autenticación en 2 pasos completada exitosamente"
    }
  ]
}
```

| Campo | Tipo | Notas |
|---|---|---|
| `id_bitacora` | number | ID autoincremental |
| `id_usuario` | number \| null | `null` en eventos sin usuario identificado |
| `nombre_usuario` | string \| null | Puede ser el texto ingresado en un login fallido |
| `accion` | string | Ver catálogo en sección 13 |
| `ip_origen` | string \| null | IPv4/IPv6; `::1` normalizado a `127.0.0.1` |
| `fecha` | string | ISO 8601 |
| `detalle` | string \| null | Descripción legible |

**Orden:** `id_bitacora DESC` (más reciente primero).

**Errores:** `401`, `403`, `500` (`Error al consultar la bitácora de accesos.`).

---

## 11. Modelos de datos

> Estos son los campos expuestos por la API. Las tablas pueden tener columnas adicionales; no dependas de columnas no documentadas.

### 11.1 `usuarios`

| Columna | Tipo PostgreSQL | Expuesta | Notas |
|---|---|:---:|---|
| `id_usuario` | `INTEGER` (identity) | ✅ | PK |
| `nombre` | `VARCHAR` | ✅ | |
| `apellido_paterno` | `VARCHAR` | ✅ | |
| `apellido_materno` | `VARCHAR` | ✅ | Puede ser `null` |
| `nombre_usuario` | `VARCHAR` | ✅ | Único |
| `contrasena` | `VARCHAR` | ❌ | Hash bcrypt, nunca se expone |
| `correo` | `VARCHAR` | ✅ | Único |
| `id_rol` | `INTEGER` | ✅ | FK → `roles.id_rol` |
| `estatus` | `SMALLINT` | ✅ | `1` activo / `0` inactivo |
| `fecha_creacion` | `TIMESTAMP` | ✅ | Default `CURRENT_TIMESTAMP` |

### 11.2 `roles`

| Columna | Tipo PostgreSQL | Expuesta | Notas |
|---|---|:---:|---|
| `id_rol` | `INTEGER` (identity) | ✅ | PK |
| `nombre` | `VARCHAR` | ✅ | Único |
| `descripcion` | `VARCHAR`/`TEXT` | ✅ | Puede ser `null` |
| `permisos` | `JSONB` | ✅ | Se devuelve como arreglo |
| `estatus` | `SMALLINT` | ✅ | `1` activo / `0` inactivo |
| `fecha_creacion` | `TIMESTAMP` | ✅ | Default `CURRENT_TIMESTAMP` |

### 11.3 `codigos_2fa` (interna)

| Columna | Tipo | Notas |
|---|---|---|
| `id_codigo` | `INTEGER` (identity) | PK |
| `id_usuario` | `INTEGER` | FK → `usuarios` |
| `codigo` | `VARCHAR(10)` | 6 dígitos |
| `expiracion` | `TIMESTAMPTZ` | `NOW() + 10 min` |
| `usado` | `BOOLEAN` | Default `FALSE` |
| `intentos` | `SMALLINT` | Default `0` |
| `fecha_creacion` | `TIMESTAMP` | Default `CURRENT_TIMESTAMP` |

### 11.4 `tokens_recuperacion` (interna)

| Columna | Tipo | Notas |
|---|---|---|
| `id_token` | `INTEGER` (identity) | PK |
| `id_usuario` | `INTEGER` | FK → `usuarios` |
| `token` | `VARCHAR(255)` | Único, 64 hex chars |
| `expiracion` | `TIMESTAMPTZ` | `NOW() + 1 hora` |
| `usado` | `BOOLEAN` | Default `FALSE` |
| `fecha_creacion` | `TIMESTAMP` | Default `CURRENT_TIMESTAMP` |

### 11.5 `bitacora_accesos`

| Columna | Tipo | Notas |
|---|---|---|
| `id_bitacora` | `INTEGER` (identity) | PK |
| `id_usuario` | `INTEGER` \| null | Sin FK (tolera usuarios eliminados) |
| `nombre_usuario` | `VARCHAR(100)` \| null | |
| `accion` | `VARCHAR(100)` | Obligatorio |
| `ip_origen` | `VARCHAR(45)` \| null | IPv4/IPv6 |
| `fecha` | `TIMESTAMP` | Default `CURRENT_TIMESTAMP` |
| `detalle` | `TEXT` \| null | |

### 11.6 Diagrama de relaciones

```
roles (1) ──── (N) usuarios
                    │
                    ├── (N) codigos_2fa
                    └── (N) tokens_recuperacion

bitacora_accesos  ← registra eventos de cualquier usuario (sin FK estricta)
```

---

## 12. Catálogo de permisos y roles

### 12.1 Roles predefinidos

| `id_rol` | Nombre | `es_predefinido` | Permisos por defecto |
|:---:|---|:---:|---|
| `1` | `Administrador` | ✅ | `usuarios`, `roles`, `clientes`, `proveedores`, `productos`, `inventario`, `ventas`, `compras`, `ingresos`, `gastos`, `caja`, `cxc`, `cxp`, `reportes`, `bitacora` |
| `2` | `Encargado` | ✅ | `clientes`, `proveedores`, `productos`, `inventario`, `ventas`, `compras`, `ingresos`, `gastos`, `caja`, `cxc`, `cxp` |
| `3` | `Vendedor` | ✅ | `ventas`, `clientes`, `productos_consulta`, `caja` |

> `es_predefinido` es `true` únicamente para `id_rol` 1, 2 y 3 (regla fija en el código, no una columna de BD).

### 12.2 Catálogo completo de strings de permisos

```
usuarios
roles
clientes
proveedores
productos
productos_consulta
inventario
ventas
compras
ingresos
gastos
caja
cxc
cxp
reportes
bitacora
```

### 12.3 Autorización real

- La API **solo** verifica el nombre del rol para las rutas administrativas (`rol_nombre === "Administrador"`).
- Los `permisos` se devuelven al cliente para que **tu sistema** controle el acceso a sus propios módulos.
- Un permiso desconocido no rompe nada: se almacena y se devuelve tal cual.

---

## 13. Catálogo de acciones de bitácora

Valores observados en `accion` (búsqueda `ILIKE`, por lo que puedes filtrar por prefijo):

| Acción | Origen | Significado |
|---|---|---|
| `INICIO_SESION_FALLIDO` | login | Usuario inexistente o contraseña incorrecta |
| `ACCESO_DENEGADO_USUARIO_INACTIVO` | login | Credenciales válidas, usuario inactivo |
| `ACCESO_DENEGADO_ROL_INACTIVO` | login | Credenciales válidas, rol inactivo |
| `INICIO_SESION_PASO1_EXITOSO` | login | Credenciales válidas, código 2FA enviado |
| `INICIO_SESION_2FA_FALLIDO` | verify-2fa | Código incorrecto, usado o expirado |
| `INICIO_SESION_2FA_EXITOSO` | verify-2fa | Login completo |
| `REENVIO_CODIGO_2FA` | resend-2fa | Nuevo código solicitado |
| `CIERRE_SESION_MANUAL` | logout | Logout con `motivo: "manual"` |
| `CIERRE_SESION_INACTIVIDAD` | logout | Logout con `motivo: "inactividad"` |
| `SOLICITUD_RECUPERACION_CONTRASENA` | forgot-password | Token de recuperación generado |
| `RESTABLECIMIENTO_CONTRASENA_EXITOSO` | reset-password | Contraseña restablecida con token |
| `CAMBIO_CONTRASENA_EXITOSO` | change-password | Usuario cambió su contraseña |
| `CAMBIO_CONTRASENA_FALLIDO` | change-password | Contraseña actual incorrecta |
| `CREACION_ROL` | POST /roles | Rol creado |
| `MODIFICACION_ROL` | PUT /roles/:id | Rol actualizado |
| `ACTIVACION_ROL` | PATCH /roles/:id/status | Rol activado |
| `DESACTIVACION_ROL` | PATCH /roles/:id/status | Rol desactivado |
| `CAMBIO_ROL_USUARIO` | PUT /roles/assign-user/:id | Rol reasignado |
| `CREACION_USUARIO` | POST /users | Usuario creado |
| `MODIFICACION_USUARIO` | PUT /users/:id | Usuario actualizado |
| `ACTIVACION_USUARIO` | PATCH /users/:id/status | Usuario activado |
| `DESACTIVACION_USUARIO` | PATCH /users/:id/status | Usuario desactivado |
| `RESTABLECIMIENTO_CONTRASENA_ADMIN` | POST /users/:id/reset-password-admin | Reset por administrador |

> Los errores al escribir en bitácora **no** interrumpen la operación principal: se registran en consola y la petición continúa.

---

## 14. Ejemplos de integración

### 14.1 Cliente JavaScript / TypeScript (fetch)

```ts
const API_BASE = 'https://tu-dominio.com/api';

let sessionToken: string | null = null;

function authHeaders(): Record<string, string> {
  return sessionToken
    ? { 'Content-Type': 'application/json', Authorization: `Bearer ${sessionToken}` }
    : { 'Content-Type': 'application/json' };
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: { ...authHeaders(), ...(options.headers || {}) }
  });

  const body = await res.json().catch(() => ({
    success: false,
    message: 'Respuesta no válida del servidor'
  }));

  if (!res.ok) {
    // Sesión expirada o cuenta desactivada: fuerza re-login
    if (res.status === 401 || (res.status === 403 && body.code === 'USER_INACTIVE')) {
      sessionToken = null;
    }
    throw new Error(body.message || `Error HTTP ${res.status}`);
  }

  return body as T;
}

// --- Paso 1 ---
async function login(nombre_usuario: string, contrasena: string) {
  return request<{ success: true; requires2FA: true; tempToken: string; emailMasked: string }>(
    '/auth/login',
    { method: 'POST', body: JSON.stringify({ nombre_usuario, contrasena }) }
  );
}

// --- Paso 2 ---
async function verify2FA(tempToken: string, codigo: string) {
  const data = await request<{ success: true; token: string; user: unknown }>(
    '/auth/verify-2fa',
    { method: 'POST', body: JSON.stringify({ tempToken, codigo }) }
  );
  sessionToken = data.token;
  return data;
}

// --- Uso ---
const step1 = await login('jperez', 'MiClave123');
console.log('Código enviado a', step1.emailMasked);

// (el usuario introduce el código de 6 dígitos)
const session = await verify2FA(step1.tempToken, '482913');

const me = await request('/auth/me');
```

### 14.2 Cliente Axios

```js
import axios from 'axios';

const http = axios.create({
  baseURL: 'https://tu-dominio.com/api',
  headers: { 'Content-Type': 'application/json' }
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('contapyme_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

http.interceptors.response.use(
  (res) => res,
  (error) => {
    const status = error.response?.status;
    const data = error.response?.data;
    if (status === 401 || (status === 403 && data?.code === 'USER_INACTIVE')) {
      localStorage.removeItem('contapyme_token');
      window.location.href = '/login';
    }
    return Promise.reject(new Error(data?.message || 'Error de red'));
  }
);

// Login
const { data: step1 } = await http.post('/auth/login', {
  nombre_usuario: 'jperez',
  contrasena: 'MiClave123'
});

// Verificar 2FA
const { data: session } = await http.post('/auth/verify-2fa', {
  tempToken: step1.tempToken,
  codigo: '482913'
});

localStorage.setItem('contapyme_token', session.token);
```

### 14.3 cURL — recorrido completo

```bash
BASE="http://localhost:5000/api"

# 1) Login
LOGIN=$(curl -s -X POST "$BASE/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"nombre_usuario":"jperez","contrasena":"MiClave123"}')

TEMP=$(echo "$LOGIN" | jq -r '.tempToken')

# 2) Verificar 2FA (el código llega al correo / consola del servidor)
VERIFY=$(curl -s -X POST "$BASE/auth/verify-2fa" \
  -H "Content-Type: application/json" \
  -d "{\"tempToken\":\"$TEMP\",\"codigo\":\"482913\"}")

TOKEN=$(echo "$VERIFY" | jq -r '.token')

# 3) Datos del usuario
curl -s "$BASE/auth/me" -H "Authorization: Bearer $TOKEN" | jq

# 4) Listar usuarios (Administrador)
curl -s "$BASE/users?search=juan" -H "Authorization: Bearer $TOKEN" | jq
```

### 14.4 Python (requests)

```python
import requests

BASE = "https://tu-dominio.com/api"

# 1) Login
r = requests.post(f"{BASE}/auth/login", json={
    "nombre_usuario": "jperez",
    "contrasena": "MiClave123"
})
r.raise_for_status()
step1 = r.json()
print("Código enviado a:", step1["emailMasked"])

# 2) Verificar 2FA
r = requests.post(f"{BASE}/auth/verify-2fa", json={
    "tempToken": step1["tempToken"],
    "codigo": "482913"
})
r.raise_for_status()
session = r.json()
token = session["token"]

# 3) Petición autenticada
headers = {"Authorization": f"Bearer {token}"}
r = requests.get(f"{BASE}/auth/me", headers=headers)
r.raise_for_status()
print(r.json()["user"])
```

### 14.5 Variables sugeridas para Postman / Insomnia

| Variable | Valor |
|---|---|
| `baseUrl` | `http://localhost:5000/api` |
| `tempToken` | *(se llena tras el login)* |
| `token` | *(se llena tras verify-2fa)* |
| `id_usuario` | `3` |
| `id_rol` | `2` |

En Postman, en la pestaña **Authorization** de la colección: `Bearer Token` → `{{token}}`.

---

## 15. Consideraciones importantes (gotchas)

### 15.1 Logout no revoca el JWT

`POST /auth/logout` solo registra el evento en bitácora. El token sigue siendo válido hasta su `exp`. Si necesitas revocación inmediata, debes:

1. Eliminar el token del almacenamiento del cliente, **y**
2. (Opcional, requiere cambio de backend) mantener una lista negra de tokens o un `token_version` por usuario.

### 15.2 Desactivar usuario/rol no invalida tokens, pero sí bloquea

El JWT sigue siendo criptográficamente válido, pero `verifyToken` consulta la BD en cada petición y devuelve `403` si el usuario o el rol están inactivos. Tu cliente debe manejar `403 USER_INACTIVE` / `403 ROLE_INACTIVE` como "sesión terminada".

### 15.3 Siempre hay 2FA

No existe un modo "login directo". Todo login requiere `verify-2fa`. Si tu integración es servidor-a-servidor (sin humano), necesitarás acceso al buzón de correo o un mecanismo alternativo que no está implementado.

### 15.4 El código 2FA solo se puede usar una vez

Si tu cliente hace doble submit (auto-submit al 6º dígito + clic en el botón), la segunda petición devuelve `400 "Este código ya fue utilizado..."`. Bloquea el formulario mientras `loading === true`.

### 15.5 `tempToken` vs `token`

| | `tempToken` | `token` |
|---|---|---|
| Emitido por | `login` / `resend-2fa` | `verify-2fa` |
| Vigencia | 10 min | `JWT_EXPIRES_IN` (8h por defecto) |
| Uso | Solo `verify-2fa` / `resend-2fa` | Rutas protegidas |
| Se envía en | Body | Header `Authorization` |

Enviar un `tempToken` en `Authorization` produce `401 Token inválido o manipulado`.

### 15.6 `total` de bitácora no respeta los filtros

`GET /api/bitacora` devuelve `total` como `COUNT(*)` de **toda** la tabla, sin aplicar `accion` ni `nombre_usuario`. Para paginar con filtros, calcula el total en el cliente o solicita un cambio al backend.

### 15.7 Sin paginación en usuarios y roles

`GET /api/users` y `GET /api/roles` devuelven todos los registros. Con catálogos grandes, el rendimiento puede degradarse.

### 15.8 `PUT` de rol reemplaza permisos

En `PUT /api/roles/:id`, si omites `permisos` se guarda `[]`. Envía siempre el arreglo completo, incluso si no cambió.

### 15.9 `PUT` de usuario es parcial, `PUT` de rol es total

- `PUT /api/users/:id`: solo actualiza los campos presentes.
- `PUT /api/roles/:id`: exige `nombre` y reemplaza `descripcion` y `permisos` con lo enviado (o `null` / `[]`).

### 15.10 `estatus` acepta cualquier valor

Los endpoints de toggle hacen `Number(estatus) === 1 ? 1 : 0`. Enviar `true`, `"1"`, `2` o `"activo"` resulta en `0` excepto `1`/`"1"`. Envía siempre `1` o `0`.

### 15.11 `id_rol = 1` no se puede desactivar

Protección de seguridad: el rol Administrador principal no puede desactivarse. Otros roles sí.

### 15.12 No puedes desactivarte a ti mismo por `PATCH /status`

`PATCH /api/users/:id/status` con tu propio `id_usuario` y `estatus = 0` devuelve `400`. Pero `PUT /api/users/:id` con `estatus: 0` **sí** lo permite (inconsistencia conocida).

### 15.13 Correos y nombres de usuario son case-sensitive

La comparación en login es `u.nombre_usuario = $1 OR u.correo = $1` (exacta). `JPEREZ` no coincide con `jperez`. La búsqueda en listados sí usa `ILIKE` (insensible a mayúsculas).

### 15.14 `devToken` y `devCode`

- `devCode` fue **eliminado** de las respuestas de login y resend.
- `devToken` solo aparece en `forgot-password` si `NODE_ENV !== 'production'`.
- En producción, el código 2FA y el token de recuperación solo llegan por correo.

### 15.15 SMTP no configurado

Si `SMTP_HOST`/`SMTP_USER` están vacíos, el correo no se envía y el código se imprime en la consola del servidor. El endpoint responde `200` igual. En producción esto es un fallo silencioso: verifica la configuración SMTP.

### 15.16 Rate limit por IP

Si tu sistema hace muchas peticiones desde una sola IP (por ejemplo, un backend que actúa en nombre de muchos usuarios), alcanzarás los límites de `login`/`forgot-password` (30/10 min) rápidamente. Coordina con el equipo de backend para ajustar los límites o usar autenticación servidor-a-servidor.

### 15.17 El orden de las rutas importa para `:id`

`GET /api/roles/:id` solo acepta `GET`; `PUT /api/roles/assign-user/:id_usuario` es un `PUT` distinto, por lo que no hay colisión. Aun así, envía siempre IDs numéricos para evitar `500`.

### 15.18 `emailMasked` no es el correo real

Solo sirve para mostrar al usuario. No intentes reconstruirlo ni usarlo para enviar correos.

### 15.19 Zona horaria

Las expiraciones (`codigos_2fa.expiracion`, `tokens_recuperacion.expiracion`) son `TIMESTAMPTZ` y se calculan con `NOW()` en PostgreSQL. No envíes fechas de expiración desde el cliente: la API no las acepta.

### 15.20 Sin endpoint de registro público

No existe `POST /auth/register`. Los usuarios solo los crea un Administrador vía `POST /api/users`.

### 15.21 Sin endpoint de eliminación

No hay `DELETE` para usuarios, roles ni bitácora. Se usa desactivación lógica (`estatus = 0`).

---

## 16. Checklist de integración

### Backend / infraestructura

- [ ] `DATABASE_URL` válida y `npm run db:check` en verde.
- [ ] `JWT_SECRET` y `JWT_2FA_SECRET` definidos y **distintos** entre sí y de los valores por defecto.
- [ ] `JWT_EXPIRES_IN` acorde a tu política de sesión (por defecto `8h`).
- [ ] SMTP configurado y probado (sin SMTP el 2FA solo sale por consola).
- [ ] `NODE_ENV=production` en producción (oculta `devToken` y detalles de error).
- [ ] HTTPS en producción.
- [ ] `trust proxy` configurado si hay reverse proxy.
- [ ] CORS revisado si tu frontend vive en otro dominio.

### Cliente

- [ ] Implementado el flujo de dos pasos (`login` → `verify-2fa`).
- [ ] Guardado del `token` en almacenamiento seguro.
- [ ] Header `Authorization: Bearer <token>` en rutas protegidas.
- [ ] Manejo de `401` → limpiar sesión y volver al login.
- [ ] Manejo de `403 USER_INACTIVE` / `403 ROLE_INACTIVE` → limpiar sesión y mostrar mensaje.
- [ ] Manejo de `429` → mostrar tiempo de espera.
- [ ] *Lock* anti doble-submit en el formulario 2FA.
- [ ] Botón de reenvío con cooldown (el backend permite 30/10 min; el frontend original usa 60 s).
- [ ] Envío de `Origin` en `forgot-password` si el enlace debe apuntar a tu app.
- [ ] Uso de `permisos` del usuario para mostrar/ocultar módulos.
- [ ] Rehidratación de sesión con `GET /auth/me` al iniciar la app.
- [ ] Formato de fechas tolerante a ISO 8601 con `Z`.
- [ ] IDs enviados como números.

### Pruebas mínimas

- [ ] Login con credenciales correctas → `200` + `tempToken`.
- [ ] Login con contraseña incorrecta → `401`.
- [ ] Login con usuario inactivo → `403 USER_INACTIVE`.
- [ ] Login con rol inactivo → `403 ROLE_INACTIVE`.
- [ ] Verify con código correcto → `200` + `token`.
- [ ] Verify con código incorrecto → `400` "incorrecto".
- [ ] Verify repetido con el mismo código → `400` "ya utilizado".
- [ ] Verify con código vencido → `400` "expirado".
- [ ] Verify con `tempToken` vencido → `401`.
- [ ] Ruta protegida sin token → `401`.
- [ ] Ruta protegida con token de no-Administrador → `403`.
- [ ] `GET /auth/me` → datos correctos.
- [ ] `change-password` con contraseña actual incorrecta → `400`.
- [ ] `forgot-password` con usuario inexistente → `200` genérico.
- [ ] `reset-password` con token usado → `400`.
- [ ] CRUD de roles y usuarios → códigos `200/201`.
- [ ] `GET /bitacora` con filtros y paginación.

---

## Anexo A. Resumen de endpoints

| # | Método | Ruta | Auth | Éxito | Descripción |
|:--:|---|---|---|:--:|---|
| 1 | `GET` | `/api` | — | 200 | Health check / índice |
| 2 | `POST` | `/api/auth/login` | — | 200 | Login paso 1 (credenciales) |
| 3 | `POST` | `/api/auth/verify-2fa` | — | 200 | Login paso 2 (código) |
| 4 | `POST` | `/api/auth/resend-2fa` | — | 200 | Reenviar código |
| 5 | `POST` | `/api/auth/logout` | opcional | 200 | Registrar cierre de sesión |
| 6 | `POST` | `/api/auth/forgot-password` | — | 200 | Solicitar recuperación |
| 7 | `POST` | `/api/auth/reset-password` | — | 200 | Restablecer con token |
| 8 | `POST` | `/api/auth/change-password` | Bearer | 200 | Cambiar contraseña propia |
| 9 | `GET` | `/api/auth/me` | Bearer | 200 | Usuario actual |
| 10 | `GET` | `/api/roles` | Bearer + Admin | 200 | Listar roles |
| 11 | `GET` | `/api/roles/:id` | Bearer + Admin | 200 | Detalle de rol |
| 12 | `POST` | `/api/roles` | Bearer + Admin | 201 | Crear rol |
| 13 | `PUT` | `/api/roles/:id` | Bearer + Admin | 200 | Actualizar rol |
| 14 | `PATCH` | `/api/roles/:id/status` | Bearer + Admin | 200 | Activar/desactivar rol |
| 15 | `PUT` | `/api/roles/assign-user/:id_usuario` | Bearer + Admin | 200 | Reasignar rol a usuario |
| 16 | `GET` | `/api/users` | Bearer + Admin | 200 | Listar/buscar usuarios |
| 17 | `GET` | `/api/users/:id` | Bearer + Admin | 200 | Detalle de usuario |
| 18 | `POST` | `/api/users` | Bearer + Admin | 201 | Crear usuario |
| 19 | `PUT` | `/api/users/:id` | Bearer + Admin | 200 | Actualizar usuario |
| 20 | `PATCH` | `/api/users/:id/status` | Bearer + Admin | 200 | Activar/desactivar usuario |
| 21 | `POST` | `/api/users/:id/reset-password-admin` | Bearer + Admin | 200 | Reset por administrador |
| 22 | `GET` | `/api/bitacora` | Bearer + Admin | 200 | Consultar auditoría |

---

## Anexo B. Glosario

| Término | Significado |
|---|---|
| **JWT** | JSON Web Token firmado con HMAC-SHA256 |
| **`tempToken`** | JWT temporal que solo autoriza el paso 2 del login |
| **2FA** | Autenticación en dos pasos por código de 6 dígitos enviado por correo |
| **`estatus`** | Estado lógico: `1` activo, `0` inactivo |
| **Rol** | Perfil que agrupa permisos; determina acceso administrativo |
| **Permiso** | String que tu sistema usa para habilitar módulos (ej. `ventas`) |
| **Bitácora** | Registro de auditoría de eventos de seguridad y cambios |
| **Rate limit** | Límite de peticiones por IP y ventana de tiempo |
| **Envelope** | Estructura estándar de respuesta (`success`, `message`, `data`) |

---

*Fin del documento.*
