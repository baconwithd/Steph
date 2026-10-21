# Corrección 53 — Iris, armaduras y pose de NPC

Reemplaza las versiones anteriores de estos CINCO mods por los JAR incluidos en mods/. No dejes duplicados: Gore .37, Ragdollified .30, Flashlight .20, FracturePoint .15 y Hardcore True Darkness .5.

Conserva Iris 1.8.14-beta.1, MakeUp UltraFast y los demás mods de la entrega anterior. No incluye ni modifica Iris. Mantén los mods de contenido sincronizados donde ya estén instalados; no agregues los mods de renderizado a un servidor dedicado que no los utilizaba.

## Cambios

- Con Iris activo, la linterna copia el color y la profundidad del escenario dibujado, en lugar de volver a renderizar el terreno con otro pipeline. La composición de linterna y oscuridad se difiere al final de GameRenderer.renderLevel, mediante un mixin de prioridad inferior al de Iris. Conserva el camino anterior sin shaders.
- El color recuperado del escenario iluminado es una aproximación: el resultado puede diferir de una linterna con iluminación nativa del shaderpack. Si falla la copia gráfica se desactiva ese intento hasta cambiar mundo o resolución, evitando reintentos intermitentes; consulta el log si desaparece el cono.
- El ragdoll consulta el proveedor de armadura de NeoForge utilizado por FracturePoint. Sus huesos respetan la parte corporal que está renderizando el ragdoll, evitando repetir el uniforme completo en cada extremidad. No añade nuevos recortes parciales a las mallas Geo de armadura.
- La pose capturada del jugador auxiliar de TaCZ NPCs se asocia al identificador del NPC real. Se reutiliza también al quedar derribado y al cambiar a cadáver. Se dibuja el equipo de manos con la transformación física de los brazos, sin modificar inventarios.

## Límites y comprobación

La pose conservada es la última capturada mientras el NPC se renderizaba, dentro de la retención existente de cinco segundos. Un NPC fuera de pantalla sin captura reciente puede usar una pose de respaldo. El renderizado de armas conserva las opciones y distancia de renderizado de armadura del ragdoll.

Se comprobaron compilación Java 21 y bytecode de los cinco JAR, compilación/enlace GLSL 150 con Mesa llvmpipe, copia real de color y profundidad en tres formatos, selección del proveedor de armadura, máscaras de huesos, orden diferido y descarte de fotogramas al cambiar mundo. Pasaron las pruebas previas de impactos, geometría y persistencia médica. Las pruebas de integración de helpers usan APIs simuladas y no sustituyen Minecraft.

NO se ejecutó tu modpack completo. La eliminación del parpadeo, las poses finales y el aspecto de la armadura requieren validación visual dentro del juego con tus shaders. Esta entrega corrige rutas concretas encontradas en los JAR; no garantiza compatibilidad visual con cualquier shaderpack.

Haz una copia del mundo antes de probar. Si persiste el fallo, conserva latest.log y un vídeo corto indicando si sucede con la linterna apagada y con shaders desactivados.
