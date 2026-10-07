from pathlib import Path
import json,re
root=Path('work/v34/fa'); source='' 
counts={'Helmet':1,'Boots':1,'Leggings':2,'Chestplate':3}; items=json.loads(Path('work/v34/recycled_items.json').read_text())
assert len(items)>700,len(items)
for name,slot in items:
 data={'neoforge:conditions':[{'type':'neoforge:mod_loaded','modid':'selfexpression'}],'type':'minecraft:crafting_shapeless','ingredients':[{'item':'selfexpression:'+name}],'result':{'id':'firstaid:cloth','count':counts[slot]}}
 p=root/'data/firstaid/recipe'/('recycle_selfexpression_'+name+'.json');p.write_text(json.dumps(data,separators=(',',':')))
# Dirty cloth has a separate registry identity: crafting cannot bypass laundering.
model={'parent':'minecraft:item/generated','textures':{'layer0':'firstaid:item/cloth'}}
(root/'assets/firstaid/models/item/dirty_cloth.json').write_text(json.dumps(model))
for p in (root/'assets/firstaid/lang').glob('*.json'):
 if p.stem not in ['en_us','es_ar','es_cl','es_ec','es_es','es_mx','es_uy','es_ve']:continue
 d=json.loads(p.read_text());d['item.firstaid.dirty_cloth']='Dirty cloth · wash in water' if p.stem=='en_us' else 'Tela sucia · lavar en agua';d['item.firstaid.cloth']='Cloth bandage' if p.stem=='en_us' else 'Tela para vendar';p.write_text(json.dumps(d,ensure_ascii=False,indent=2)+'\n')
p=root/'META-INF/neoforge.mods.toml';s=p.read_text().replace('zomboid-v3.2-lrtactical','zomboid-v3.4-cloth-hordes').replace('First Aid · Zomboid v3.2 · LR Medical','First Aid · Zomboid v3.4 · Heridas y telas');s+='\n[[dependencies.firstaid]]\nmodId="hordes"\ntype="required"\nversionRange="[1.6.3f,1.6.3g)"\nordering="AFTER"\nside="BOTH"\n';p.write_text(s)
# The Hordes must load with this exact paired bridge to avoid a missing helper.
p=Path('work/v34/hordes/META-INF/neoforge.mods.toml');s=p.read_text();s+='\n[[dependencies.hordes]]\nmodId="firstaid"\ntype="required"\nversionRange="[1.3.3,)"\nordering="NONE"\nside="BOTH"\n';p.write_text(s)
Path('work/v34/recycled_items.json').write_text(json.dumps(items));print('Recycling recipes:',len(items));print('slot counts:',{slot:sum(x[1]==slot for x in items) for slot in counts})
