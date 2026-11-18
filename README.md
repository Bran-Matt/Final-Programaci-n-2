# Final Programación II · Columbia Viajes

Aplicación web para gestionar una cadena de agencias de viajes: sucursales, hoteles, vuelos, turistas y sus reservas.

## Enunciado

### [Columbia Viajes](www.columbiaviajes.com.ar)

Desarrolle un sistema para gestionar la información de una cadena de agencias de viajes, relativa al
hospedaje y vuelos de los turistas que la contratan, teniendo en cuenta que:
- La cadena de agencias está compuesta por un conjunto de sucursales. Cada sucursal viene definida
por el código de sucursal, dirección, e-mail y teléfono.
- La cadena tiene contratados una serie de hoteles de forma exclusiva. Cada hotel estará definido por
el código de hotel, nombre, dirección, ciudad, teléfono y número de plazas disponibles.
- De igual forma, la cadena tiene contratados una serie de vuelos regulares de forma exclusiva. Cada
vuelo viene definido por el número de vuelo, fecha y hora, origen y destino, plazas totales y plazas
de clase turista de las que dispone.
- La información que se desea almacenar por cada turista es el código de turista, nombre y apellidos,
dirección, e-mail y teléfono.

Por otra parte, hay que tener en cuenta la siguiente información:
- A la cadena de agencias le interesa conocer qué sucursal ha contratado el turista.
- A la hora de viajar el turista puede elegir cualquiera de los vuelos que ofrece la cadena, y en que
clase (turista o primera) desea viajar.
- De igual manera, el turista se puede hospedar en cualquiera de los hoteles que ofrece la cadena, y
elegir el régimen de hospedaje (media pensión o pensión completa). Siendo significativa la fecha de
llegada y de partida.
- Al sistema podrán acceder cuatro tipos de usuarios: clientes (que sólo lo podrán consultar),
vendedores (que administrarán las reservas y a los clientes), administradores (que administrarán
todo) y dueño (que podrá consultar un listado de los vendedores, ordenado según cuánto facturaron).

Para ello:
- Analice los requerimientos anteriores
- Determine los objetos requeridos para implementar ese sistema
- Establezca los atributos que deben tener estos objetos
- Fije los comportamientos que exhibirán estos objetos
- Especifique la forma en que los objetos deben interactuar entre sí para cumplir con los
requerimientos del sistema
- Usar una base de datos
- Solo podes usar minimo 2 framework
- Que sea una aplicacion web

El sistema deberá utilizar abstracción, encapsulamiento, herencia, polimorfismo y persistencia


---

## Cómo correrlo en tu computadora

Solo necesitás **Java 21** (JDK). No hace falta instalar ninguna base de datos: por defecto la app usa **H2**, que viene adentro y guarda todo en un archivo.

