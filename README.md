# T2 – Soluciones Web y Aplicaciones Distribuidas (NRC 16046)

| Caso | Clase | Integrante |
|---|---|---|
| Caso 1 – Docente | `Teacher` | ALTAMIRANO SANCHEZ JEAN PIERRE |
| Caso 14 – Torneo | `Tournament` | PEÑA PAJARES JHON PAOLO |

Spring Boot 4.1 · Java 17 · Spring Data JPA · PostgreSQL · Bean Validation · ModelMapper · Lombok · springdoc (Swagger)

## Cómo ejecutar

1. PostgreSQL en `localhost:5432` (usuario `postgres`, clave `root`) y crear la base:
   `CREATE DATABASE altamirano_pajares;`
2. Ejecutar `AltamiranoPajaresApplication` desde IntelliJ, o `./mvnw spring-boot:run`.
   Hibernate crea las tablas `teachers` y `tournaments` (`ddl-auto=update`).
3. Probar:
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Postman: importar `postman/Altamirano_Pajares.postman_collection.json` y ejecutar la colección (23 pruebas: 200, 201, 204, 400 y 404).

## Estructura de paquetes (`com.altamirano_pajares`)

```
com.altamirano_pajares
├── model          Teacher, Tournament, AcademicDegree (enum)        → entidades JPA
├── repository     IGenericRepository<T,ID> → ITeacherRepository, ITournamentRepository
├── service        IGenericService<T,ID> → ITeacherService, ITournamentService
│   └── implementation  GenericService<T,ID> (abstracta) → TeacherService, TournamentService
├── dto            TeacherDTO, TournamentDTO                          → lo que entra/sale por la API
├── controller     TeacherController (/v1/teachers), TournamentController (/v1/tournaments)
├── exception      ModelNotFoundException (404), BusinessRuleException (400),
│                  GlobalErrorHandler (@RestControllerAdvice), CustomErrorResponse
└── config         MapperConfig (ModelMapper), OpenApiConfig (Swagger)
```

## Atributos y dónde se valida cada regla

**Teacher**

| Atributo | Tipo | Regla | DTO (Bean Validation) | Servicio | BD |
|---|---|---|---|---|---|
| teacherCode | String | Obligatorio, único | `@NotBlank` `@Size(20)` | `existsByTeacherCode…` | `nullable=false, unique=true` |
| firstName | String | Obligatorio | `@NotBlank` | – | `nullable=false` |
| lastName | String | Obligatorio | `@NotBlank` | – | `nullable=false` |
| email | String | Formato válido, único | `@Email` | `existsByEmail…` | `unique=true` + `@Check` |
| academicDegree | enum | BACHILLER, MAGISTER, DOCTOR | `@NotNull` | – | `@Enumerated(STRING)` + check |

**Tournament**

| Atributo | Tipo | Regla | DTO | Servicio | BD |
|---|---|---|---|---|---|
| name | String | Obligatorio, único | `@NotBlank` | `existsByName…` | `unique=true` |
| sport | String | Obligatorio | `@NotBlank` | – | `nullable=false` |
| startDate | LocalDate | Obligatorio | `@NotNull` | – | `nullable=false` |
| endDate | LocalDate | Posterior a startDate | `@NotNull` | `validateDates()` | `@Check(end_date > start_date)` |
| maxTeams | Integer | Entre 2 y 64 | `@Min(2)` `@Max(64)` | – | `@Check(max_teams BETWEEN 2 AND 64)` |

Hay tres niveles de validación:

1. El **DTO** rechaza datos mal formados.
2. El **servicio** aplica las reglas de negocio que necesitan consultar la BD (unicidad) o comparar campos (fechas).
3. La **BD** es la última barrera si algo se escapa, por ejemplo dos peticiones simultáneas.

## Flujo de una petición (ej. `POST /v1/teachers`)

```
Cliente (Postman/Swagger)
  │ JSON
  ▼
TeacherController  @Valid TeacherDTO ──✗──► GlobalErrorHandler → 400
  │ modelMapper.map(dto, Teacher.class)
  ▼
TeacherService.save()  ¿teacherCode/email ya existen? ──✗──► BusinessRuleException → 400
  │
  ▼
ITeacherRepository.save()  (Spring Data JPA → Hibernate → INSERT en PostgreSQL)
  │ entidad con id generado (IDENTITY)
  ▼
TeacherController → modelMapper.map(entity, TeacherDTO.class) → 201 Created + Location: /v1/teachers/{id}
```

