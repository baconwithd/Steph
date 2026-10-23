# TacZ NPCs tactical.56 — estabilidad sobre tactical.55

Parche incremental para el JAR `tacznpcs-2.1.0-1.21.1-tactical.55-native-utilities.jar` suministrado. Conserva los módulos de comunidades, agricultura, profesiones, utilidades y todas sus recetas/datos.

Cambios:

- Las respuestas cortas y repetidas no provocan un cierre automático por su longitud o historial. La cola conserva una línea si su emisión falla; una sesión antigua no puede emitir en la charla de otro NPC.
- Las interrupciones por peligro de las charlas libres conservan sus líneas pendientes. Consultar otro NPC o rechazar un arma no borra la pausa. Tu compañero permite retomar la charla llevando un arma e iniciarla o retomarla mientras corres. Se mantiene el seguimiento durante la conversación.
- Retirar objetos del almacén admite varias pilas, respeta componentes y recupera inventarios si falla la escritura. No consulta posiciones nulas, depósitos ausentes o bloques que no sean contenedores. Incluye el cajón vinculado al contar existencias.
- El generador solo acepta combustible en las casillas que realmente lo admiten. Las máquinas inexistentes se omiten. Los errores liberan la tarea y aplican espera antes de reintentar.
- La colocación de muebles de Doomsday ejecuta el callback nativo, comprueba todas sus piezas y revierte bloques e inventario si falla el callback o se cancela cualquier pieza. La colocación nativa de camas permanece intacta.
- Fortificación, reparación, puertas, agricultura, tala y minería liberan el control tras 400 ticks sin progreso y reintentan después de 200. El movimiento y el trabajo efectivo conservan la tarea.
- El cambio de bando o refugio actualiza la comunidad del NPC sin retirar a sus compañeros de ella. Las opciones de navegación de puertas se activan solo cuando la navegación las admite.
- La lista de mods y el anuncio de ejecución identifican la versión .56.

## Instalación

Cierra Minecraft, sustituye únicamente el JAR NPC .55 por `tacznpcs-2.1.0-1.21.1-tactical.56-stability.jar` y deja una sola versión de TacZ NPCs en `mods`. Conserva el resto de mods de tu instalación. No requiere borrar NPCs ni bases. Confirma `2.1.0-tactical.56-stability` en la lista de mods y `[TacZ NPCs tactical.56]` en el arranque del mundo.

## Validación y límites

Cinco suites de simulación cargan las clases reales parcheadas con `-Xverify:all`: inventario, muebles, diálogo/seguimiento, comunidades/utilidades y profesiones. Incluyen fallos inyectados, cancelación de eventos, restricciones de casillas y rutas inalcanzables. No se ha ejecutado el modpack completo dentro de Minecraft; la colisión real de puertas y las interacciones con otros mods necesitan validarse allí.

El log aportado confirma .55 y un cierre por sprint, pero no identifica aquel NPC como reclutado. Sus otros cuatro cierres de encuentros llegan unos 31 segundos después de mostrar las opciones y son compatibles con agotar el plazo de respuesta. No contiene un crash de NPC. Los errores de recursos, taczaddon/Sophisticated, GeckoLib, Essential/Paxi y el spam de distancia de visión no se presentan como corregidos por este JAR. El log original no se incluye en la publicación.

## Reconstrucción

Java 21 y Python 3 en Linux/macOS. Las herramientas se descargan de Maven Central si faltan y se comprueban con SHA-256. Es necesario disponer del JAR .55 exacto (hash registrado en `build56.py`).

```sh
python3 build56.py /ruta/tacznpcs-2.1.0-1.21.1-tactical.55-native-utilities.jar --test
```

El JAR queda en `output/`. Los fuentes son el delta del parche, su aplicador ASM y las pruebas; el código original se conserva desde el JAR base. El build verifica que los archivos originales, salvo las once clases modificadas y los metadatos de versión, permanezcan idénticos. Los dobles de Minecraft/NeoForge de `tests/` solo se usan para simulación y nunca entran en el JAR.
