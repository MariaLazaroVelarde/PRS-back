# 📚 API Documentation - MS-Calidad-Agua

## 🌟 Información General

**Microservicio**: MS-CALIDAD-AGUA (8087)  
**Versión**: v2  
**Base URL**: `http://localhost:8087/api/v2`  
**Tipo**: Solo ADMIN (Administradores autorizados)  
**Tecnología**: Spring Boot WebFlux (Reactivo)  

---

## 🔐 Autenticación
- **Tipo**: OAuth2 Resource Server
- **Token**: JWT Bearer Token requerido en header `Authorization`
- **Permisos**: Solo usuarios con rol ADMIN pueden acceder

---

## 📜 Resumen Completo de Endpoints

### 📋 Registro Diario de Calidad
- `GET /api/v2/dailyrecords` - Listar todos los registros
- `GET /api/v2/dailyrecords/{id}` - Obtener registro por ID
- `POST /api/v2/dailyrecords` - Crear nuevo registro
- `PUT /api/v2/dailyrecords/{id}` - Actualizar registro
- `DELETE /api/v2/dailyrecords/{id}` - Eliminación lógica
- `DELETE /api/v2/dailyrecords/{id}/physical` - Eliminación física
- `PUT /api/v2/dailyrecords/{id}/restore` - Restaurar registro

### 🧪 Pruebas de Calidad
- `GET /api/v2/qualitytests` - Listar todas las pruebas
- `GET /api/v2/qualitytests/{id}` - Obtener prueba por ID
- `POST /api/v2/qualitytests` - Crear nueva prueba
- `PUT /api/v2/qualitytests/{id}` - Actualizar prueba
- `DELETE /api/v2/qualitytests/{id}` - Eliminación lógica
- `DELETE /api/v2/qualitytests/{id}/physical` - Eliminación física
- `PUT /api/v2/qualitytests/{id}/restore` - Restaurar prueba

### ⚗️ Parámetros de Calidad
- `GET /api/v2/qualityparameters` - Listar todos los parámetros
- `GET /api/v2/qualityparameters/active` - Parámetros activos
- `GET /api/v2/qualityparameters/inactive` - Parámetros inactivos
- `GET /api/v2/qualityparameters/{id}` - Obtener parámetro por ID
- `POST /api/v2/qualityparameters` - Crear nuevo parámetro
- `PUT /api/v2/qualityparameters/{id}` - Actualizar parámetro
- `DELETE /api/v2/qualityparameters/{id}` - Eliminar parámetro
- `PATCH /api/v2/qualityparameters/{id}/activate` - Activar parámetro
- `PATCH /api/v2/qualityparameters/{id}/deactivate` - Desactivar parámetro

### 📍 Puntos de Muestreo
- `GET /api/v2/testingpoints` - Listar todos los puntos
- `GET /api/v2/testingpoints/active` - Puntos activos
- `GET /api/v2/testingpoints/inactive` - Puntos inactivos
- `GET /api/v2/testingpoints/{id}` - Obtener punto por ID
- `POST /api/v2/testingpoints` - Crear nuevo punto
- `PUT /api/v2/testingpoints/{id}` - Actualizar punto
- `DELETE /api/v2/testingpoints/{id}` - Eliminar punto
- `PATCH /api/v2/testingpoints/{id}/activate` - Activar punto
- `PATCH /api/v2/testingpoints/{id}/deactivate` - Desactivar punto

### 🚨 Incidencias de Calidad
- `GET /api/v2/qualityincidents` - Listar todas las incidencias
- `GET /api/v2/qualityincidents/resolved` - Incidencias resueltas
- `GET /api/v2/qualityincidents/unresolved` - Incidencias pendientes
- `GET /api/v2/qualityincidents/{id}` - Obtener incidencia por ID
- `POST /api/v2/qualityincidents` - Crear nueva incidencia
- `PUT /api/v2/qualityincidents/{id}` - Actualizar incidencia
- `DELETE /api/v2/qualityincidents/{id}` - Eliminar incidencia

