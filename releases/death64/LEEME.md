# First Aid .25 + Survivor Creator v38 — muerte antes de reaparecer

Instala los DOS JAR de este paquete y elimina sus versiones anteriores. En LAN/servidor, actualiza cliente y servidor. No hace falta sustituir Ragdollified ni Corpse.

## Pantalla

Se eliminó el panel grande con estadísticas en columnas y el título rojo. La distribución sigue tu referencia de Project Zomboid: calavera pequeña, texto blanco centrado sobre el mundo, tiempo sobrevivido escrito como una frase, zombis abatidos y modo de juego. Abajo hay tres botones negros compactos: Nuevo personaje, Menú principal y Salir al escritorio. Incluye una fuente sans de DejaVu, con su licencia, para separar esta pantalla del aspecto del menú anterior.

La vista de derribado conserva la opción de reanimarse o rendirse; no se trata como muerte definitiva.

## Cadáver y personaje nuevo

- La pantalla de muerte se abre al morir, antes del respawn. El resumen ya no depende de haber reaparecido.
- Se bloquea la solicitud automática de respawn del cliente mientras se muestra la muerte, incluso con reaparición inmediata activada.
- Una cámara local, sin añadir entidades al mundo, permanece en el lugar de la muerte para mostrar el cadáver y su entorno. El mundo continúa funcionando; no se crea otro cuerpo.
- «Nuevo personaje» abre el creador con una ficha vacía. Todavía no se pide reaparecer; la cámara permanece en el lugar de la muerte mientras eliges profesión, rasgos y zona.
- Solo una confirmación válida permite al servidor reemplazar al jugador muerto. A continuación se usa la creación normal de Survivor Creator, con su equipamiento y selección de aparición. La conexión se vincula al nuevo jugador antes de continuar.
- Las selecciones inválidas no provocan respawn ni entregan equipo. Los avisos de muerte del respawn de creación se suprimen para no reabrir el resumen.
- Al completarse la creación se libera la cámara hacia el personaje nuevo y se restauran la perspectiva, el HUD y los ajustes isométricos anteriores. La reanimación y la desconexión limpian sus estados respectivos.

Se conserva la restricción de los mundos extremos: este cambio no habilita respawn allí.

## Validación

Se verificaron las 428 clases de los dos JAR con ASM BasicVerifier. Las pruebas de JVM ejercitan el renderizador y la pantalla real modificada, la cámara fija, el creador sin respawn, los mensajes tardíos, la restauración al completar, la validación del servidor antes de reemplazar al jugador y la vinculación de la conexión. Otras comprobaciones comparan los archivos para conservar el resto de First Aid y Survivor Creator y excluir los dobles de prueba de los JAR.

No se ejecutó Minecraft con tu modpack en este entorno. Las API del juego se simularon en las pruebas; queda pendiente comprobar visualmente la integración completa con tus mods de cámara, cadáveres y física.

## Prueba dentro del juego

1. Muere lejos de tu punto de aparición: debes seguir viendo el lugar del cadáver, sin aparecer vivo en otro sitio detrás del resumen.
2. Elige Nuevo personaje y navega por profesión, rasgos y zona: la cámara no debe saltar al spawn.
3. Confirma el personaje y la zona: entonces debe ocurrir la reaparición y desaparecer el estado de muerte.
4. Comprueba otra muerte y una reanimación para verificar que no se conserva el estado de la vida anterior.
