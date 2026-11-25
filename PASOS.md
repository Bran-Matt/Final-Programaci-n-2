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

---

# Presentación en varias compus (base, backend y frontend separados)

Para mostrar cada parte en una compu distinta. Todas tienen que estar **en la misma red** (por ejemplo el wifi o la red del cole).

| Compu | Qué muestra | Qué corre |
|---|---|---|
| **BDD** | Base de datos | XAMPP con MySQL (puerto **3306**) y phpMyAdmin |
| **BACK** | Backend | La app de Spring Boot (puerto **8080**) |
| **FRONT** (o la misma BACK) | Frontend | Solo el navegador |

> Normalmente la app corre solo en tu compu (`localhost`). Con el comando de abajo queda abierta a la red **solo mientras la tenés corriendo**; cuando la cerrás, vuelve a ser como siempre.

## Compu BDD: la base de datos (XAMPP)

1. Abrí el **XAMPP Control Panel** y tocá **Start** en **Apache** y en **MySQL**.
2. Entrá a http://localhost/phpmyadmin. Tocá **Servidor: 127.0.0.1** arriba (para estar en el general y no dentro de una base), abrí la pestaña **SQL**, pegá esto y tocá **Continuar** (abajo a la derecha):

   ```
   CREATE DATABASE IF NOT EXISTS columbia_final;
   CREATE USER IF NOT EXISTS 'columbia'@'%' IDENTIFIED BY '1234';
   GRANT ALL ON columbia_final.* TO 'columbia'@'%';
   FLUSH PRIVILEGES;
   ```

   Se puede correr las veces que haga falta: si la base o el usuario ya existen, no pasa nada.

   - **Si salió bien** (cartel verde), fijate que en la lista de la izquierda aparezca **columbia_final** (si no, apretá F5) y seguí con el paso 3.
   - Usamos `columbia_final` y no `columbia_viajes` porque en las compus del cole puede haber una `columbia_viajes` vieja, de otra versión del proyecto, con tablas que chocan con la app nueva.
   - **Si aparece el error #1034** (una tabla interna de XAMPP está rota): borrá todo del cuadro, pegá esto y tocá **Continuar**:

     ```
     REPAIR TABLE mysql.db;
     FLUSH PRIVILEGES;
     ```

     Después volvé a pegar **el código de arriba completo**, tocá **Continuar** y, cuando salga bien, seguí con el paso 3.

3. Abrí **cmd**, escribí `ipconfig` y anotá la **Dirección IPv4** (por ejemplo `192.168.1.20`). Esa es la **IP_BDD**.
   Ojo: tiene que salir del `ipconfig` **de la compu BDD**, no de la del back. Si hay varios adaptadores, usá el que dice **Ethernet** (o Wi-Fi si estás por wifi).
4. Dejá XAMPP abierto durante toda la presentación.

Las tablas no hace falta crearlas: la app las crea sola la primera vez que se conecta, con los datos de ejemplo.

## Compu BACK: el backend (la app)

Abrí **cmd** y pegá **de a una línea** (si en esta compu todavía no bajaste Java y el proyecto, hacé antes los pasos de la sección **CMD**):

```
cd %USERPROFILE%\Documents
```

```
for /d %i in (jdk-21*) do set "JAVA_HOME=%CD%\%i"
```

```
set "PATH=%JAVA_HOME%\bin;%PATH%"
```

```
cd final-programaci-n-2-main\columbia-springboot
```

Antes de correr la app, fijate que Java esté listo con `java -version` (tiene que decir 21). Si dice que no reconoce el comando, hacé los pasos 2 y 3 de la sección **CMD**.

Y esta, **todo en una sola línea**, cambiando `IP_BDD` por la IP que anotaste en la compu BDD (tiene que quedar algo como `jdbc:mysql://192.168.1.20:3306/...`, sin la palabra IP_BDD):

```
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql "-Dspring-boot.run.arguments=--server.address=0.0.0.0 --spring.datasource.url=jdbc:mysql://IP_BDD:3306/columbia_final --spring.datasource.username=columbia --spring.datasource.password=1234"
```

