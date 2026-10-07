# First Aid · Heridas y telas v3.4

1. Cierra Minecraft y, si juegas con servidor, detén el servidor.
2. Descarga `aplicar_firstaid_heridas_v34.py` usando **Download raw file** (la flecha hacia abajo de GitHub). El repositorio es privado: entra con tu cuenta de GitHub.
3. Guarda el archivo en la carpeta `mods`, junto a los JAR que enviaste. Comprueba que no termine en `.txt`.
4. Abre PowerShell en `mods` y ejecuta:

```powershell
python .\aplicar_firstaid_heridas_v34.py
```

El instalador reemplaza First Aid y The Hordes conservando sus nombres. Verifica las versiones exactas antes de escribir y crea copias fuera de `mods`, en `copias_firstaid_heridas`. Puedes ejecutarlo de nuevo: detecta el parche ya aplicado. Self Expression y Horde Hoard no necesitan reemplazarse. Conserva Les Raisins con el parche de animaciones anterior.

En multijugador, aplica los mismos JAR modificados en el servidor y en los clientes. Ambos deben tener Les Raisins y las dependencias originales de los mods.

## Cómo funciona

- Salud muestra las ocho partes en **dos columnas y cuatro filas**, sin bajar. Las dos figuras muestran balas, vendajes, cortes, mordidas y sangrado.
- Selecciona una parte y pulsa **Tratar**: el tratamiento comienza automáticamente. Un clic posterior lo cancela.
- Tela, venda y tirita tardan **3,3 segundos**. La tela usa el modelo y la animación de la venda, y su curación normal para lesiones sin balas también copia el sanador de la venda.
- Recicla una prenda de **Self Expression** sola en la cuadrícula de fabricación: botas 1 tela, pantalones 2, pechera 3 y casco 1. Hay recetas para las 876 prendas de esta versión. Las recetas de vendas con tela y fibra se conservan.
- Un vendaje se vuelve sucio tras **2 minutos de juego**. Si la zona todavía está lesionada, tiene un **25 % de infección local** en ese momento y cada 30 segundos adicionales. El material sucio puede seguir curando mientras no haya infección. Esta infección local causa dolor y detiene la recuperación; no equivale a la infección zombi de The Hordes.
- Para cambiarlo, selecciona la zona vendada y pulsa **Retirar vendaje**. Recibirás una tela limpia o sucia. Retirarlo no borra una infección existente; aplica material limpio para tratarla.
- Reutilizar tela sucia tiene un **50 % de infección local** al colocarla. La tela sucia es un objeto separado y no se convierte en vendas limpias mediante fabricación. Puedes tirarla o lavarla con clic derecho apuntando a agua: fuente, corriente, agua en un bloque o caldero con agua. Se lava una unidad por uso; el caldero gasta un nivel. La tela vuelve a ser normal.
- Cada golpe directo de un zombi que realmente haga daño tiene una tirada independiente del **45 % de corte leve**. Cada corte abierto quita **0,02 puntos de salud de esa parte cada 10 segundos**; vendarlo detiene esa pérdida. La infección local quita 0,04 cada 10 segundos. El sangrado fuerte se conserva como un estado distinto.
- Los golpes de zombis mantienen el **35 % de mordida** y, si hay mordida, el **75 % de infección** de The Hordes. La ropa en la ranura que cubre la parte reduce esas probabilidades según protección y desgaste. La armadura regional original de First Aid absorbe el daño del golpe. Un golpe completamente absorbido no genera estas heridas.
- Puedes vendar con la bala alojada. La recuperación tiene una sola tirada del **50 %** que se guarda y no se repite al cambiar de venda. La bala mantiene dolor aunque la recuperación tenga éxito. Extraer todas las balas permite recuperación normal.
- La lentitud conserva su reducción de movimiento y deja de reducir el FOV; se conservan el zoom del arco, el sprint y la opción de intensidad del FOV.

## Comprobaciones

Pasaron 50 comprobaciones de regresión y 116 comprobaciones nuevas con servicios de Minecraft simulados, la revisión del bytecode y las pruebas del instalador (contenido, respaldo, repetición y rechazo sin cambios ante versiones distintas). **No se ha iniciado Minecraft para probarlo dentro del juego.**

Las fuentes de los cambios y el procedimiento de reconstrucción están en `fuentes_firstaid_v34` de esta rama.
