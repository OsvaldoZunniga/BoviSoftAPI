# Welcome, this is BoviSoftAPIREST

Here u gonna found all the info about my cattle in Finca La Piolina. Thats the way that Im gonna manage my cattle, including sells and injections.

### About API

- CattleController.updateCattle no actualiza los siguientes campos(id, statusId, parentsId) esto para mantener integridad en el sistema. statusId se puede actualizar desde el endpoint `patch("/cattle/{id}/destroy")`

### EndPoints

#### Welcome message

-get `/` This endpoint is the API root and is just a welcome message, that return a simple string.

#### Cattles

- getAll `/cattle/`
- getById `/cattle/{id}`
- post `...`

### MongoValidation

```{
  $jsonSchema: {
    title: 'Cattle Validation Schema',
    bsonType: 'object',
    required: [
      'earTag',
      'name',
      'breed',
      'birthday',
      'weight',
      'motherId',
      'fatherId'
    ],
    properties: {
      earTag: {
        bsonType: 'string',
        description: 'Número de arete único del bovino.'
      },
      name: {
        bsonType: 'string',
        description: 'Nombre del animal.'
      },
      breed: {
        bsonType: 'string',
        description: 'Raza del bovino'
      },
      birthday: {
        bsonType: 'string',
        description: 'Fecha de nacimiento.'
      },
      weight: {
        bsonType: [
          'double',
          'int'
        ],
        description: 'Peso en kilogramos.'
      },
      imagePath: {
        bsonType: [
          'string',
          'null'
        ],
        description: 'Ruta de la imagen en Cloud Storage. Puede ser un string o nulo.'
      },
      motherId: {
        bsonType: 'string',
        description: 'Referencia al _id de la madre para control genético.'
      },
      fatherId: {
        bsonType: 'string',
        description: 'Referencia al _id del padre para control genético.'
      },
      statusId: {
        bsonType: 'int'
      }
    },
    additionalProperties: false
  }
}
```

## todo

- add default statusID value as 1 in cattle records.
- Improve the schema implementing another JSON format, like:

```
{
  "id": "hash",
  properties: {
    ...
  },
  "id": "hash",
  properties: {
    ...
  }
}
```
