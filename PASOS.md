# Paso a paso: instalar y correr Columbia Viajes en otra computadora (Windows)

Esta guía es para dejar el proyecto andando en una compu nueva, desde cero.
La app corre **solo en tu computadora** (se abre en `http://localhost:8080`).

---

## 1. Qué hay que instalar

| Programa | ¿Para qué? | Dónde bajarlo |
|---|---|---|
| **Git** | Para bajar (clonar) el proyecto de GitHub | https://git-scm.com/download/win |
| **JDK 21** (Java) | Para compilar y correr la app | https://adoptium.net/temurin/releases/?version=21 (elegí Windows, x64, **JDK**, archivo `.msi`) |
| **VS Code** (opcional) | Para ver y editar el código | https://code.visualstudio.com |
| **Extension Pack for Java** (opcional) | Extensión de VS Code para trabajar con Java | Dentro de VS Code: Extensiones (Ctrl+Shift+X) → buscar "Extension Pack for Java" |

**No hace falta instalar:**

- **Maven**: el proyecto trae `mvnw.cmd`, que lo baja solo la primera vez.
- **MySQL, XAMPP ni phpMyAdmin**: la app usa **H2**, una base de datos que viene adentro del proyecto y se crea sola.

### Al instalar el JDK

En el instalador `.msi` de Temurin, cuando aparezcan las opciones, activá:

- **Add to PATH**
- **Set JAVA_HOME variable**

### Verificar que quedó bien

Abrí una terminal nueva (tecla Windows → escribí `PowerShell` → Enter) y escribí:

```
java -version
git --version
```

Tiene que aparecer algo como `openjdk version "21..."` y `git version 2...`.
Si dice que no reconoce el comando, cerrá la terminal, abrila de nuevo, y si sigue igual reiniciá la compu.

---

## 2. Bajar el proyecto (clonar)

En PowerShell, andá a la carpeta donde lo quieras guardar (por ejemplo Documentos) y clonalo:

```
cd $HOME\Documents
git clone https://github.com/bran-matt/final-programaci-n-2.git
cd final-programaci-n-2
```

> Si todavía no está todo unido en la rama principal (`main`), bajá la rama con la app completa:
> `git checkout terminar-final`

---

## 3. Correr la app

1. Entrá a la carpeta de la app:

   ```
   cd columbia-springboot
   ```