### 👥 Usuarios
- `GET /api/v2/users` - Listar todos los usuarios
- `GET /api/v2/users/active` - Usuarios activos
- `GET /api/v2/users/inactive` - Usuarios inactivos
- `GET /api/v2/users/{id}` - Obtener usuario por ID
- `POST /api/v2/users` - Crear nuevo usuario
- `PUT /api/v2/users/{id}` - Actualizar usuario
- `DELETE /api/v2/users/{id}` - Eliminar usuario
- `PATCH /api/v2/users/{id}/activate` - Activar usuario
- `PATCH /api/v2/users/{id}/deactivate` - Desactivar usuario

**Total de Endpoints**: 47 endpoints distribuidos en 6 módulos principales

---

## 📋 Endpoints Detallados

### 🔄 Daily Records (Registros Diarios de Calidad)
**Base Path**: `/api/v2/dailyrecords`

#### GET /api/v2/dailyrecords
**Descripción**: Obtener todos los registros diarios de calidad del agua  
**Permisos**: ADMIN  
**Respuesta**:
```json
{
  "success": true,
  "data": [
    {
      "id": "64f1a2b3c4d5e6f7a8b9c0d1",
      "recordCode": "DRec001",
      "organizationId": "org123",
      "testingPointIds": ["tp001", "tp002"],
      "recordDate": "2023-06-05T08:00:00Z",
      "recordType": "CHLORINE",
      "level": 0.8,
      "acceptable": true,
      "actionRequired": false,
      "recordedByUserId": "user123",
      "observations": "Nivel óptimo de cloro",
      "amount": 150.5,
      "createdAt": "2023-06-05T08:15:00Z"
    }
  ]
}
```

#### GET /api/v2/dailyrecords/{id}
**Descripción**: Obtener un registro diario específico por ID  
**Parámetros**: 
- `id` (path): ID del registro

#### POST /api/v2/dailyrecords
**Descripción**: Crear un nuevo registro diario  
**Body**:
```json
{
  "organizationId": "org123",
  "testingPointIds": ["tp001"],
  "recordDate": "2023-06-05T08:00:00Z",
  "recordType": "CHLORINE",
  "level": 0.8,
  "acceptable": true,
  "actionRequired": false,
  "recordedByUserId": "user123",
  "observations": "Nivel óptimo de cloro",
  "amount": 150.5
}
```

#### PUT /api/v2/dailyrecords/{id}
**Descripción**: Actualizar un registro diario existente
**Nota**: Genera automáticamente un nuevo código de registro

#### DELETE /api/v2/dailyrecords/{id}
**Descripción**: Eliminación lógica del registro (soft delete)
**Respuesta**: 204 No Content

#### DELETE /api/v2/dailyrecords/{id}/physical
**Descripción**: Eliminación física permanente del registro
**Respuesta**: 204 No Content

#### PUT /api/v2/dailyrecords/{id}/restore
**Descripción**: Restaurar un registro eliminado lógicamente

---

### 🧪 Quality Tests (Pruebas de Calidad)
**Base Path**: `/api/v2/qualitytests`

#### GET /api/v2/qualitytests
**Descripción**: Obtener todas las pruebas de calidad realizadas  
**Respuesta**:
```json
{
  "success": true,
  "data": [
    {
      "id": "64f1a2b3c4d5e6f7a8b9c0d2",
      "testCode": "ANL001",
      "organizationId": "org123",
      "testingPointId": "tp001",
      "testDate": "2023-06-05T08:00:00Z",
      "waterTemperature": 18.5,
      "results": [
        {
          "parameterId": "param001",
          "parameterCode": "CLORO_LIBRE",
          "measuredValue": 0.8,
          "unit": "mg/L",
          "status": "ACCEPTABLE",
          "observations": "Dentro del rango óptimo"
        }
      ],
      "status": "COMPLETED",
      "generalObservations": "Agua clara, sin olor ni sabor extraño",
      "createdAt": "2023-06-05T08:30:00Z"
    }
  ]
}
```

#### GET /api/v2/qualitytests/{id}
**Descripción**: Obtener una prueba de calidad específica por ID

#### POST /api/v2/qualitytests
**Descripción**: Crear una nueva prueba de calidad  
**Body**:
```json
{
  "organizationId": "org123",
  "testingPointId": "tp001",
  "testDate": "2023-06-05T08:00:00Z",
  "waterTemperature": 18.5,
  "generalObservations": "Agua clara",
  "results": [
    {
      "parameterId": "param001",
      "parameterCode": "CLORO_LIBRE", 
      "measuredValue": 0.8,
      "unit": "mg/L",
      "status": "ACCEPTABLE",
      "observations": "Óptimo"
    }
  ]
}
```

