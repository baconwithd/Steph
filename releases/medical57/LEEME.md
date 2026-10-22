# First Aid 3.6.20 — animaciones por zona

Reemplaza SOLO los dos mods incluidos: First Aid 3.6.19 por 3.6.20, y LesRaisins Tactical Equipements melee .4 por medical-view .5. No dejes las versiones antiguas junto a las nuevas. Actualiza los equipos donde ya están instalados. Conserva TaCZ, compatibilidad médica, cámara y los demás mods.

## Animaciones

Tercera persona: gestos diferenciados para cabeza, torso, brazo izquierdo/derecho, piernas y pies. Secuencias de vendaje, presión y fijación de tablilla, extracción, inyección, medicina, desinfección y retirada. Entrada/salida amortiguada y transición entre objetivos. Cabeza, hombros y mangas siguen la inclinación del torso; los desplazamientos se restauran para no acumularse. Reanimación y ejercicios utilizan poses específicas a partir de sus estados existentes.

Primera persona: desplazamientos y giros suaves de manos/equipo hacia la zona atendida. Cabeza sube el gesto; piernas y pies lo llevan más abajo; brazos cambian su posición lateral. Hay curvas diferentes para inyección, medicina, extracción y compresión. LR conserva sus modelos, duración, efectos y animación nativa de consumible, con una trayectoria de manos suavizada y menor movimiento de su hueso de cámara. El movimiento adicional se aplica dentro del push/pop de la pila, sin modificar la orientación real del jugador ni el apuntado. Las medicinas LR con identificador guardado en el ItemStack también se reconocen.

Los gestos usan el esqueleto estándar de Minecraft: no se añaden codos, dedos ni nuevas mallas de piernas en primera persona. Son animaciones de presentación; no modifican colisión, tiempos de tratamiento, habilidades, consumo o curación. Se conservan las decisiones y la cámara de supervivencia incluidas en la base 3.6.19.

## Preview

FirstAid-animaciones-preview.mp4 y .gif muestran las curvas de Motion57 y FirstPerson57 en un maniquí renderizado fuera de Minecraft. Las vistas pequeñas de primera persona son una referencia de trayectoria: no reproducen el esqueleto y la animación de recursos de LR completa, ni tu skin ni shaders. No son una grabación del juego. El ZIP de fuentes incluye el generador del preview y las curvas utilizadas.

## Base y comprobación

Basado en los dos JARs enviados y en el flujo médico/isométrico del repositorio ZrEsper/putamadre, HEAD consultado 18f20af4faca6802599564fa044b1faad0c981c8. Se conserva la base del usuario en lugar de reemplazarla por una compilación antigua del repositorio.

Compilación Java 21 y verificación de bytecode de ambos JARs. 1.746.342 comprobaciones de continuidad, valores finitos, variantes, límites y restauración; pruebas de identificación de medicinas LR, modo de extracción/tablilla, zona elegida, caducidad de gestos y ejecución de la integración de primera persona sobre APIs simuladas. Se comprobaron retención de @Mixin, descriptor, variables de argumentos y posición tras pushPose. La integración de LR se omite si First Aid no está disponible.

NO se ejecutó el modpack completo ni se grabó Minecraft. Deben comprobarse en juego el aspecto final, la prioridad frente a otros mods de animación y la sincronización entre clientes. El preview técnico permite revisar el movimiento, no demuestra compatibilidad visual con todos los modelos y shaders. Los gestos de limpieza/retirada son locales; las tareas médicas, reanimación y ejercicio usan la sincronización existente de First Aid. No se añaden animaciones propias al sistema médico independiente de NPCs.
