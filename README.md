# Chat de Conversación Local

Este proyecto es un chat de conversación local que permite a los usuarios comunicarse entre sí dentro de la misma máquina.

## Funcionalidades principales

* **Inicio de sesión:**
    * Los usuarios ingresan un nombre de usuario para unirse al grupo de conversación.
* **Chat en tiempo real:**
    * Los usuarios pueden enviar mensajes a otros usuarios en línea.
    * Cada usuario tiene su propia ventana de chat.
* **Selección de destinatario:**
    * Un menú desplegable permite elegir el usuario destinatario del mensaje.
* **Historial de mensajes:**
    * La ventana de chat muestra el historial de mensajes enviados y recibidos, incluyendo los nombres de usuario de los remitentes y destinatarios.
* **Limpieza de historial:**
    * Cada usuario puede borrar su historial de mensajes.

## Estructura del repositorio

* `src/app`: Contiene el código fuente del Client(interfaz de usuario) y Server (comunicación entre Clients)
* `executable/app`: Archivos ejecutables `.jar` de Client y Server

## Requisitos previos
1.  **Java:** Descarga e instala el JDK o JRE (versión >= 17) desde [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) .

## Ejecución
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/uranium092/Chat
    ```
2. **Ir a los ejecutables `.jar`:** Navega a Chat/executable/app. Deberías de ver `Client.jar` y `Server.jar`
3. **Ejecutar (Primero)`Server.jar`:** Da doble click sobre `Server.jar` o ejecuta `java -jar Server.jar`. Se abrirá una ventana que registrará la comunicación entre clientes. Solo necesita ejecutarlo una vez.
4. **Ejecutar `Client.jar`:**
   * ***Asegúrate*** que `Server.jar` ya esté en ejecución, de lo contrario, sigue el paso anterior.
   * Ejecute `Client.jar` (doble clic o `java -jar Client.jar`) para iniciar una sesión de chat. Cada ejecución crea un nuevo usuario en la red. Por ejemplo, dos ejecuciones crean dos usuarios que pueden chatear entre sí.

## Consideraciones
* Si lo llega a probar, Asegúrese que `Server.jar` esté antes en ejecución que `Client.jar`
* Este chat está diseñado para funcionar en la misma máquina, por lo que los usuarios deben estar en el mismo entorno local.
* La interfaz de usuario es simple y directa, enfocada en la funcionalidad principal de la conversación.