#### PUT /api/v2/qualitytests/{id}
**Descripción**: Actualizar una prueba de calidad existente

#### DELETE /api/v2/qualitytests/{id}
**Descripción**: Eliminación lógica de la prueba (soft delete)
**Respuesta**: 204 No Content

#### DELETE /api/v2/qualitytests/{id}/physical
**Descripción**: Eliminación física permanente de la prueba
**Respuesta**: 204 No Content

#### PUT /api/v2/qualitytests/{id}/restore
**Descripción**: Restaurar una prueba eliminada lógicamente

---

### ⚗️ Quality Parameters (Parámetros de Calidad)
**Base Path**: `/api/v2/qualityparameters`

#### GET /api/v2/qualityparameters
**Descripción**: Obtener todos los parámetros de calidad configurados

#### GET /api/v2/qualityparameters/active
**Descripción**: Obtener solo parámetros activos

#### GET /api/v2/qualityparameters/inactive  
**Descripción**: Obtener solo parámetros inactivos

#### GET /api/v2/qualityparameters/{id}
**Descripción**: Obtener un parámetro de calidad específico por ID

#### POST /api/v2/qualityparameters
**Descripción**: Crear un nuevo parámetro de calidad  
**Body**:
```json
{
  "organizationId": "org123",
  "parameterCode": "CLORO_LIBRE",
  "parameterName": "Cloro Libre Residual",
  "unit": "mg/L",
  "minValue": 0.3,
  "maxValue": 1.5,
  "optimalMin": 0.5,
  "optimalMax": 1.0,
  "testFrequency": "DAILY",
  "description": "Medición de cloro libre en agua potable"
}
```

#### PUT /api/v2/qualityparameters/{id}
**Descripción**: Actualizar un parámetro de calidad existente

#### DELETE /api/v2/qualityparameters/{id}
**Descripción**: Eliminar un parámetro de calidad
**Respuesta**: 204 No Content

#### PATCH /api/v2/qualityparameters/{id}/activate
**Descripción**: Activar un parámetro

#### PATCH /api/v2/qualityparameters/{id}/deactivate
**Descripción**: Desactivar un parámetro

---

### 📍 Testing Points (Puntos de Muestreo)
**Base Path**: `/api/v2/testingpoints`

#### GET /api/v2/testingpoints
**Descripción**: Obtener todos los puntos de muestreo  
**Respuesta**:
```json
{
  "success": true,
  "data": [
    {
      "id": "64f1a2b3c4d5e6f7a8b9c0d3",
      "pointCode": "PM001",
      "pointName": "Reservorio Principal",
      "pointType": "RESERVORIO",
      "organizationId": "org123",
      "zoneId": "zone001",
      "locationDescription": "Entrada del reservorio principal",
      "street": null,
      "coordinates": {
        "latitude": -12.0464,
        "longitude": -77.0428
      },
      "status": "ACTIVE",
      "createdAt": "2023-06-01T09:00:00Z"
    },
    {
      "id": "64f1a2b3c4d5e6f7a8b9c0d4",
      "pointCode": "PM002",
      "pointName": "Casa Rodriguez",
      "pointType": "DOMICILIO",
      "organizationId": "org123",
      "zoneId": "zone002",
      "locationDescription": "Vivienda unifamiliar",
      "street": "Calle Los Pinos 123",
      "coordinates": {
        "latitude": -12.0480,
        "longitude": -77.0445
      },
      "status": "ACTIVE",
      "createdAt": "2023-06-01T10:00:00Z"
    }
  ]
}
```

#### POST /api/v2/testingpoints
**Descripción**: Crear un nuevo punto de muestreo  
**Body para RESERVORIO**:
```json
{
  "organizationId": "org123",
  "pointName": "Reservorio Secundario",
  "pointType": "RESERVORIO",
  "zoneId": "zone001",
  "locationDescription": "Reservorio de distribución",
  "coordinates": {
    "latitude": -12.0464,
    "longitude": -77.0428
  }
}
```