1. Instalá el JDK 21 si no lo tenés (por ejemplo [Eclipse Temurin 21](https://adoptium.net/temurin/releases/?version=21)). Para verificar, en una terminal: `java -version`.
2. Abrí una terminal en la carpeta `columbia-springboot`.
3. Ejecutá:
   - **Windows:** `mvnw.cmd spring-boot:run`
   - **Mac / Linux:** `./mvnw spring-boot:run`

   La primera vez tarda unos minutos porque descarga Maven y las librerías.
4. Entrá a **http://localhost:8080** en el navegador.

También lo podés abrir desde IntelliJ IDEA o VS Code y ejecutar la clase `ColumbiaSpringbootApplication`.

### Usuarios de prueba

La primera vez que arranca, la app carga datos de ejemplo. Todos tienen la clave **1234**:

| Usuario | Rol | Qué puede hacer |
|---|---|---|
| `cliente1` … `cliente4` | Cliente | Consultar sus datos, sus reservas, vuelos, hoteles y sucursales |
| `vendedor1` … `vendedor3` | Vendedor | Administrar clientes y reservas (cada reserva que hace se le factura) |
| `admin` | Administrador | Administrar todo: sucursales, hoteles, vuelos, usuarios, clientes y reservas |
| `duenio` | Dueño | Ver el listado de vendedores ordenado según cuánto facturaron |

Para empezar con la base vacía de nuevo, cerrá la app y borrá la carpeta `columbia-springboot/data`.

### Generar el .jar

`mvnw.cmd package` (o `./mvnw package`) genera `target/columbia-springboot-0.0.1-SNAPSHOT.jar`, que se ejecuta con:

```
java -jar target/columbia-springboot-0.0.1-SNAPSHOT.jar
```

### Usar MySQL en vez de H2 (opcional)

Si tenés MySQL (por ejemplo el de XAMPP, con MySQL iniciado desde el panel de control), en `src/main/resources/application.properties` cambiá `spring.profiles.active=h2` por `spring.profiles.active=mysql`. Los datos de conexión están en `application-mysql.properties` (usuario `root` sin clave, como XAMPP por defecto). La base `columbia_viajes` y las tablas se crean solas.

---

## Tecnologías

- **Spring Boot 4** (Spring MVC): framework web.
- **Spring Data JPA / Hibernate**: persistencia en base de datos.
- **Thymeleaf**: plantillas HTML del lado del servidor.
- **Bootstrap 5** + Bootstrap Icons: diseño de las pantallas.
- **blobatar** (licencia MIT): avatares de los usuarios, generados a partir de su código. Está copiado en `static/js/blobatar/` junto con su archivo `LICENSE`.
- **H2** (por defecto) o **MySQL**.

## Diseño orientado a objetos

| Concepto | Dónde se ve |
|---|---|
| **Abstracción** | `Usuario` y `Reserva` son clases abstractas; `ServicioBase<T>` y `CrudController<T>` son base genérica de servicios y controladores. |
| **Encapsulamiento** | Atributos privados con getters/setters; las reglas (plazas libres, fechas, códigos únicos) viven en los servicios. Las claves se guardan con hash BCrypt. |
| **Herencia** | `Cliente`, `Vendedor`, `Administrador` y `Duenio` heredan de `Usuario`. `ReservaVuelo` y `ReservaHotel` heredan de `Reserva`. Los servicios heredan de `ServicioBase`, y los controladores de sucursales, hoteles y vuelos de `CrudController`. |
| **Polimorfismo** | `Reserva.calcularImporte()` lo implementa cada tipo (precio por clase en vuelos; noches × precio × régimen en hoteles). Cada usuario define su `getRol()`, `getPaginaInicio()` y permisos (`puedeGestionarReservas()`, `puedeAdministrar()`…), que usan el login, el control de acceso y el menú. `Rol.crearUsuario()` crea la subclase correcta. |
| **Persistencia** | Entidades JPA guardadas en la base: herencia `JOINED` para usuarios (una tabla por rol) y `SINGLE_TABLE` para reservas. |

### Modelo

- **Sucursal**: código, dirección, e-mail, teléfono.
- **Hotel**: código, nombre, dirección, ciudad, teléfono, plazas disponibles, precio por noche.
- **Vuelo**: número, fecha y hora, origen, destino, plazas totales, plazas de clase turista (las de primera son la diferencia) y precio por clase.
- **Cliente** (turista): código de turista, nombre, apellidos, dirección, e-mail, teléfono y la **sucursal** que contrató.
- **ReservaVuelo**: cliente, vuelo y **clase** (turista o primera). No deja vender más pasajes que plazas.
- **ReservaHotel**: cliente, hotel, **régimen** (media pensión o pensión completa) y **fechas de llegada y partida**. No deja superar las plazas del hotel en fechas superpuestas.
- Cada reserva guarda el **vendedor** que la hizo; la facturación del vendedor es la suma de sus reservas.

### Estructura del código

```
columbia-springboot/src/main/java/.../columbia_springboot/
├── entity/       Clases del modelo (Usuario y subclases, Reserva y subclases, Hotel, Vuelo, Sucursal, enums)
├── repository/   Acceso a datos (Spring Data JPA)
├── service/      Reglas de negocio (ServicioBase + un servicio por entidad)
├── controller/   Pantallas web por sección
├── dto/          Datos de formularios y del ranking
└── config/       Control de sesión y permisos, datos iniciales
```

Los tests (`./mvnw test`) prueban el login, los permisos por rol, el cálculo de importes, el orden del ranking y que no se pueda sobrevender un vuelo.

---

## Paso a Paso

Guía completa para instalar y correr el proyecto en otra computadora (Windows): **[PASOS.md](PASOS.md)**.

### Qué instalar

- **Git**: https://git-scm.com/download/win
- **JDK 21**: https://adoptium.net/temurin/releases/?version=21 (al instalar, marcá *Add to PATH* y *Set JAVA_HOME variable*)
- **VS Code** + **Extension Pack for Java** (opcional, para ver el código)

No hace falta instalar Maven (viene `mvnw.cmd`) ni MySQL/XAMPP (la base H2 viene adentro y se crea sola).

### Cómo instalarlo y correrlo

```
git clone https://github.com/bran-matt/final-programaci-n-2.git
cd final-programaci-n-2\columbia-springboot
.\mvnw.cmd spring-boot:run
```

1. La primera vez tarda unos minutos (descarga Maven y las librerías).
2. Esperá la línea `Started ColumbiaSpringbootApplication`.
3. Abrí **http://localhost:8080** y entrá con un usuario de prueba (`cliente1`, `vendedor1`, `admin` o `duenio`, clave `1234`).
4. Para apagarla: **Ctrl + C** en la terminal.

### Si algo falla

- **"java no se reconoce" / error de JAVA_HOME:** reinstalá el JDK 21 marcando *Add to PATH* y *Set JAVA_HOME*, y abrí una terminal nueva.
- **"Port 8080 was already in use":** ya hay otra copia de la app abierta; cerrala con Ctrl + C y probá de nuevo.
- **Empezar con la base limpia:** con la app apagada, borrá la carpeta `columbia-springboot\data`.

Más detalles y otros problemas en [PASOS.md](PASOS.md).
