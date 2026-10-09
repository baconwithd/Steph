# TacZ Tactical 1.0.0 — versión experimental

Complemento para **Minecraft 1.21.1 / NeoForge**, preparado contra el archivo `tacz-neoforge-1.21.1-1.1.8-hotfix-r7.jar` que proporcionaste.

## Descargas e instalación

1. Descarga [tacz-tactical-1.0.0-neoforge-1.21.1.jar](tacz-tactical-1.0.0-neoforge-1.21.1.jar) usando **Download raw file** en GitHub.
2. Colócalo en `mods` junto a tu **TACZ original hotfix-r7** y sus dependencias.
3. Para la IA de bandidos usa también [el JAR de NPCs Tactical](../tacznpcs-tactical/tacznpcs-2.1.0-1.21.1-tactical.1.jar), sustituyendo el NPC original. No pongas ambos JAR de NPCs a la vez.
4. En multijugador instala el complemento en servidor y clientes, con los mismos ajustes comunes.
5. Prueba primero en una copia del mundo. Esta versión todavía no se ha ejecutado dentro de Minecraft.

Este archivo es un **mod adicional pequeño**: el JAR original de TACZ sigue instalado. La dependencia exige la versión exacta `1.1.8-hotfix-r7`.

## Cambios

- **Precisión al apuntar:** dispersión angular ×0,55; equivale a 45 % menos dispersión. En escopetas ×0,85 para mantener un cono de perdigones. La dispersión desde la cadera conserva sus valores originales.
- **Sonido:** alcance configurado de disparos normales ×2 y silenciados ×1,25. El alcance efectivo también depende de los chunks y entidades que el servidor envía al cliente, la configuración base y la atenuación.
- **Ráfagas nativas:** las de tres disparos pasan a cuatro; su pausa entre grupos queda al 75 %, con un mínimo de 0,05 segundos. Solo afecta al modo ráfaga existente del arma.
- **Fogonazo:** tamaño ×1,2 y ventana visual de 80 ms frente a 50 ms.
- **Trazadoras:** grosor visual ×1,2. TACZ ya las representa con iluminación máxima; se conserva su frecuencia configurada y la del NPC.
- **Luz de disparo:** pulso de luz 14 durante 4 ticks, con partículas, tanto para el jugador como para tiradores remotos/NPCs cuando se recibe su evento de sonido normal.
- **Impactos en superficies:** chispas y pulso de luz 11 durante 5 ticks al crearse la marca de bala de TACZ. Si las marcas de bala están desactivadas, este efecto puede no producirse. No se aplica a impactos en entidades.
- **Silenciadores:** conservan la supresión del fogonazo y no crean el pulso de luz adicional de disparo.

Las luces usan estados temporales de `minecraft:light` **solo en el mundo del cliente**, en posiciones de aire cargadas. Se restaura el estado anterior al expirar, siempre que siga presente el estado temporal. No envían cambios de bloques al servidor. Hay un límite de 16 luces y 48 bloques de radio; pulsos superpuestos prolongan la misma luz. El resultado visual y el coste de actualización dependen del renderizador y del equipo.

El complemento no garantiza impactos: movimiento, cobertura, caída de la bala y dispersión restante siguen influyendo. La pauta de 3 trayectorias precisas de cada 10 pertenece al mod de NPCs.

## Configuración

Al iniciar se genera `config/tacz-tactical.properties`. Reinicia el juego/servidor tras editarlo. Incluimos [un ejemplo con los valores predeterminados](tacz-tactical.properties).

- `aim_spread_multiplier`: menor valor = mayor precisión al apuntar.
- `dynamic_lights=false`: desactiva los pulsos de iluminación; conserva partículas y demás ajustes.
- `max_dynamic_lights`, `dynamic_light_radius`: permiten reducir el coste de las luces.
- `three_round_burst_count=3`: conserva ráfagas nativas de tres balas si lo prefieres.

## Validación y límites

- Las tres partes del 7z se unieron y el JAR original pasó la comprobación ZIP.
- Compilación con Java 21; verificación estructural de 14 clases.
- Auditoría de 14 puntos de Mixin contra los descriptores exactos del JAR recibido, incluyendo argumentos, métodos estáticos y retención de anotaciones.
- Pruebas simuladas de dispersión, ráfagas, restauración de luces, superposición, límites, actualizaciones del mundo, chunks descargados, cambio de mundo y desactivación.
- **No se ha probado el arranque de NeoForge, la aplicación real de los Mixins ni el resultado visual dentro del juego.** Las pruebas simuladas no sustituyen esa comprobación. Si falla, conserva `logs/latest.log` y el informe de `crash-reports`.

SHA-256 del TACZ original examinado:
`fa3a09f15265a4065a566960a8deab315f6c601a5f9a542840b362d455dd5470`

## Fuentes

[Descargar fuentes y pruebas](tacz-tactical-fuentes.zip). El paquete contiene `src`, pruebas, auditoría y `build.sh` (Java 21 y Python 3.11+). El script genera interfaces mínimas para compilación, que **no se incluyen en el JAR**; NeoForge/Mixin proporcionan las clases reales en ejecución. Se puede ejecutar `bash build.sh /ruta/al/tacz-original.jar` para compilar y auditar contra ese archivo.

Este complemento no incluye modelos, sonidos ni el JAR original de TACZ.