**Body para DOMICILIO/SUMINISTRO**:
```json
{
  "organizationId": "org123",
  "pointName": "Casa Martínez",
  "pointType": "DOMICILIO",
  "zoneId": "zone002",
  "locationDescription": "Vivienda en zona residencial",
  "street": "Av. Los Rosales 456",
  "coordinates": {
    "latitude": -12.0495,
    "longitude": -77.0460
  }
}
```

**Notas importantes**:
- **Campo `street`**: Obligatorio para puntos tipo `DOMICILIO` y `SUMINISTRO`
- **Campo `street`**: Opcional para otros tipos (`RESERVORIO`, `RED_DISTRIBUCION`)
- **Tipos válidos**: `RESERVORIO`, `RED_DISTRIBUCION`, `DOMICILIO`, `SUMINISTRO`

#### GET /api/v2/testingpoints/active
**Descripción**: Obtener solo puntos de muestreo activos

#### GET /api/v2/testingpoints/inactive
**Descripción**: Obtener solo puntos de muestreo inactivos

#### GET /api/v2/testingpoints/{id}
**Descripción**: Obtener un punto de muestreo específico por ID

#### PUT /api/v2/testingpoints/{id}
**Descripción**: Actualizar un punto de muestreo existente
**Nota**: El campo `street` sigue las mismas reglas que en la creación

#### PATCH /api/v2/testingpoints/{id}/activate
**Descripción**: Activar un punto de muestreo

#### PATCH /api/v2/testingpoints/{id}/deactivate
**Descripción**: Desactivar un punto de muestreo

#### DELETE /api/v2/testingpoints/{id}
**Descripción**: Eliminar un punto de muestreo

---

### 🚨 Quality Incidents (Incidencias de Calidad)
**Base Path**: `/api/v2/qualityincidents`

#### GET /api/v2/qualityincidents
**Descripción**: Obtener todas las incidencias de calidad

#### GET /api/v2/qualityincidents/resolved
**Descripción**: Obtener solo incidencias resueltas

#### GET /api/v2/qualityincidents/unresolved
**Descripción**: Obtener solo incidencias pendientes

#### GET /api/v2/qualityincidents/{id}
**Descripción**: Obtener una incidencia específica por ID

#### POST /api/v2/qualityincidents
**Descripción**: Reportar una nueva incidencia de calidad  
**Body**:
```json
{
  "organizationId": "org123",
  "incidentCode": "INC001",
  "testingPointId": "tp001",
  "incidentType": "CALIDAD_BAJA",
  "description": "Cloro por debajo del mínimo requerido",
  "severity": "HIGH",
  "reportedDate": "2023-06-05T10:00:00Z",
  "reportedBy": "user123"
}
```

#### PUT /api/v2/qualityincidents/{id}
**Descripción**: Actualizar una incidencia existente

#### DELETE /api/v2/qualityincidents/{id}
**Descripción**: Eliminar una incidencia
**Respuesta**: 204 No Content

---

### 👥 Users (Usuarios)
**Base Path**: `/api/v2/users`

#### GET /api/v2/users
**Descripción**: Obtener todos los usuarios del sistema

#### GET /api/v2/users/active
**Descripción**: Obtener solo usuarios activos

#### GET /api/v2/users/inactive
**Descripción**: Obtener solo usuarios inactivos

#### GET /api/v2/users/{id}
**Descripción**: Obtener un usuario específico por ID

#### POST /api/v2/users
**Descripción**: Crear un nuevo usuario  
**Body**:
```json
{
  "name": "María González",
  "email": "maria@example.com",
  "password": "password123"
}
```

#### PUT /api/v2/users/{id}
**Descripción**: Actualizar un usuario existente

#### DELETE /api/v2/users/{id}
**Descripción**: Eliminar un usuario
**Respuesta**: 204 No Content

#### PATCH /api/v2/users/{id}/activate
**Descripción**: Activar un usuario

#### PATCH /api/v2/users/{id}/deactivate
**Descripción**: Desactivar un usuario

---

## 🏠 Campos Específicos para Puntos de Suministro

### Campo "Street" (Calle)
El campo `street` ha sido añadido específicamente para puntos de análisis que representan **domicilios** o **suministros**:

