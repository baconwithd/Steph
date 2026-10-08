# Survivor Creator v13 + First Aid 3.4.5

Instala juntos los dos JAR nuevos y elimina sus versiones anteriores; usa las mismas versiones en servidor y clientes. Conserva Zomboid Hordes 1.5.0, eZWeight v7, los mods de armas y el resto del pack.

## Habilidades activas

Solo aparecen estas nueve habilidades. Las demás se retiran del registro visible y no ganan XP, sin borrar su XP anterior guardada. Fuerza sigue ganando XP al acertar golpes con armas reconocidas aunque ya no haya una skill separada por cada familia de arma.

Cada bonus es por nivel y cada skill llega a nivel 10. Las capacidades de carga requieren eZWeight v7. Un punto de vida equivale a medio corazón.

| Skill | Práctica | Bonus por nivel |
|---|---|---|
| Fitness / condición física | Correr por el suelo y saltar | +0,4 armadura, +1 punto de vida máxima y +1,5 kg de carga |
| Fuerza | Acertar golpes cuerpo a cuerpo con armas reconocidas a criaturas | +0,8 daño cuerpo a cuerpo, +1 punto de vida máxima y +2 kg de carga |
| Carrera | Correr por el suelo, fuera del agua | +0,5 kg de carga |
| Carpintería | Talar troncos y fabricar objetos de madera compatibles | +3% de velocidad al romper troncos y tablones |
| Metalurgia | Fabricar objetos metálicos y sacar metales, vidrio o ladrillos del horno | +3% de velocidad en bloques minables con pico |
| Cocina | Fabricar comida y retirarla cocinada del horno | +0,1 de saturación adicional al comer, limitada por la barra de comida |
| Agricultura | Cosechar cultivos maduros compatibles, plantar y arar | +2 puntos porcentuales de posibilidad de obtener un alimento extra |
| Primeros auxilios | Fabricar vendas/suturas/férulas, aplicar tratamientos válidos de First Aid y extraer balas | −4% de tiempo de aplicación y recuperación, +5% de curación por pulso |
| Pesca | Recoger una captura con caña | −4% de espera, +0,2 de suerte al generar el botín y +5 puntos porcentuales de posibilidad de duplicar un pez |

Fitness y Fuerza al nivel 10 suman **20 puntos de vida extra**: la vida máxima vanilla pasa de 20 a 40, salvo otros modificadores. First Aid utiliza su escalado de salud para reflejar la capacidad adicional en las partes; su redondeo y los límites configurados por parte siguen aplicándose. Subir nivel no cura automáticamente las heridas.

Pesca 10 reduce la espera un 40%, suma 2 de suerte y tiene un 50% de posibilidad de dar un pez extra cuando la captura contiene pescado. La suerte mejora las probabilidades de las tablas vanilla; no garantiza tesoros. Se siguen exigiendo las condiciones de agua abierta para tesoros, y los encantamientos de la caña se conservan.

Primeros auxilios 10 reduce los tiempos un 40% y aumenta la curación de cada pulso un 50%. Una extracción de 4 segundos pasa a aproximadamente 2,4 segundos; un vendaje clínico de 3,3 segundos, a unos 2 segundos. Se aplica a las curas por partes de First Aid y a sus vendajes clínicos. Las acciones especiales consumidas dan XP real, sin duplicar la recompensa cuando el mismo tratamiento atraviesa varios hooks. Los consumibles genéricos de otros mods que no usan este sistema mantienen sus propios efectos.

## Progreso y menú

Se conserva el ritmo lento de v12 y la XP anterior. Umbrales acumulados: 30, 80, 160, 280, 450, 700, 1000, 1400, 1900 y 2500 XP. La XP por acción baja con el nivel actual. Durante los 10 segundos de gracia no se gana XP. Los avisos de subida siguen en una cola, separados 4,5 segundos.

En la pestaña Habilidades del menú corporal de First Aid verás grupos, nombres en filas compactas y diez casillas: doradas para niveles ganados, gris parcial para la XP hacia el siguiente nivel y oscuras para niveles pendientes. Al pasar el ratón por una fila verás nivel, XP del tramo actual y bonos. La información se sincroniza desde el servidor cada segundo, también en multijugador. `/survivor skills` sigue disponible.

## Daño flotante

Al dañar a un jugador o monstruo aparecen durante 24 ticks (1,2 segundos a 20 TPS):

- Blanco: **GOLPE**, daño normal.
- Dorado: **CRÍTICO**, confirmado por el evento de crítico cuerpo a cuerpo de NeoForge.
- Rojo: **HEADSHOT**, confirmado por el evento de impacto de TaCZ, o por la intersección de la dirección del golpe cuerpo a cuerpo con el 20% superior de la caja del objetivo.

Un headshot tiene prioridad visual si también fue crítico. Los avisos muestran daño efectivo; con First Aid se usa la pérdida real de salud de las partes. No añaden un multiplicador nuevo de daño. Los golpes cancelados o sin daño no muestran números. Se limita la acumulación a cinco avisos por víctima y 80 globales; se limpian al parar el servidor y al recargar el mundo.

## Reciclaje en crafteo

- 1 trampilla de madera → 2 tablones de su tipo.
- 1 cartel normal o colgante de madera → 2 tablones de su tipo.
- 1 trampilla de hierro → 2 lingotes de hierro.

Incluye las once familias de madera vanilla: roble, abeto, abedul, jungla, acacia, roble oscuro, manglar, cerezo, bambú, carmesí y distorsionado. Estas recetas no borran por sí solas un cartel colocado: hay que recoger el ítem y ponerlo en el crafteo. Se conserva el reciclaje anterior de vallas y slabs. Fabricar/reciclar carteles y slabs no da XP para evitar ciclos gratuitos.

## Validación

Compilado con Java 21 contra Minecraft 1.21.1, NeoForge 21.1.255 y los JAR exactos del pack. 96 comprobaciones de balance, tiempos y filtrado; 22 comprobaciones de integración; recetas JSON válidas; verificación de estructura y pila de las clases. Se conservan la gracia, la cola de avisos, las curas y la infección reducida de la versión anterior. No se pudo arrancar una instancia de Minecraft en este entorno: la comprobación completa de renderizado, TaCZ y jugabilidad debe hacerse dentro del pack.
