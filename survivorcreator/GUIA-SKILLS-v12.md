# Skills y puntos — Survivor Creator v12

La XP se obtiene al practicar y sube automáticamente el nivel de esa skill. Los puntos de profesión/rasgos se asignan en el creador de personaje y funcionan aparte.

Se conservan tus niveles y XP existentes. Todas las skills tienen hasta 10 niveles. Los umbrales acumulados son: **30, 80, 160, 280, 450, 700, 1000, 1400, 1900 y 2500 XP**. Cada nivel exige más práctica; además, la XP por acción se divide entre `1 + 0,1 × nivel actual`. No hay nivel automático solo por estar quieto.

Durante la gracia de aparición no se gana XP. Al terminar, el ritmo es distinto por grupo: Fitness gana el 20% de su XP anterior; agilidad, el 25%; combate/armas de fuego, el 35%; oficios/supervivencia, el 50%. El multiplicador global se limita a 1 para que una configuración antigua de pruebas con valores como 10 no provoque subidas instantáneas. Valores inferiores a 1 permiten ralentizarlo más.

## Cómo sube cada skill y qué da

Bonos indicados **por nivel**, salvo donde se indique lo contrario. La carga adicional requiere el eZWeight modificado v7 que ya usamos en el pack.

| Skill | Cómo obtiene XP | Beneficio real |
|---|---|---|
| Fitness / condición física | Correr por el suelo y saltar | +0,4 de armadura y +1,5 kg de capacidad de carga. |
| Strength / fuerza | Golpes cuerpo a cuerpo efectivos a criaturas, usando un arma reconocida | +0,8 de daño cuerpo a cuerpo y +2 kg de capacidad de carga. |
| Sprinting / carrera | Desplazarte corriendo por el suelo, fuera del agua | +0,5 kg de capacidad de carga. |
| Lightfooted / paso ligero | Caminar por el suelo sin correr ni agacharte | Registra nivel; todavía sin bonus adicional. |
| Sneaking / sigilo | Desplazarte agachado | Registra nivel; todavía sin bonus adicional. |
| Nimble / agilidad | También se practica al desplazarte agachado | Registra nivel; todavía sin bonus adicional. |
| Axe / hachas | Golpear criaturas con hacha o hachuela | Registra práctica; el bonus de daño general lo da Fuerza. |
| Long blade / hoja larga | Golpear con espada, katana o machete | Registra práctica; todavía sin bonus propio. |
| Short blade / hoja corta | Golpear con cuchillo, daga, karambit o herramienta cortante reconocida | Registra práctica; todavía sin bonus propio. |
| Long blunt / contundente largo | Golpear con bate, club, palanca, tubo o bastón reconocido | Registra práctica; todavía sin bonus propio. |
| Short blunt / contundente corto | Golpear con martillo, llave, sartén o maza reconocida | Registra práctica; todavía sin bonus propio. |
| Spear / lanzas | Golpear con lanza, tridente, pica o alabarda | Registra práctica; todavía sin bonus propio. |
| Carpentry / carpintería | Talar troncos y fabricar objetos de madera compatibles | +3% de velocidad al romper troncos y tablones; máximo +30%. |
| Metalworking / metalurgia | Fabricar objetos metálicos y retirar metales, vidrio o ladrillos del horno | +3% de velocidad en bloques minables con pico; máximo +30%. |
| Cooking / cocina | Craftear alimentos o retirar comida cocinada del horno | +0,1 de saturación extra al comer, limitada por tu barra de comida. Comer por sí solo no da XP de cocina. |
| Farming / agricultura | Cosechar cultivos maduros compatibles, plantar y arar | +2 puntos porcentuales de posibilidad de obtener un alimento extra al cosechar; máximo 20%. Trigo, zanahoria, patata y remolacha son ejemplos compatibles. |
| First aid / primeros auxilios | Craftear vendas, suturas o férulas; consumir medicamentos compatibles | Registra práctica; todavía sin bonus propio a curación. Los tratamientos especiales que no consumen un ítem mediante el evento normal pueden no dar XP. |
| Electrical / electricidad | Craftear objetos identificados como cable, circuito, batería o redstone | Registra práctica; todavía sin bonus adicional. |
| Tailoring / costura | Craftear tela, lana y ropa/armaduras compatibles | Registra práctica; todavía sin bonus adicional. Un objeto identificado antes como metálico cuenta para metalurgia. |
| Fishing / pesca | Recoger una captura con la caña | Registra práctica; todavía sin bonus adicional. |
| Foraging / recolección | Recoger flores rompiéndolas | Registra práctica; todavía sin bonus adicional. No todas las plantas silvestres cuentan. |
| Maintenance / mantenimiento | Aún sin acción automática de XP | Aún sin bonus implementado. |
| Mechanics / mecánica | Aún sin acción automática de XP | Aún sin bonus implementado. |
| Aiming / puntería | Aún sin acción automática de XP | Aún sin bonus implementado. Disparar no la sube en esta versión. |
| Reloading / recarga | Aún sin acción automática de XP | Aún sin bonus implementado. Recargar no la sube en esta versión. |
| Trapping / trampas | Aún sin acción automática de XP | Aún sin bonus implementado. |