Conviene **copiarla de acá y pegarla**, no escribirla a mano: un error de tipeo (por ejemplo `datasourse` en vez de `datasource`) hace que la app ignore el usuario y la clave.

Qué hace cada parte:

- `profiles=mysql`: usa MySQL en vez de H2.
- `server.address=0.0.0.0`: deja que otras compus de la red entren a la app.
- `datasource.url=...IP_BDD:3306...`: se conecta a la base de la compu BDD.

Cuando aparezca `Started ColumbiaSpringbootApplication`:

- Si Windows pregunta por el **firewall**, tocá **Permitir**.
- Abrí **otro** cmd, escribí `ipconfig` y anotá la Dirección IPv4 de esta compu. Esa es la **IP_BACK**.

## Compu FRONT (o la misma BACK): el frontend

Abrí el navegador y entrá a:

```
http://IP_BACK:8080
```

(cambiando `IP_BACK` por la IP de la compu BACK). Ingresá con `admin` / `1234` o cualquiera de los usuarios de prueba.

Para mostrar que los datos se guardan en la base: hacé una reserva desde el navegador y después, en la compu BDD, abrí la tabla `reservas` en phpMyAdmin.

## Si algo falla

**"Communications link failure" o "Connection refused" en la compu BACK**
La app no llega a la base. Revisá que MySQL esté en verde en XAMPP, que `IP_BDD` esté bien escrita y que las dos compus estén en la misma red. Si en la compu BDD Windows preguntó por el firewall, tiene que estar en **Permitir**. Si las compus del cole no te dejan cambiar el firewall, lo tiene que habilitar el profe (puerto 3306 en la compu BDD y 8080 en la BACK).

**Cómo saber si la compu BACK llega a la base**
En la compu BACK abrí cmd y escribí (cambiando `IP_BDD` por la IP de verdad):

```
powershell Test-NetConnection IP_BDD -Port 3306
```

- `TcpTestSucceeded : True`: la red y el firewall están bien. Si la app igual falla, revisá el usuario `columbia` y el `GRANT`.
- `TcpTestSucceeded : False`: no llega. Casi siempre es el **firewall de la compu BDD**, que bloquea el puerto 3306. Hay que permitir `mysqld.exe` (o el puerto 3306) en el Firewall de Windows de esa compu; si no tenés permisos de administrador, lo tiene que hacer el profe. También revisá que las dos compus estén en la misma red y que MySQL esté en verde en XAMPP.

**"Unknown database 'columbia_final'"**
La app ya llega a MySQL, pero la base no está creada. Volvé a correr el SQL del punto 2 de la compu BDD (asegurate de tocar **Continuar**) y fijate que `columbia_final` aparezca a la izquierda en phpMyAdmin.

**"Connection refused" aunque la IP parece bien**
- Revisá que la IP sea la de la **compu BDD** (`ipconfig` en esa compu). Si pusiste la de la compu BACK, la app busca MySQL en sí misma y no lo encuentra.
- En la compu BDD, escribí `netstat -an | findstr 3306`. Tiene que aparecer `0.0.0.0:3306 ... LISTENING`. Si solo aparece `127.0.0.1:3306`, en XAMPP tocá **Config** (fila de MySQL) → **my.ini**, buscá `bind-address="127.0.0.1"`, ponele un `#` adelante, guardá y hacé **Stop** y **Start** en MySQL.

**"Access denied for user 'columbia'"**
Falta el usuario o el permiso. Volvé a correr en phpMyAdmin la línea del `GRANT` (y si hace falta, el `REPAIR TABLE` de arriba).

**"El sistema no puede encontrar la ruta especificada" al hacer `cd final-programaci-n-2-main\columbia-springboot`**
En esta compu todavía no está el proyecto (o la carpeta se llama distinto). Escribí `dir` para ver qué carpetas hay; si no está, hacé el paso 4 de la sección **CMD**.

