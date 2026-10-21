# Corrección 52 — cadáveres, armadura y linterna

Continúa medical51 (`977ec0ff265a3dc7bb90a64b995384c87c1c78f1`) sobre los JAR que adjuntaste el 21 de octubre. No usa los borradores anteriores de medical50.

## Instalación

Cierra Minecraft y el servidor. Sustituye los cinco mods incluidos; conserva **una sola versión por mod**:

| Mod | Sustituir | Nuevo |
| --- | --- | --- |
| Zomboid Zombie Gore | .35 | .36 filled-wounds-search |
| Ragdollified | .28 | .29 continuous-corpse-pose |
| Zombie Remains Loot | .24 | .25 quietly-search |
| Quietly | v18 | v19 ragdoll-search |
| Flashlight | .18 | .19 iris-terrain-programs |

Los cuatro primeros deben coincidir en cliente y servidor. Flashlight se reemplaza donde ya esté instalado; este ZIP no añade mods a un servidor que no los use. Conserva First Aid 3.6.17, compat 1.0.5, TaCZ NPCs .33, Corpse .5, Splatter .2, Hardcore True Darkness .4, EMF/ETF y MakeUp 9.5g.

Prueba primero con cadáveres nuevos. No se borran los inventarios ni se regeneran objetos que ya se hubieran perdido antes de instalarlo.

## Cambios

- **Interior de mordidas:** textura de tejido completamente opaca en lugar del atlas de marcas transparente; menor separación entre tejido y borde de la herida. Conserva las correcciones de tamaño del torso y de búferes médicos de medical51.
- **Saqueo:** el clic físico identifica si pertenece a un Corpse humano o al inventario de un zombi. Los humanos pasan por su interacción nativa, respetando su acceso, y los zombis mantienen su inventario existente. El Corpse sigue la posición del torso físico para que su entidad de inventario no quede lejos del cuerpo visible.
- **Quietly:** los ragdolls de zombis usan el tiempo de búsqueda de `Rules.ticks` y el nivel de Scavenging. Hay progreso e interrupción por movimiento, daño, cambio de mundo, apertura de otro menú, desaparición del cuerpo o desconexión. El Corpse humano reutiliza la búsqueda nativa. No se inventa un segundo botín. Los inventarios de ragdoll ya revisados se recuerdan durante la sesión.
- **Caída del NPC:** conserva la pose del ragdoll derribado al convertirlo en cadáver. Si aún se movía, no se marca como inmóvil en el aire; si estaba asentado, conserva ese estado. Se evita que el actor invisible caiga por su cuenta mientras sigue al ragdoll.
- **Armadura humanoide:** recortes locales de los impactos médicos del jugador, centrado sobre cada pieza física usando sus límites reales y ocultación de piezas cuyo miembro ya está destruido. Los renderizadores completos de armadura conservan su pose propia. La ruta cubierta es ModelPart/humanoide; las armaduras externas basadas exclusivamente en GeoBone no reciben un recortador geométrico nuevo en esta entrega.
- **Rendimiento:** mapas de modelos con claves débiles por identidad, sin ejecutar `hashCode` de los modelos ni vaciar una cola ilimitada en cada consulta. Se eliminan caras internas opacas exactamente coincidentes al construir la malla almacenada en caché; se conservan piel, marcas y superficies expuestas. La emisión del tejido de mordidas usa llamadas directas.
- **Error del último log:** el recolector omite entidades nulas y Corpse cuyo registro de muerte aún no está inicializado, en lugar de abortar su recorrido.
- **Linterna con Iris/Sodium:** conserva los programas de terreno compatibles con el formato de los chunks durante la captura. La versión anterior devolvía `null` al pedirlos. Se conserva el aislamiento y la restauración del estado de Iris.
- **Sombras repetidas:** el recorrido de profundidad valida que la superficie esté junto al rayo de la luz, en vez de aceptar cruces de profundidad de cámara que pueden repetir siluetas. Incluye rechazo de sombras del arma junto al emisor. Usa 16 muestras simples en lugar de las 8 muestras dobles anteriores.
- **Estabilidad de luz:** prioridad con histéresis para no alternar continuamente entre dos NPC cercanos. Encendido y apagado gradual de unos 0,3 s, incluidos cambios de fuente, con un límite a la variación por fotograma si hay un tirón. Se mantienen como máximo dos luces activas.

## Evidencia y límites

El perfil dura 78,4 segundos y registra aproximadamente 20 TPS y 5,3 ms de media por tick del servidor. En el cliente, las rutas de Gore acumulan un 36,92 % del muestreo y Ragdollified un 29,01 %; **se solapan y no deben sumarse**. La generación de datos extendidos de vértices con shaders ocupa un 19,34 %. Esto orienta las correcciones de modelos y geometría; no demuestra una mejora concreta de FPS.

Se analizan los métodos de todos los JAR modificados con ASM/BasicVerifier. Pasan las pruebas de tejido/búfer, armadura y pivotes, búsqueda y transición de cadáver, restauración de Iris, rampas de luz, impactos reales de Bullet13.tick, 217 controles de coordenadas y 4030 controles médicos/de persistencia. Las APIs del juego se simulan; las funciones de los mods sí son las compiladas para entregar. Los JAR no contienen esas clases de compilación.

El shader GLSL 150 compila y enlaza en OpenGL por software Mesa/llvmpipe. **No se ejecutó Minecraft con tu modpack ni se validó visualmente la desaparición de líneas, sombras duplicadas o destellos. No se certifica ausencia de parpadeo.** El código corrige las rutas descritas y necesita esa comprobación final dentro del juego. Los avisos externos de packs/MTS/Essential del log no se presentan como reparados.

Se incluyen fuentes, pruebas y SHA-256. Las pruebas de regresión reutilizan las fuentes publicadas de medical49; no son un entorno completo de desarrollo de Minecraft.
