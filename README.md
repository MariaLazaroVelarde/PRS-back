# MS-Calidad-Agua-back

## 📋 Descripción
Microservicio de gestión de calidad del agua desarrollado con Spring Boot WebFlux para el sistema JASS (Juntas Administradoras de Servicios de Saneamiento). Proporciona APIs RESTful para el manejo reactivo de registros de calidad, pruebas, parámetros, puntos de muestreo e incidencias.

## 🚀 Tecnologías
- **Framework**: Spring Boot 3.4.5 con WebFlux (Reactive)
- **Base de Datos**: MongoDB (Reactive)
- **Seguridad**: OAuth2 Resource Server
- **Documentación**: OpenAPI 3 (Swagger)
- **Monitoreo**: Prometheus + Micrometer
- **Java**: 17
- **Build**: Maven

## 📡 Endpoints Disponibles

### 🔄 Daily Records (Registros Diarios)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v2/dailyrecords` | Obtener todos los registros diarios |
| `GET` | `/api/v2/dailyrecords/{id}` | Obtener registro por ID |
| `POST` | `/api/v2/dailyrecords` | Crear nuevo registro diario |
| `PUT` | `/api/v2/dailyrecords/{id}` | Actualizar registro diario |
| `DELETE` | `/api/v2/dailyrecords/{id}` | Eliminación lógica del registro |
| `DELETE` | `/api/v2/dailyrecords/{id}/physical` | Eliminación física del registro |
| `PUT` | `/api/v2/dailyrecords/{id}/restore` | Restaurar registro eliminado |

### 🧪 Quality Tests (Pruebas de Calidad)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v2/qualitytests` | Obtener todas las pruebas de calidad |
| `GET` | `/api/v2/qualitytests/{id}` | Obtener prueba por ID |
| `POST` | `/api/v2/qualitytests` | Crear nueva prueba de calidad |
| `PUT` | `/api/v2/qualitytests/{id}` | Actualizar prueba de calidad |
| `DELETE` | `/api/v2/qualitytests/{id}` | Eliminación lógica de la prueba |
| `DELETE` | `/api/v2/qualitytests/{id}/physical` | Eliminación física de la prueba |
| `PUT` | `/api/v2/qualitytests/{id}/restore` | Restaurar prueba eliminada |

### ⚗️ Quality Parameters (Parámetros de Calidad)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v2/qualityparameters` | Obtener todos los parámetros |
| `GET` | `/api/v2/qualityparameters/active` | Obtener parámetros activos |
| `GET` | `/api/v2/qualityparameters/inactive` | Obtener parámetros inactivos |
| `GET` | `/api/v2/qualityparameters/{id}` | Obtener parámetro por ID |
| `POST` | `/api/v2/qualityparameters` | Crear nuevo parámetro |
| `PUT` | `/api/v2/qualityparameters/{id}` | Actualizar parámetro |
| `DELETE` | `/api/v2/qualityparameters/{id}` | Eliminar parámetro |
| `PATCH` | `/api/v2/qualityparameters/{id}/activate` | Activar parámetro |
| `PATCH` | `/api/v2/qualityparameters/{id}/deactivate` | Desactivar parámetro |

### 📍 Testing Points (Puntos de Muestreo)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v2/testingpoints` | Obtener todos los puntos de muestreo |
| `GET` | `/api/v2/testingpoints/active` | Obtener puntos activos |
| `GET` | `/api/v2/testingpoints/inactive` | Obtener puntos inactivos |
| `GET` | `/api/v2/testingpoints/{id}` | Obtener punto por ID |
| `POST` | `/api/v2/testingpoints` | Crear nuevo punto de muestreo |
| `PUT` | `/api/v2/testingpoints/{id}` | Actualizar punto de muestreo |
| `DELETE` | `/api/v2/testingpoints/{id}` | Eliminar punto de muestreo |
| `PATCH` | `/api/v2/testingpoints/{id}/activate` | Activar punto |
| `PATCH` | `/api/v2/testingpoints/{id}/deactivate` | Desactivar punto |

### 🚨 Quality Incidents (Incidencias de Calidad)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v2/qualityincidents` | Obtener todas las incidencias |
| `GET` | `/api/v2/qualityincidents/resolved` | Obtener incidencias resueltas |
| `GET` | `/api/v2/qualityincidents/unresolved` | Obtener incidencias no resueltas |
| `GET` | `/api/v2/qualityincidents/{id}` | Obtener incidencia por ID |
| `POST` | `/api/v2/qualityincidents` | Crear nueva incidencia |
| `PUT` | `/api/v2/qualityincidents/{id}` | Actualizar incidencia |
| `DELETE` | `/api/v2/qualityincidents/{id}` | Eliminar incidencia |

### 👥 Users (Usuarios)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v2/users` | Obtener todos los usuarios |
| `GET` | `/api/v2/users/active` | Obtener usuarios activos |
| `GET` | `/api/v2/users/inactive` | Obtener usuarios inactivos |
| `GET` | `/api/v2/users/{id}` | Obtener usuario por ID |
| `POST` | `/api/v2/users` | Crear nuevo usuario |
| `PUT` | `/api/v2/users/{id}` | Actualizar usuario |
| `DELETE` | `/api/v2/users/{id}` | Eliminar usuario |
| `PATCH` | `/api/v2/users/{id}/activate` | Activar usuario |
| `PATCH` | `/api/v2/users/{id}/deactivate` | Desactivar usuario |

## 🔗 APIs Externas Consumidas
Este microservicio actualmente **NO consume APIs externas**. Opera de forma independiente con:
- **Base de Datos**: MongoDB (conexión directa)
- **Autenticación**: OAuth2 (validación local de tokens)
- **Métricas**: Prometheus (exposición de métricas internas)

## 📖 Documentación de la API
- **Swagger UI**: `http://localhost:8085/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8085/v3/api-docs`

## 🏥 Monitoreo y Salud
- **Health Check**: `GET /actuator/health`
- **Métricas**: `GET /actuator/metrics`
- **Prometheus**: `GET /actuator/prometheus`

## 🔧 Configuración
- **Puerto**: 8085
- **Base URL**: `/api/v2`
- **Base de Datos**: MongoDB Atlas
- **CORS**: Habilitado para todos los orígenes (*)

## 🚀 Cómo Ejecutar
```bash
# Compilar
mvn clean package -DskipTests

# Ejecutar
java -jar target/vg-ms-water-quality-*.jar

# Docker
docker build -t vg-ms-water-quality .
docker run -p 8085:8085 vg-ms-water-quality
```