**"java no se reconoce como un comando"**
En esta compu no está Java, o cerraste el cmd. Hacé los pasos 1, 2 y 3 de la sección **CMD** (si `jdk.zip` ya está bajado, alcanza con el 1 y el 3).

**Hice una reserva y no aparece en phpMyAdmin**
- Las reservas se guardan en la tabla **reservas**, no en `vuelos`.
- Si arrancaste la app solo con `mvnw.cmd spring-boot:run` (sin el comando largo), usa la base H2 de adentro del proyecto y no la de XAMPP. Para que guarde en XAMPP tiene que arrancar con el comando largo.

**Desde la compu FRONT no abre `http://IP_BACK:8080`**
Fijate primero que en la misma compu BACK abra `http://localhost:8080`. Si ahí anda pero desde FRONT no, es el firewall de la compu BACK o que no están en la misma red.

**Plan B (si no se pueden conectar las compus entre sí)**
Corré la app en la misma compu que XAMPP (base y backend juntos), con `jdbc:mysql://localhost:3306/columbia_final` en lugar de `IP_BDD`, y mostrá el frontend desde otra compu con el navegador en `http://IP_de_esa_compu:8080`.

---

# ¿localhost o IP? (y la versión todo en una compu)

## Qué significa localhost

**`localhost` siempre quiere decir "esta misma compu"**, la compu donde lo escribís.

- Si en la compu BACK escribís `localhost:3306`, la app busca MySQL **en la compu BACK**, no en la BDD.
- Si en la compu FRONT abrís `http://localhost:8080`, el navegador busca la app **en la compu FRONT**, y como ahí no está corriendo, no abre nada.

Por eso, cuando cada parte está en **una compu distinta**, hay que usar la **IP** de la otra compu (`IP_BDD`, `IP_BACK`). Así funcionan las redes, no es algo de la app.

Regla fácil: **si el programa está en la misma compu, va `localhost`; si está en otra, va la IP.**

| Desde... | Hacia... | Qué se pone |
|---|---|---|
| Compu BACK (app) | Base en la compu BDD | `IP_BDD:3306` |
| Compu FRONT (navegador) | App en la compu BACK | `http://IP_BACK:8080` |
| Compu BACK (navegador) | App en la misma BACK | `http://localhost:8080` |

## Versión todo en una compu (todo con localhost)

Si alcanza con mostrar las tres partes por separado, cada una en su puerto, se puede hacer todo en **una sola compu** usando `localhost` en todos lados. Es más simple: no hace falta sacar IPs ni tocar el firewall.

| Parte | Programa | Dónde se ve |
|---|---|---|
| Base de datos | XAMPP con MySQL (puerto **3306**) | http://localhost/phpmyadmin |
| Backend | La app de Spring Boot (puerto **8080**) | La ventana de cmd donde corre |
| Frontend | El navegador | http://localhost:8080 |

### Pasos

1. **Base:** abrí XAMPP, tocá **Start** en **Apache** y **MySQL**, y en phpMyAdmin corré el SQL del punto 2 de la sección anterior (el de la compu BDD).
2. **Backend:** abrí cmd y pegá de a una línea:

   ```
   cd %USERPROFILE%\Documents
   ```

   ```
   for /d %i in (jdk-21*) do set "JAVA_HOME=%CD%\%i"
   ```

   ```
   set "PATH=%JAVA_HOME%\bin;%PATH%"
   ```

   ```
   cd final-programaci-n-2-main\columbia-springboot
   ```

   ```
   mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql "-Dspring-boot.run.arguments=--spring.datasource.url=jdbc:mysql://localhost:3306/columbia_final --spring.datasource.username=columbia --spring.datasource.password=1234"
   ```

   Esperá a que aparezca `Started ColumbiaSpringbootApplication`.

3. **Frontend:** abrí el navegador en **http://localhost:8080** y entrá con `admin` / `1234`.

Para mostrar que los datos se guardan: hacé una reserva en el navegador y después abrí la tabla `reservas` en phpMyAdmin.