#### 📋 Reglas de Uso:
- **Obligatorio** para tipos: `DOMICILIO`, `SUMINISTRO`
- **Opcional** para tipos: `RESERVORIO`, `RED_DISTRIBUCION`
- **Formato**: Texto libre (máximo 200 caracteres)
- **Ejemplo**: "Calle Los Pinos 123", "Av. Los Rosales 456"

#### 🔍 Ejemplos por Tipo de Punto:

**RESERVORIO** (street opcional):
```json
{
  "pointName": "Reservorio Principal",
  "pointType": "RESERVORIO",
  "street": null,
  "locationDescription": "Entrada del reservorio principal"
}
```

**DOMICILIO** (street obligatorio):
```json
{
  "pointName": "Casa Rodriguez",
  "pointType": "DOMICILIO", 
  "street": "Calle Los Pinos 123",
  "locationDescription": "Vivienda unifamiliar"
}
```

**SUMINISTRO** (street obligatorio):
```json
{
  "pointName": "Suministro Comercial",
  "pointType": "SUMINISTRO",
  "street": "Av. Comercio 890",
  "locationDescription": "Local comercial"
}
```

---

## 🔗 APIs Externas Consumidas

### ❌ Sin Consumo de APIs Externas
Este microservicio **NO consume APIs externas**. Opera independientemente con:

- **Base de Datos**: Conexión directa a MongoDB Atlas
- **Autenticación**: Validación local de tokens OAuth2  
- **Métricas**: Exposición de métricas internas vía Prometheus

### 🏢 Servicios Internos Disponibles
El microservicio contiene un servicio vacío preparado para futuras integraciones:
- **OrganizationService**: Servicio preparado para integración con MS-ORGANIZACIONES (actualmente vacío)

### 📝 Notas de Arquitectura
- **Sin dependencias externas**: El microservicio funciona de forma autónoma
- **Reactive Stack**: Utiliza WebFlux para operaciones no bloqueantes
- **Seguridad**: OAuth2 Resource Server para autenticación JWT
- **Monitoring**: Actuator + Prometheus para observabilidad

### 🏗️ Arquitectura de Integración
```
MS-CALIDAD-AGUA (8087) 
├── MongoDB Atlas (Database)
├── OAuth2 Server (Authentication)  
└── Prometheus (Metrics)
```

### 🔄 Posibles Integraciones Futuras
El microservicio está diseñado para integrarse con:
- **MS-ORGANIZACIONES**: Para validar organizaciones
- **MS-USUARIOS-AUTENTICACION**: Para gestión de usuarios
- **MS-NOTIFICACIONES**: Para alertas de calidad
- **MS-RECLAMOS-INCIDENCIAS**: Para escalamiento de incidencias

---

## 📊 Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| 200 | Operación exitosa |
| 201 | Recurso creado exitosamente |
| 204 | Eliminación exitosa (sin contenido) |
| 400 | Error en datos de entrada |
| 401 | No autorizado |
| 403 | Prohibido (sin permisos) |
| 404 | Recurso no encontrado |
| 500 | Error interno del servidor |

## 🛠️ Formato de Respuesta Estándar

### Respuesta Exitosa
```json
{
  "success": true,
  "data": { /* objeto o array de datos */ }
}
```

### Respuesta de Error
```json
{
  "success": false,
  "error": {
    "errorCode": 404,
    "message": "Recurso no encontrado",
    "details": "No se encontró el registro con ID especificado"
  }
}
```

---

## 🔧 Configuración de Desarrollo

### Variables de Entorno
```bash
DATABASE_URL=mongodb+srv://usuario:password@cluster.mongodb.net/waterquality
DATABASE_USERNAME=sistemajass
DATABASE_PASSWORD=ZC7O1Ok40SwkfEje
PORT=8087
```

### Swagger UI
- **URL**: `http://localhost:8087/swagger-ui.html`
- **API Docs**: `http://localhost:8087/v3/api-docs`

### Actuator Endpoints
- **Health**: `http://localhost:8087/actuator/health`
- **Metrics**: `http://localhost:8087/actuator/metrics`
- **Prometheus**: `http://localhost:8087/actuator/prometheus`