2. Arrancala:

   ```
   .\mvnw.cmd spring-boot:run
   ```

   (Si usás la terminal vieja "Símbolo del sistema" / cmd, es sin el `.\`: `mvnw.cmd spring-boot:run`)

3. **La primera vez tarda varios minutos**, porque baja Maven y todas las librerías. Las siguientes veces es mucho más rápido.

4. Esperá hasta ver una línea parecida a esta:

   ```
   Started ColumbiaSpringbootApplication in 6.3 seconds
   ```

5. Abrí el navegador y entrá a **http://localhost:8080**

6. **Para apagarla:** volvé a la terminal y apretá **Ctrl + C** (si pregunta "¿Desea terminar el trabajo por lotes (S/N)?", escribí `S` y Enter).

### Desde VS Code (alternativa)

1. Archivo → Abrir carpeta → elegí `columbia-springboot`.
2. Esperá a que la extensión de Java termine de cargar el proyecto (abajo a la derecha).
3. Abrí `src/main/java/com/columbiaviajesweb/columbia_springboot/ColumbiaSpringbootApplication.java` y tocá **Run** arriba del `main`.

---

## 4. Usuarios de prueba

La primera vez que arranca, la app carga datos de ejemplo. **Todos tienen la clave `1234`.**

| Usuario | Rol | Qué puede hacer |
|---|---|---|
| `cliente1` … `cliente4` | Cliente | Consultar sus datos, sus reservas, vuelos, hoteles y sucursales |
| `vendedor1` … `vendedor3` | Vendedor | Administrar clientes y reservas |
| `admin` | Administrador | Administrar todo |
| `duenio` | Dueño | Ver el listado de vendedores ordenado según cuánto facturaron |

---

## 5. La base de datos

- La base es **H2** y se guarda en un archivo: `columbia-springboot\data\columbia.mv.db`.
- Se crea sola la primera vez que corrés la app, con las tablas y los datos de ejemplo.
- **Para empezar de cero:** apagá la app (Ctrl + C) y borrá la carpeta `columbia-springboot\data`. La próxima vez que arranque se vuelve a crear.
- Esa carpeta no se sube a GitHub, así que cada computadora tiene su propia base.

### Ver la base desde el navegador (consola de H2)

No hace falta phpMyAdmin (ese es solo para MySQL). La app trae la **consola de H2**:

1. Con la app corriendo, entrá a **http://localhost:8080/h2-console**
2. En la pantalla de ingreso completá así:

   | Campo | Qué poner |
   |---|---|
   | Driver Class | `org.h2.Driver` (ya viene puesto) |
   | JDBC URL | `jdbc:h2:file:./data/columbia;AUTO_SERVER=TRUE` |
   | User Name | `sa` |
   | Password | *(dejalo vacío)* |

3. Tocá **Connect**. A la izquierda vas a ver las tablas (`USUARIOS`, `VUELOS`, `HOTELES`, `RESERVAS`, etc.).
4. Hacé clic en una tabla (o escribí por ejemplo `SELECT * FROM VUELOS`) y tocá **Run** para ver los datos.

> La consola solo se puede abrir desde la misma computadora donde corre la app.
> Si en JDBC URL aparece otra cosa (por ejemplo `jdbc:h2:~/test`), borrala y poné la de arriba; si no, abre una base vacía.

---

## 6. Problemas comunes

**"java no se reconoce como un comando" o "JAVA_HOME not found / is set to an invalid directory"**
El JDK no quedó en el PATH o falta JAVA_HOME. Reinstalá el JDK marcando *Add to PATH* y *Set JAVA_HOME variable*, después cerrá y abrí la terminal.
Para revisarlo: `echo $env:JAVA_HOME` tiene que mostrar una carpeta como `C:\Program Files\Eclipse Adoptium\jdk-21...`.

**"Port 8080 was already in use"**
Ya hay algo usando el puerto 8080, casi siempre otra copia de la app que quedó abierta. Cerrá esa otra terminal (o apretá Ctrl + C en ella) y probá de nuevo.
Si no la encontrás, podés arrancarla en otro puerto:

```
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

y entrar a http://localhost:8081

**"release version 21 not supported" o error parecido al compilar**
Tenés instalada una versión vieja de Java. Fijate con `java -version` que diga 21; si dice 8, 11 o 17, instalá el JDK 21 y revisá JAVA_HOME (punto anterior).

**La primera vez se queda mucho rato descargando**
Es normal: baja Maven y las librerías. Necesita internet solo esa primera vez.

**PowerShell dice que no puede ejecutar `mvnw.cmd`**
Asegurate de estar dentro de la carpeta `columbia-springboot` (con `dir` tenés que ver `mvnw.cmd` y `pom.xml`) y de escribirlo con `.\` adelante.

**Quiero los datos de ejemplo originales de vuelta**
Borrá la carpeta `columbia-springboot\data` con la app apagada y volvé a arrancarla.

---

## 7. Resumen rápido (cuando ya está todo instalado)

```
cd $HOME\Documents\final-programaci-n-2\columbia-springboot
.\mvnw.cmd spring-boot:run
```

Abrir http://localhost:8080 · entrar con `admin` / `1234` · apagar con Ctrl + C.

---

# CMD

Para una compu donde **no se puede instalar nada** (por ejemplo la del instituto, donde winget y Git están bloqueados). Java y el proyecto se bajan como zip, así que no hace falta ser administrador.

Abrí **cmd** (tecla Windows → escribí `cmd` → Enter) y pegá **de a una línea**, esperando a que termine cada una.

### 1. Ir a Documentos

```
cd %USERPROFILE%\Documents
```

### 2. Bajar Java 21 y descomprimirlo

```
curl -L -o jdk.zip https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse
```

```
tar -xf jdk.zip
```

### 3. Decirle a esta ventana dónde está Java

```
for /d %i in (jdk-21*) do set "JAVA_HOME=%CD%\%i"
```

```
set "PATH=%JAVA_HOME%\bin;%PATH%"
```

```
java -version
```

Tiene que decir 21.

### 4. Bajar el proyecto (sin Git)

```
curl -L -o proyecto.zip https://github.com/bran-matt/final-programaci-n-2/archive/refs/heads/main.zip
```

```
tar -xf proyecto.zip
```

### 5. Correrlo

```
cd final-programaci-n-2-main\columbia-springboot
```

```
mvnw.cmd spring-boot:run
```

Cuando aparezca `Started ColumbiaSpringbootApplication`, entrá a http://localhost:8080 con `admin` / `1234`.

> **Si cerrás el cmd**, Java deja de estar configurado. La próxima vez repetí los pasos **1** y **3**, y después el **5**.