## Endpoints y códigos HTTP

| Método | Ruta | Éxito | Errores |
|---|---|---|---|
| GET | `/v1/teachers` | 200 | – |
| GET | `/v1/teachers/{id}` | 200 | 404 no existe, 400 id no numérico |
| POST | `/v1/teachers` | 201 + header `Location` | 400 validación / duplicado / enum inválido |
| PUT | `/v1/teachers/{id}` | 200 | 400, 404 |
| DELETE | `/v1/teachers/{id}` | 204 | 404 |

`/v1/tournaments` funciona igual. Además devuelve 400 cuando `endDate <= startDate`.

## Principios y patrones aplicados

- **Arquitectura en capas (N-capas):** controller → service → repository → BD. Cada capa solo conoce a la de abajo y depende de **interfaces**, no de implementaciones.
- **DTO (Data Transfer Object):** la API nunca expone la entidad JPA.
  - Desacopla el contrato REST del modelo de BD.
  - Concentra las validaciones de entrada.
  - Con `@JsonProperty(READ_ONLY)`, el cliente no puede enviar el `id`.
  - **ModelMapper** convierte DTO ↔ entidad. Usa la estrategia `STRICT` para que no confunda `startDate` con `endDate`.
- **Repository (Spring Data JPA):** `IGenericRepository<T,ID> extends JpaRepository` lleva `@NoRepositoryBean`, así que Spring no la instancia; solo instancia a sus hijas. Los *query methods* (`existsByEmailIgnoreCaseAndIdTeacherNot`) generan el SQL a partir del nombre del método.
- **Servicio genérico + Template Method:**
  - `GenericService<T,ID>` implementa el CRUD una sola vez. Las clases hijas solo implementan `getRepo()` y sobrescriben `save`/`update` para agregar sus reglas, llamando luego a `super`.
  - `update` asigna el id por reflexión (`setIdTeacher`, `setIdTournament`). Así siempre se hace un UPDATE y no un INSERT.
- **Inyección de dependencias por constructor:** `@RequiredArgsConstructor` (Lombok) más campos `final`. Se aplica la D de **SOLID**: el controlador depende de `ITeacherService`, no de `TeacherService`.
- **SOLID:**
  - **S:** cada clase tiene una responsabilidad.
  - **O:** se agrega una entidad nueva sin modificar `GenericService`.
  - **L:** `TeacherService` puede usarse donde se espera un `IGenericService`.
  - **I:** las interfaces son pequeñas y específicas.
  - **D:** se depende de abstracciones.
- **Manejo centralizado de errores:** `@RestControllerAdvice` transforma excepciones en respuestas JSON uniformes (`CustomErrorResponse`) con el código HTTP correcto. Por eso los controladores no tienen try/catch.
- **REST:** recursos en plural, verbos HTTP con su semántica, versión en la URL (`/v1`), `201 + Location` al crear y `204` sin cuerpo al eliminar.

## Anotaciones principales

| Anotación | Para qué |
|---|---|
| `@Entity`, `@Table` | Mapea la clase a una tabla |
| `@Id`, `@GeneratedValue(IDENTITY)` | PK autoincremental de la BD |
| `@Column(nullable, unique, length)` | Restricciones a nivel de columna |
| `@Enumerated(EnumType.STRING)` | Guarda el nombre del enum, no su posición |
| `@Check` (Hibernate) | Restricción CHECK en la tabla |
| `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@EqualsAndHashCode(onlyExplicitlyIncluded)` | Lombok: getters/setters/constructores; igualdad solo por id (recomendado en entidades JPA) |
| `@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min`, `@Max` | Bean Validation en el DTO |
| `@Valid` | Dispara la validación del DTO en el controlador |
| `@RestController`, `@RequestMapping`, `@GetMapping`… `@PathVariable`, `@RequestBody` | Capa web REST |
| `@Service`, `@Configuration`, `@Bean` | Componentes y configuración de Spring |
| `@NoRepositoryBean` | Repositorio base que Spring no instancia |
| `@RestControllerAdvice`, `@ExceptionHandler` | Manejo global de errores |
