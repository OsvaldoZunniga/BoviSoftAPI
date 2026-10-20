# BoviSoft API REST

[English](#english) | [Español](#español)

---

## English

### Welcome to BoviSoft API REST

Here you will find all the information about the cattle in Finca La Piolina. This system manages livestock records, including sales and medical treatments.

### Installation and Setup

#### Prerequisites

- Git.
- Java JDK 17 or a compatible version.
- Access to the MongoDB database configured for the project.

The project includes the Maven Wrapper, so installing Maven globally is not required.

#### 1. Clone the repository

```bash
git clone https://github.com/OsvaldoZunniga/BoviSoftAPI.git
cd BoviSoftAPI
```

#### 2. Configure MongoDB

Review `src/main/resources/application.properties` (or copy from `application.properties.example`) and configure your MongoDB connection:

```properties
spring.mongodb.uri=<MONGODB_URI>
spring.mongodb.database=BoviSoft
```

#### 3. Create the uploads folder

The API stores uploaded images in `uploads`. Create it from the backend root if it does not already exist:

```bash
mkdir uploads
```

#### 4. Install dependencies and run

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8082`.

#### 5. Verify backend status

Open `http://localhost:8082/` in your browser or execute:

```bash
curl http://localhost:8082/
```

To stop the server, press `Ctrl+C` in your terminal.

---

### Endpoints

#### Welcome message

- `GET(/)` API Root / Welcome message

#### Cattle

- `GET(/cattle/)` Retrieve all cattle records
- `GET(/cattle/{id})` Retrieve a specific cattle record by ID
- `POST(/cattle/)` Register a new cattle record (accepts JSON or `multipart/form-data` with an image file)
- `PUT(/cattle/{id})` Update cattle record
- `POST(/cattle/{id}/image)` Upload / replace only the image for a cattle (`multipart/form-data`)
- `POST(/cattle/upload-image)` Independent image upload (returns `{ "imageUrl": "/uploads/..." }`)
- `DELETE(/cattle/{id}/destroy)` Logical deletion (changes statusId to 0)

#### FarmEvents

- `GET(/farmEvents/)` Retrieve all farm events
- `GET(/farmEvents/{id})` Retrieve a specific farm event by ID
- `POST(/farmEvents/)` Register a new farm event
- `PUT(/farmEvents/{id})` Update a farm event
- `DELETE(/farmEvents/{id})` Delete a farm event

---

### Static Files (Images)

Uploaded images are stored in the `/uploads` directory on the server and are publicly accessible at:
- `http://localhost:8082/uploads/<generated-name.extension>`

---

### MongoDB Validation Schema

```json
{
  "$jsonSchema": {
    "title": "Cattle Validation Schema",
    "bsonType": "object",
    "required": [
      "earTag",
      "name",
      "breed",
      "birthday",
      "weight",
      "motherId",
      "fatherId"
    ],
    "properties": {
      "earTag": {
        "bsonType": "string",
        "description": "Unique ear tag number of the cattle."
      },
      "name": {
        "bsonType": "string",
        "description": "Animal name."
      },
      "breed": {
        "bsonType": "string",
        "description": "Cattle breed."
      },
      "birthday": {
        "bsonType": "string",
        "description": "Birth date."
      },
      "weight": {
        "bsonType": [
          "double",
          "int"
        ],
        "description": "Weight in kilograms."
      },
      "imagePath": {
        "bsonType": [
          "string",
          "null"
        ],
        "description": "Path of the image in storage. Can be string or null."
      },
      "motherId": {
        "bsonType": "string",
        "description": "Reference to mother's _id for genetic tracking."
      },
      "fatherId": {
        "bsonType": "string",
        "description": "Reference to father's _id for genetic tracking."
      },
      "statusId": {
        "bsonType": "int"
      }
    },
    "additionalProperties": false
  }
}
```

---

## Español

### Bienvenido a BoviSoft API REST

Aquí encontrarás toda la información sobre el ganado en Finca La Piolina. Esta es la forma en la que gestiono mi ganado, incluyendo ventas y tratamientos médicos.

### Instalación y puesta en marcha

#### Requisitos previos

- Git.
- Java JDK 17 o una versión compatible.
- Acceso a la base de datos MongoDB configurada para el proyecto.

El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven de forma global.

#### 1. Clonar el repositorio

```bash
git clone https://github.com/OsvaldoZunniga/BoviSoftAPI.git
cd BoviSoftAPI
```

#### 2. Configurar MongoDB

Revisa el archivo `src/main/resources/application.properties` (o copia desde `application.properties.example`) y configura la conexión a MongoDB:

```properties
spring.mongodb.uri=<MONGODB_URI>
spring.mongodb.database=BoviSoft
```

#### 3. Crear la carpeta de archivos

La API guarda las imágenes en `uploads`. Créala desde la raíz del backend si todavía no existe:

```bash
mkdir uploads
```

#### 4. Instalar dependencias y ejecutar

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

La API quedará disponible en `http://localhost:8082`.

#### 5. Verificar que el backend está activo

Abre `http://localhost:8082/` en el navegador o ejecuta:

```bash
curl http://localhost:8082/
```

Para detener el servidor, presiona `Ctrl+C` en la terminal.

---

### Endpoints

#### Welcome message

- `GET(/)` API Root / Mensaje de bienvenida

#### Bovinos (Cattle)

- `GET(/cattle/)` Obtener todos los registros de bovinos
- `GET(/cattle/{id})` Obtener un bovino específico por ID
- `POST(/cattle/)` Registrar un nuevo bovino (admite JSON o `multipart/form-data` con archivo de imagen)
- `PUT(/cattle/{id})` Actualizar un bovino
- `POST(/cattle/{id}/image)` Subir / reemplazar solo la imagen de un bovino (`multipart/form-data`)
- `POST(/cattle/upload-image)` Subida independiente de imagen (retorna `{ "imageUrl": "/uploads/..." }`)
- `DELETE(/cattle/{id}/destroy)` Eliminación lógica (cambia statusId a 0)

#### Eventos de la Finca (FarmEvents)

- `GET(/farmEvents/)` Obtener todos los eventos de la finca
- `GET(/farmEvents/{id})` Obtener un evento específico por ID
- `POST(/farmEvents/)` Registrar un nuevo evento de finca
- `PUT(/farmEvents/{id})` Actualizar un evento de finca
- `DELETE(/farmEvents/{id})` Eliminar un evento de finca

---

### Archivos Estáticos (Imágenes)

Las imágenes subidas se guardan en la carpeta `/uploads` en el servidor y quedan accesibles públicamente en:
- `http://localhost:8082/uploads/<nombre-generado.extension>`

---

### Validación en MongoDB

```json
{
  "$jsonSchema": {
    "title": "Cattle Validation Schema",
    "bsonType": "object",
    "required": [
      "earTag",
      "name",
      "breed",
      "birthday",
      "weight",
      "motherId",
      "fatherId"
    ],
    "properties": {
      "earTag": {
        "bsonType": "string",
        "description": "Número de arete único del bovino."
      },
      "name": {
        "bsonType": "string",
        "description": "Nombre del animal."
      },
      "breed": {
        "bsonType": "string",
        "description": "Raza del bovino."
      },
      "birthday": {
        "bsonType": "string",
        "description": "Fecha de nacimiento."
      },
      "weight": {
        "bsonType": [
          "double",
          "int"
        ],
        "description": "Peso en kilogramos."
      },
      "imagePath": {
        "bsonType": [
          "string",
          "null"
        ],
        "description": "Ruta de la imagen en almacenamiento. Puede ser un string o nulo."
      },
      "motherId": {
        "bsonType": "string",
        "description": "Referencia al _id de la madre para control genético."
      },
      "fatherId": {
        "bsonType": "string",
        "description": "Referencia al _id del padre para control genético."
      },
      "statusId": {
        "bsonType": "int"
      }
    },
    "additionalProperties": false
  }
}
```
