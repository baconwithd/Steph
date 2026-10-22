# Gore .42 + TaCZ .22

Reemplaza las versiones anteriores de estos DOS mods por los JAR de este paquete. No dejes dos versiones de Gore ni de TaCZ en mods. En multijugador, actualiza ambos lados. Conserva tu Ragdollified actual; este paquete no lo reemplaza.

- Los cráteres usan el daño real del proyectil en el punto de impacto, antes de reducir la salud del zombi, y su velocidad. Una pistola deja menos destrucción que un rifle o francotirador de mayor potencia.
- Los perdigones que realmente alcanzan la cabeza en el mismo disparo cercano acumulan destrucción; pueden acabar de destrozarla aunque el zombi muera antes de recibir el último. Los disparos distantes no reciben esa bonificación de proximidad.
- Un francotirador con daño alto puede destruir completamente la cabeza. Los impactos menores dejan cráteres localizados. Se conservan las cortaduras de armas blancas y el comportamiento de explosiones.
- Al morir por un disparo, el cadáver recibe un empuje horizontal en la dirección de la bala, graduado por daño y velocidad. Las escopetas consideran la concentración de perdigones por proximidad. La velocidad queda limitada a 1,4 m/s y no se añade lanzamiento vertical ni un segundo impulso de muerte. Se conservan los límites existentes al disparar a cadáveres ya muertos.
- ALT y la patada bajan el arma en primera y tercera persona, igual que al acercarla a una pared.
- La apertura de puerta con una TaCZ usa el brazo izquierdo: extender, alcanzar el punto de contacto, girar y retirar. Se conserva el disparador nativo de puerta abierta correctamente y el uso contextual con clic derecho. El gesto nuevo es local; otros jugadores conservan el sistema de sincronización existente. No se añade una segunda interacción de juego.

## Validación y límites

Se verificaron las 1.265 clases de los JAR con ASM BasicVerifier. Hay pruebas de JVM sobre los propios helpers de producción con dobles de las API del juego: metadatos de proyectiles, acumulación de perdigones, transición vivo/cadáver, límites de empuje, paquetes, radio de daño y gesto de brazo izquierdo. Un contrato compara los archivos y comprueba los enganches de bytecode: se conservan los recursos y clases fuera de los cambios previstos.

No se pudo ejecutar Minecraft con tu modpack aquí. Esto NO es una prueba visual dentro del juego. Revisa especialmente el brazo con tus modelos de armas y el empuje con tu configuración de física. El log identifica Ragdollified .32; su contrato de creación se contrastó con el .31 disponible, manteniendo tu JAR de Ragdollified.

## Prueba rápida dentro del juego

1. Compara pistola, rifle y francotirador sobre zombis nuevos, a igual distancia y apuntando a la misma zona.
2. Compara escopeta a 1–3 bloques y a más de 8 bloques. Prueba un disparo que mate antes de que terminen de llegar los perdigones.
3. Abre una puerta con el arma equipada, en primera y tercera persona. Prueba ALT y la patada: el cañón debe bajar.
4. Mata zombis mientras caminan o están quietos: deben caer con un empuje hacia delante del proyectil, sin salir disparados hacia arriba.