Las skills de armas registran su práctica; Fuerza aporta el aumento de daño real. La clasificación usa la familia/nombre del arma, por lo que un arma de mod con un nombre no reconocido puede no dar XP de combate.

## Ejemplos de XP al nivel 0

Con multiplicador global 1 y fuera de la gracia:

- Correr: Fitness 0,02 XP por tick y Sprinting 0,025 por tick. A 20 TPS, aproximadamente **75 segundos de carrera para Fitness 1** y **60 segundos para Sprinting 1**.
- Caminar: Lightfooted 0,00625 por tick, aproximadamente 4 minutos para nivel 1.
- Desplazarte agachado: Sneaking 0,0125 por tick; Nimble 0,00625 por tick.
- Golpe válido: Fuerza 0,35 y skill del arma 1,75 XP. Aproximadamente 86 golpes para Fuerza 1 y 18 para el arma 1.
- Cosecha madura o captura de pesca: 2,5 XP; 12 acciones para el primer nivel.
- Plantar o arar: 0,5 XP por acción.
- Fabricar/cocinar: 1 XP por unidad de resultado, con un límite de 16 unidades contabilizadas por evento. Algunas recetas se asignan a otra categoría según sus materiales.
- Talar un tronco: 1 XP de carpintería. Recoger una flor: 0,5 XP de recolección.
- Consumir un medicamento reconocido: 1,5 XP de primeros auxilios.

Las cifras disminuyen al subir el nivel. La creación y el reciclaje de slabs no dan XP; tampoco reciclar fences/trapdoors en tablones, para evitar ciclos de práctica gratuitos.

## Avisos de subida

Los niveles y sus beneficios se guardan al ganar XP. Los avisos visuales, sonido y chat se muestran **uno por uno**, en el orden en que se ganan, cada 90 ticks (4,5 segundos a 20 TPS). El popup dura 70 ticks. Si una gran ganancia cruza varios niveles de la misma skill, se muestran en orden 1, 2, 3… antes de los siguientes avisos. La cola se limpia al morir o desconectarse; la XP ganada conserva las reglas habituales del mod.

Las skills sin bonus ahora lo indican en el aviso; no muestran una mejora de estadísticas inexistente.

## Gracia y hordas

Al terminar de crear el personaje o reaparecer tienes **200 ticks de protección (10 segundos a 20 TPS) y Velocidad II**. El menú de creación no consume esa gracia. Volver a conectarte con un personaje ya creado no otorga otros 10 segundos; una gracia en curso conserva el tiempo restante. La protección bloquea daño normal mediante el evento de daño; comandos administrativos como `/kill` conservan su función.

Las hordas nuevas aparecen a **48–80 bloques** de su punto de referencia y al menos a **40 bloques de los jugadores**. Solo usan terreno y chunks cargados. Los zombis tienen +5% de velocidad base de día y +10% de noche: un poco más lentos que antes.

Cada intento válido de horda por ruido tiene **20% de probabilidad** de activarse, cinco veces menos que el inicio automático anterior. Un intento fallido también consume la espera de 2 minutos para impedir que disparar continuamente repita la tirada cada tick. Los zombis cercanos siguen oyendo el ruido e investigándolo. Los comandos de prueba fuerzan la horda y omiten esa tirada.

`/survivor skills` muestra niveles y XP. Para comprobar el orden, con operador y después de la gracia puedes usar `/survivor addxp TU_NOMBRE fitness 150` y `/survivor addxp TU_NOMBRE long_blade 90` en un personaje con esas skills a nivel 0. Las cifras del comando también pasan por el ritmo de XP nuevo.

Compilado y verificado fuera del juego; falta probar el comportamiento completo en la instancia.
