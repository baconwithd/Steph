package ichttt.mods.firstaid.compat.lrtactical;

import ichttt.mods.firstaid.survival.ClinicalState;
import java.lang.reflect.*;
import java.util.*;

/** Exact-version bridge. No client classes are referenced by server method signatures. */
public final class TreatmentV33 {
    static final String FA="ichttt.mods.firstaid.";
    static final Map<String,Field> FIELDS=new java.util.concurrent.ConcurrentHashMap<>();
    static final Map<String,Method> PRIVATE_METHODS=new java.util.concurrent.ConcurrentHashMap<>();
    private static final Set<Object> SERVER_USES=Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
    private static Object autoPlayer, autoHand, autoStack;
    private static int autoMode, autoStartTick, autoDuration;
    public static Class<?> type(String name) {
        try{return Class.forName(name);}catch(ClassNotFoundException e){throw new IllegalStateException(name,e);}
    }
    public static Object call(Object o,String name,Object...args){return MedicalBridge.call(o,name,args);}
    public static Object stat(String cls,String name,Object...args){return call(type(cls),name,args);}
    public static Field field(Object o,String name) {
        Class<?> c=o instanceof Class<?>?(Class<?>)o:o.getClass();String key=c.getName()+"#"+name;
        return FIELDS.computeIfAbsent(key,k->{
            for(Class<?> t=c;t!=null;t=t.getSuperclass())try{Field f=t.getDeclaredField(name);f.setAccessible(true);return f;}catch(NoSuchFieldException ignored){}
            throw new IllegalStateException(key);
        });
    }
    public static Object get(Object o,String name){try{return field(o,name).get(o instanceof Class<?>?null:o);}catch(IllegalAccessException e){throw new IllegalStateException(name,e);}}
    public static void set(Object o,String name,Object v){try{field(o,name).set(o instanceof Class<?>?null:o,v);}catch(IllegalAccessException e){throw new IllegalStateException(name,e);}}
    static Object privateCall(Object o,String name,Object...args){
        Class<?> c=o instanceof Class<?>?(Class<?>)o:o.getClass();String key=c.getName()+"#"+name+"/"+args.length;
        Method m=PRIVATE_METHODS.get(key);
        if(m==null){for(Class<?> t=c;t!=null&&m==null;t=t.getSuperclass())for(Method x:t.getDeclaredMethods())if(x.getName().equals(name)&&x.getParameterCount()==args.length){m=x;break;}
            if(m==null)throw new IllegalStateException(key);m.setAccessible(true);PRIVATE_METHODS.put(key,m);}
        try{return m.invoke(o instanceof Class<?>?null:o,args);}catch(ReflectiveOperationException e){throw new IllegalStateException(key,e);}
    }
    static Object mc(){return stat("net.minecraft.client.Minecraft","getInstance");}
    static Object model(Object player){return stat(FA+"common.util.CommonUtils","getDamageModel",player);}
    static ClinicalState state(Object model){return (ClinicalState)stat(FA+"survival.ClinicalData","state",model);}
    static Object[] enums(){return type(FA+"api.enums.EnumPlayerPart").getEnumConstants();}
    static Object part(Object m,int i){return call(m,"getFromEnum",enums()[i]);}
    static Object[] screenParts(){return (Object[])get(type(FA+"client.gui.GuiHealthScreen"),"PARTS");}
    static int number(Object o,String n){return ((Number)get(o,n)).intValue();}
    static void say(Object p,String s){call(p,"displayClientMessage",stat("net.minecraft.network.chat.Component","literal",s),true);}

    public static boolean recoveryAllowed(ClinicalState.Wound w){return w.bandaged&&!w.infected&&(w.bullets==0||w.lodgedOutcome==1);}
    public static int painFloor(Object model,int base){for(ClinicalState.Wound w:state(model).wounds)if(w.bullets>0||w.infected)return Math.max(2,base);return base;}
    public static float intensityFloor(Object model,float base){for(ClinicalState.Wound w:state(model).wounds)if(w.bullets>0||w.infected)return Math.max(0.5f,base);return base;}

    public static void writeExtra(Object model,Object tag){
        Object root=call(tag,"getCompound","zomboidClinical");ClinicalState s=state(model);
        for(int i=0;i<8;i++){Object child=call(root,"getCompound","part"+i);ClinicalState.Wound w=s.wounds[i];
            call(child,"putBoolean","bitten",w.bitten);call(child,"putBoolean","recovered",w.recovered);call(child,"putInt","lodgedOutcome",w.lodgedOutcome);
            call(child,"putInt","dressingAge",w.dressingAge);call(child,"putBoolean","dirty",w.dirty);call(child,"putBoolean","woundInfected",w.infected);call(child,"putInt","deteriorationTicks",w.deteriorationTicks);
        }
    }
    public static void readExtra(Object model,Object tag){
        Object root=call(tag,"getCompound","zomboidClinical");ClinicalState s=state(model);
        for(int i=0;i<8;i++){Object child=call(root,"getCompound","part"+i);ClinicalState.Wound w=s.wounds[i];
            w.bitten=(Boolean)call(child,"getBoolean","bitten");w.recovered=(Boolean)call(child,"getBoolean","recovered");w.lodgedOutcome=Math.max(0,Math.min(2,(Integer)call(child,"getInt","lodgedOutcome")));
            w.bandaged=(Boolean)call(child,"getBoolean","bandaged");
            w.dressingAge=Math.max(0,(Integer)call(child,"getInt","dressingAge"));w.dirty=(Boolean)call(child,"getBoolean","dirty");w.infected=(Boolean)call(child,"getBoolean","woundInfected");w.deteriorationTicks=Math.max(0,Math.min(199,(Integer)call(child,"getInt","deteriorationTicks")));
            if(w.recovered){w.gunshot=false;w.cut=false;w.bitten=false;w.healingLeft=0;}
            if(w.bandaged&&w.bullets>0&&w.lodgedOutcome==0)w.lodgedOutcome=2; // safe migration, no client random rolls
        }
    }
    public static boolean zombie(Object o){return o!=null&&type("net.minecraft.world.entity.monster.Zombie").isInstance(o);}
    public static double coverageProtection(Object player,Object region){
        Object slot=get(region,"slot");Object stack=call(player,"getItemBySlot",slot);
        if((Boolean)call(stack,"isEmpty"))return 0;
        double armor=((Number)stat(FA+"common.util.ArmorUtils","getArmor",stack,slot)).doubleValue();
        double condition=1;
        if((Boolean)call(stack,"isDamageableItem"))condition=Math.max(0,1-(double)(Integer)call(stack,"getDamageValue")/Math.max(1,(Integer)call(stack,"getMaxDamage")));
        return Math.min(0.85,0.25+Math.max(0,armor)*0.06)*condition;
    }
    public static double biteChance(double protection){return 0.35*(1-Math.max(0,Math.min(0.85,protection)));}
    public static double infectionChance(double protection){return 0.75*(1-Math.max(0,Math.min(0.85,protection)));}
    public static void recordBite(Object model,Object player,Object source,Object before){
        if(!type("net.minecraft.server.level.ServerPlayer").isInstance(player)||(Boolean)call(player,"isCreative")||(Boolean)call(player,"isSpectator"))return;
        Object attacker=call(source,"getDirectEntity");if(!zombie(attacker)||attacker!=call(source,"getEntity"))return;
        int index=-1;float most=0.0001f;
        for(int i=0;i<8;i++){float damage=((Number)get(part(before,i),"currentHealth")).floatValue()-((Number)get(part(model,i),"currentHealth")).floatValue();if(damage>most){index=i;most=damage;}}
        if(index<0)return;double protection=coverageProtection(player,enums()[index]);Object random=call(player,"getRandom");
        boolean mild=((Number)call(random,"nextFloat")).doubleValue()<0.45;
        if(mild){state(model).cancelJob();ClinicalState.Wound cut=state(model).wounds[index];cut.cut();call(model,"scheduleResync");}
        if(((Number)call(random,"nextFloat")).doubleValue()>=biteChance(protection))return;
        ClinicalState s=state(model);s.cancelJob();ClinicalState.Wound w=s.wounds[index];w.cut();w.bitten=true;
        stat(FA+"fracture.Bleeding","start",player,enums()[index]);say(player,"Mordida en "+call(enums()[index],"toString")+". Trata la herida.");
        if(((Number)call(random,"nextFloat")).doubleValue()<infectionChance(protection)){
            stat("net.smileycorp.hordes.infection.InfectedEffect","apply",player);
            if(infected(player))say(player,"Mordida infectada: infección de The Hordes.");
        }
        call(model,"scheduleResync");stat(FA+"common.util.CommonUtils","syncDamageModel",player);
    }
    public static boolean managedZombieDamage(Object event){
        Object player=call(event,"getEntity");if(!type("net.minecraft.server.level.ServerPlayer").isInstance(player))return false;
        Object source=call(event,"getSource");Object attacker=call(source,"getDirectEntity");
        return zombie(attacker)&&attacker==call(source,"getEntity")&&model(player)!=null;
    }
    static boolean infected(Object player){return player!=null&&(Boolean)call(player,"hasEffect",get(type("net.smileycorp.hordes.infection.HordesInfection"),"INFECTED"));}

    public static void begin(Object rawState,Object player){
        ClinicalState s=(ClinicalState)rawState;
        if(s.dressingPart<0&&s.extractionPart<0)return;
        Object hand=type("net.minecraft.world.InteractionHand").getEnumConstants()[s.jobHand];
        call(player,"startUsingItem",hand);set(player,"useItemRemaining",72000);
        SERVER_USES.add(player);
    }
    public static boolean validExtraction(Object stack,Object player){return (Boolean)call(player,"isUsingItem")&&(Boolean)stat(FA+"survival.ClinicalHooks","extraction",stack);}
    public static void finishServer(Object model,Object player){
        if(!SERVER_USES.contains(player))return;
        ClinicalState s=state(model);
        if(s.extractionPart<0&&s.dressingPart<0){
            if((Boolean)call(player,"isUsingItem"))call(player,"stopUsingItem");
            SERVER_USES.remove(player);
        }
    }
    static void startClient(Object hand,int mode,int duration){
        Object m=mc();Object p=get(m,"player");if(p==null)return;
        stat("me.xjqsh.lrtactical.client.input.ConsumableInputHandler","resetToggleUsing");
        autoPlayer=p;autoHand=hand;autoStack=call(call(p,"getItemInHand",hand),"copyWithCount",1);
        autoMode=mode;autoStartTick=number(p,"tickCount");autoDuration=Math.max(1,duration);
        set(type(FA+"client.ClientEventHandler"),"requireUseReleaseBeforeHealingSelection",false);
        call(p,"startUsingItem",hand);set(p,"useItemRemaining",72000);
        call(get(get(m,"options"),"keyUse"),"setDown",true);
    }
    public static void autoSelected(Object region,Object hand){
        Object p=get(mc(),"player");if(p==null)return;Object stack=call(p,"getItemInHand",hand);
        int duration=Math.max(1,(int)Math.ceil(((Number)call(call(stack,"getItem"),"getApplyTime",stack)).doubleValue()/50));
        startClient(hand,1,duration);
    }
    public static void clientBegin(Object screen,Object hand){
        Object p=get(mc(),"player");if(p==null)return;
        if(screen!=null&&state(privateCall(screen,"live")).wounds[((Enum<?>)get(screen,"selected")).ordinal()].bandaged)return;
        Object stack=call(p,"getItemInHand",hand);
        boolean extraction=(Boolean)stat(FA+"survival.ClinicalHooks","extraction",stack);
        boolean cloth="item.firstaid.cloth".equals(call(call(stack,"getItem"),"getDescriptionId"));
        startClient(hand,extraction?3:2,extraction?80:66);
    }
    public static boolean isHealingHeld(Object m){
        Object p=get(m,"player");if(p==null||get(m,"screen")!=null)return false;
        Object pending=get(type(FA+"client.ClientEventHandler"),"pendingHealingSelection");if(pending==null)return false;
        if(autoMode==1&&autoPlayer==p)return true;
        if(!(Boolean)call(get(get(m,"options"),"keyUse"),"isDown"))return false;
        Object used=call(p,"getUsedItemHand");return used==null||used==call(pending,"hand");
    }
    static void stopClient(boolean release){
        Object m=mc();Object p=get(m,"player");
        if(p!=null){
            if(release)call(get(m,"gameMode"),"releaseUsingItem",p);
            if((Boolean)call(p,"isUsingItem"))call(p,"stopUsingItem");
            set(type(FA+"client.ClientEventHandler"),"pendingHealingSelection",null);
            call(get(get(m,"options"),"keyUse"),"setDown",false);
            stat("me.xjqsh.lrtactical.client.input.ConsumableInputHandler","resetToggleUsing");
        }
        autoMode=0;autoPlayer=null;autoHand=null;autoStack=null;
    }
    public static void clientTick(){
        if(autoMode==0)return;Object m=mc();Object p=get(m,"player");
        if(p==null||p!=autoPlayer){autoMode=0;autoPlayer=null;return;}
        Object current=call(p,"getItemInHand",autoHand);
        if(!(Boolean)call(p,"isAlive")||!(Boolean)stat("net.minecraft.world.item.ItemStack","isSameItemSameComponents",current,autoStack)) {stopClient(true);return;}
        int elapsed=number(p,"tickCount")-autoStartTick;
        if(autoMode==1&&get(type(FA+"client.ClientEventHandler"),"pendingHealingSelection")==null){stopClient(false);return;}
        if(autoMode!=1&&elapsed>autoDuration+4){stopClient(true);return;}
        if(get(m,"screen")!=null){stopClient(true);return;}
        call(get(get(m,"options"),"keyUse"),"setDown",true);
        if(!(Boolean)call(p,"isUsingItem"))call(p,"startUsingItem",autoHand);
        set(p,"useItemRemaining",72000);
    }
    public static void mouseCancel(Object event){
        if((Integer)call(event,"getAction")!=1)return;
        Object m=mc();Object p=get(m,"player");if(p==null)return;
        if(autoMode!=0){stopClient(true);call(event,"setCanceled",true);say(p,"Tratamiento cancelado.");}
    }

    public static boolean blocksExternalHealing(Object wound){ClinicalState.Wound w=(ClinicalState.Wound)wound;return w.active()||w.infected;}
    public static boolean zombieSource(Object source){Object attacker=call(source,"getDirectEntity");return zombie(attacker)&&attacker==call(source,"getEntity");}
    public static Object DIRTY_ITEM;
    public static Object bandageItem(){return call(get(type(FA+"common.RegistryObjects"),"BANDAGE"),"get");}
    public static Object bandageHealer(Object stack){return call(bandageItem(),"createNewHealer",stack);}
    public static boolean dirtyStack(Object stack){return "item.firstaid.dirty_cloth".equals(call(call(stack,"getItem"),"getDescriptionId"));}
    public static boolean dressing(Object stack){return !(Boolean)call(stack,"isEmpty")&&Set.of("item.firstaid.cloth","item.firstaid.dirty_cloth","item.firstaid.bandage","item.firstaid.plaster").contains(call(call(stack,"getItem"),"getDescriptionId"));}
    public static void registerDirty(Object registry){
        java.util.function.Function<Object,Object> factory=properties->{
            try{return type(FA+"compat.lrtactical.ClothDressingItem").getConstructor(type("net.minecraft.world.item.Item$Properties")).newInstance(call(properties,"stacksTo",16));}
            catch(ReflectiveOperationException e){throw new IllegalStateException(e);}
        };
        DIRTY_ITEM=call(registry,"registerItem","dirty_cloth",factory);
    }
    static Object item(String id){return call(get(type("net.minecraft.core.registries.BuiltInRegistries"),"ITEM"),"get",stat("net.minecraft.resources.ResourceLocation","parse",id));}
    static Object newStack(Object item){
        try{return type("net.minecraft.world.item.ItemStack").getConstructor(type("net.minecraft.world.level.ItemLike")).newInstance(item);}catch(ReflectiveOperationException e){throw new IllegalStateException(e);}
    }
    static void give(Object player,Object stack){if(!(Boolean)call(call(player,"getInventory"),"add",stack))call(player,"drop",stack,false);}
    public static Object treatmentHand(Object screen,Object region){
        if(state(privateCall(screen,"live")).wounds[((Enum<?>)region).ordinal()].bandaged)return type("net.minecraft.world.InteractionHand").getEnumConstants()[0];
        return privateCall(screen,"v34NativeTreatmentHand",region);
    }
    public static boolean isClinical(Object wound){ClinicalState.Wound w=(ClinicalState.Wound)wound;return w.active()||w.bandaged;}
    public static boolean removeMessage(Object message,Object context){
        Object player=call(context,"player");if(!type("net.minecraft.server.level.ServerPlayer").isInstance(player)||(Boolean)call(player,"isSpectator")||!(Boolean)call(player,"isAlive"))return true;
        Object m=model(player);if(m==null)return true;
        Object region=call(message,"clinicalPart");if(region==null)return true;
        ClinicalState s=state(m);ClinicalState.Wound w=s.wounds[((Enum<?>)region).ordinal()];
        if(!w.bandaged)return false;
        if((Integer)call(m,"getUnconsciousTicks")>0)return true;
        s.cancelJob();Object removed=newStack(item(w.dirty||w.infected?"firstaid:dirty_cloth":"firstaid:cloth"));
        w.bandaged=false;w.dressingAge=0;w.dirty=false; // infection persists until fresh material treats the wound
        set(part(m,((Enum<?>)region).ordinal()),"activeHealer",null);
        give(player,removed);say(player,"Vendaje retirado. Lava la tela sucia usando agua antes de reutilizarla.");
        call(m,"scheduleResync");stat(FA+"common.util.CommonUtils","syncDamageModel",player);return true;
    }
    public static void applied(Object m,Object player,int index,Object stack){
        ClinicalState.Wound w=state(m).wounds[index];if(w.infected&&!w.cut&&!w.gunshot&&!w.bitten){w.cut=true;w.healingLeft=Math.max(w.healingLeft,2400);}
        w.bandaged=true;w.dressingAge=0;w.dirty=false;w.infected=false;w.deteriorationTicks=0;
        if(dirtyStack(stack)){w.dirty=true;w.dressingAge=2400;w.infected=((Number)call(call(player,"getRandom"),"nextFloat")).floatValue()<0.5f;say(player,w.infected?"La tela sucia ha infectado la herida. Cámbiala por material limpio.":"Tela sucia colocada: riesgo de infección; retírala y lávala.");}
    }
    public static void ordinaryApplied(Object context){
        Object stack=call(context,"getStack");if(!dressing(stack))return;
        Object m=call(context,"getDamageModel"),player=call(context,"getPlayer"),region=get(call(context,"getDamageablePart"),"part");
        applied(m,player,((Enum<?>)region).ordinal(),stack);
    }
    public static void tickHygiene(Object m,Object player){
        if(!type("net.minecraft.server.level.ServerPlayer").isInstance(player)||(Boolean)call(player,"isCreative")||(Boolean)call(player,"isSpectator")||!(Boolean)call(player,"isAlive"))return;
        ClinicalState s=state(m);int tick=number(player,"tickCount");if(s.lastPlayerTick!=tick||s.hygieneTick==tick)return;s.hygieneTick=tick;
        for(int i=0;i<8;i++){
            ClinicalState.Wound w=s.wounds[i];Object region=part(m,i);
            boolean open=w.active()||((Number)get(region,"currentHealth")).floatValue()<((Number)call(region,"getMaxHealth")).floatValue()-.01f;
            if(w.bandaged&&w.dirty&&open&&!w.infected&&w.dressingAge>=2400&&(w.dressingAge-2400)%600==0){
                if(((Number)call(call(player,"getRandom"),"nextFloat")).floatValue()<.25f){w.infected=true;say(player,"Herida infectada por un vendaje sucio. Retíralo y usa material limpio.");call(m,"scheduleResync");}
            }
            boolean harmful=w.infected||w.cut&&!w.bandaged;
            if(harmful){
                if(++w.deteriorationTicks>=200){w.deteriorationTicks=0;
                    call(region,"damage",w.infected?.04f:.02f,player,false,0f);
                    call(m,"syncVanillaHealth",player);call(m,"scheduleResync");
                    if(((Number)get(region,"currentHealth")).floatValue()<=0&&(i==((Enum<?>)enums()[0]).ordinal()||"BODY".equals(((Enum<?>)enums()[i]).name())))
                        stat(FA+"common.util.CommonUtils","killPlayerDirectly",player,call(call(player,"damageSources"),"generic"));
                }
            }else w.deteriorationTicks=0;
        }
    }
    public static Object clothUse(Object item,Object world,Object player,Object hand){
        Object stack=call(player,"getItemInHand",hand);
        if(dirtyStack(stack)){
            Object fluidMode=get(type("net.minecraft.world.level.ClipContext$Fluid"),"ANY");
            Object hit=privateCall(type("net.minecraft.world.item.Item"),"getPlayerPOVHitResult",world,player,fluidMode);
            Object pos=call(hit,"getBlockPos"),fluid=call(world,"getFluidState",pos);
            boolean water=(Boolean)call(fluid,"is",get(type("net.minecraft.tags.FluidTags"),"WATER"));
            Object block=null;boolean cauldron=false;
            if(!water){block=call(world,"getBlockState",pos);cauldron=(Boolean)call(block,"is",get(type("net.minecraft.world.level.block.Blocks"),"WATER_CAULDRON"));}
            if(water||cauldron){
                if(!(Boolean)call(world,"isClientSide")){if(cauldron)stat("net.minecraft.world.level.block.LayeredCauldronBlock","lowerFillLevel",block,world,pos);call(stack,"shrink",1);give(player,newStack(item("firstaid:cloth")));say(player,"Tela lavada. Ya puedes usarla sin riesgo por suciedad.");}
                return stat("net.minecraft.world.InteractionResultHolder","sidedSuccess",stack,(Boolean)call(world,"isClientSide"));
            }
        }
        return call(get(type(FA+"api.healing.HealingItemApiHelper"),"INSTANCE"),"onItemRightClick",item,world,player,hand);
    }
    public static double withoutSlowness(Object attribute){
        double base=((Number)call(attribute,"getBaseValue")).doubleValue(),add=0,mulBase=0,mulTotal=1;
        for(Object mod:(Collection<?>)call(attribute,"getModifiers")){
            if("effect.slowness".equals(call(call(mod,"id"),"getPath")))continue;
            double amount=((Number)call(mod,"amount")).doubleValue();String op=((Enum<?>)call(mod,"operation")).name();
            if(op.equals("ADD_VALUE"))add+=amount;else if(op.equals("ADD_MULTIPLIED_BASE"))mulBase+=amount;else if(op.equals("ADD_MULTIPLIED_TOTAL"))mulTotal*=1+amount;
        }
        return ((Number)call(call(call(attribute,"getAttribute"),"value"),"sanitizeValue",(base+add)*(1+mulBase)*mulTotal)).doubleValue();
    }
    public static float compensatedFov(float current,float raw,float scale,double speed,double clean,double walking){
        if(walking<=0||!Double.isFinite(clean))return current;
        double with=(speed/walking+1)/2,without=(clean/walking+1)/2;
        return with>0?(float)(current+raw*(without/with-1)*scale):current;
    }
    public static void fov(Object event){
        Object player=call(event,"getPlayer");
        if(!(Boolean)call(player,"hasEffect",get(type("net.minecraft.world.effect.MobEffects"),"MOVEMENT_SLOWDOWN"))||(Boolean)call(player,"isScoping"))return;
        Object attribute=call(player,"getAttribute",get(type("net.minecraft.world.entity.ai.attributes.Attributes"),"MOVEMENT_SPEED"));if(attribute==null)return;
        double speed=((Number)call(attribute,"getValue")).doubleValue(),clean=withoutSlowness(attribute);
        float scale=((Number)call(call(get(mc(),"options"),"fovEffectScale"),"get")).floatValue();
        double walking=((Number)call(call(player,"getAbilities"),"getWalkingSpeed")).doubleValue();
        call(event,"setNewFovModifier",compensatedFov(((Number)call(event,"getNewFovModifier")).floatValue(),((Number)call(event,"getFovModifier")).floatValue(),scale,speed,clean,walking));
    }

    static void fill(Object g,int x,int y,int x2,int y2,int color){call(g,"fill",x,y,x2,y2,color);}
    static void text(Object g,Object font,String value,int x,int y,int color){call(g,"drawString",font,value,x,y,color,false);}
    @SuppressWarnings("unchecked")
    public static List<String> extraInjuries(Object screen,Object region,List<String> original){
        List<String> lines=new ArrayList<>(original);ClinicalState.Wound w=state(privateCall(screen,"live")).wounds[((Enum<?>)get(region,"part")).ordinal()];
        if(w.bitten)lines.add("Mordida");
        if(w.dirty)lines.add("Vendaje sucio: cambiar o lavar");
        if(w.infected)lines.removeIf(s->s.startsWith("Cicatrizando"));
        if(w.infected)lines.add("Herida infectada · dolor");
        if(w.cut&&!w.bandaged)lines.add("Corte abierto: pérdida lenta de vida");
        Object p=get(mc(),"player");if(w.bitten&&infected(p))lines.add("Infección: The Hordes");
        if(w.bullets>0){lines.add("Dolor: bala alojada");if(w.bandaged&&w.lodgedOutcome==2){lines.removeIf(s->s.startsWith("Cicatrizando"));lines.add("Bala: recuperación bloqueada");}}
        return lines;
    }
    public static int grid(Object screen,Object g,Object model,int ignoredX,int ignoredY){
        Object layout=get(screen,"layout");Object font=get(screen,"font");Object selected=get(screen,"selected");
        int x=number(layout,"rightX"),top=number(layout,"contentTop"),bottom=number(layout,"contentBottom"),width=number(layout,"rightWidth");set(screen,"scroll",0);
        text(g,font,"Estado corporal · Dolor "+call(model,"getPainLevel")+" / 5",x+1,top+1,0xffd2c294);
        Object[] regions=screenParts();String[] names=(String[])get(type(FA+"client.gui.GuiHealthScreen"),"PART_NAMES");
        int rowH=Math.max(8,(bottom-top-20)/4),colW=width/2;
        for(int i=0;i<8;i++){
            int cx=x+(i%2)*colW,cy=top+18+(i/2)*rowH;Object region=call(model,"getFromEnum",regions[i]);
            List<String> lines=(List<String>)privateCall(screen,"injuries",region);if(lines.isEmpty())lines=List.of("Sin lesiones");
            int longest=((Number)call(font,"width",names[i])).intValue();for(String s:lines)longest=Math.max(longest,((Number)call(font,"width",s)).intValue());
            float scale=(float)Math.min(1,Math.min((colW-9.0)/Math.max(1,longest),(rowH-7.0)/(10+lines.size()*9.0)));
            fill(g,cx,cy,cx+colW-4,cy+rowH-3,regions[i]==selected?0xff292c22:0xff181d18);
            Object pose=call(g,"pose");call(pose,"pushPose");call(pose,"translate",(double)cx+3,(double)cy+2,0d);call(pose,"scale",scale,scale,1f);
            text(g,font,names[i],0,0,regions[i]==selected?0xffd2c294:0xffe8e5ce);
            int lineY=10;for(String s:lines){int color=s.equals("Sin lesiones")||s.startsWith("Vendaje")||s.startsWith("Cicatrizando")?0xffa8be9d:0xffe98780;text(g,font,s,0,lineY,color);lineY+=9;}
            call(pose,"popPose");
        }
        return bottom-5;
    }
    public static boolean gridClick(Object screen,double x,double y,int button){
        if(number(screen,"tab")!=2)return false;Object l=get(screen,"layout");if(l==null)return false;
        int left=number(l,"rightX"),top=number(l,"contentTop")+18,width=number(l,"rightWidth"),rowH=Math.max(8,(number(l,"contentBottom")-number(l,"contentTop")-20)/4);
        if(x<left||x>=left+width||y<top||y>=top+4*rowH)return false;
        int col=x<left+width/2?0:1,row=(int)((y-top)/rowH);Object region=screenParts()[row*2+col];set(screen,"selected",region);
        if(button==1)privateCall(screen,"applyHealing",region);return true;
    }
    public static boolean gridScroll(Object screen){if(number(screen,"tab")==2){set(screen,"scroll",0);return true;}return false;}
    public static String treatmentLabel(Object screen){
        Object region=get(screen,"selected");
        ClinicalState.Wound wound=state(privateCall(screen,"live")).wounds[((Enum<?>)region).ordinal()];
        if(wound.bandaged)return wound.dirty||wound.infected?"Retirar vendaje sucio":"Retirar vendaje";
        Object hand=privateCall(screen,"treatmentHand",region);
        if(hand==null)return "Equipa un objeto de curación";
        Object p=get(mc(),"player"),stack=call(p,"getItemInHand",hand);
        if((Boolean)stat(FA+"survival.ClinicalHooks","extraction",stack))return "Extraer bala · 4 s";
        return "Tratar parte · 3,3 s";
    }
    static boolean bleeding(Object p,Object region){return p!=null&&(Boolean)stat(FA+"fracture.Bleeding","isBleeding",p,region);}
    static void marker(Object g,int cx,int cy,int size,ClinicalState.Wound w,boolean blood){
        int d=Math.max(1,size);
        if(w.bandaged){fill(g,cx-3*d,cy-3*d,cx+3*d,cy-2*d,w.dirty?0xff9d7650:0xffe6dfb9);fill(g,cx-3*d,cy-5*d,cx+3*d,cy-4*d,w.dirty?0xff9d7650:0xffe6dfb9);}
        if(w.cut&&!w.bandaged){fill(g,cx-3*d,cy+2*d,cx-d,cy+3*d,0xffdd7777);fill(g,cx-2*d,cy+d,cx,cy+2*d,0xffdd7777);}
        if(w.infected){fill(g,cx-3*d,cy-6*d,cx-d,cy-5*d,0xffb58b3a);}
        if(w.bullets>0){fill(g,cx+d,cy-d,cx+3*d,cy+3*d,0xffd99542);fill(g,cx+d,cy-2*d,cx+2*d,cy-d,0xffffd884);fill(g,cx+d,cy+2*d,cx+3*d,cy+3*d,0xff604025);}
        if(w.bitten){fill(g,cx-3*d,cy-d,cx-d,cy+d,0xffec7373);fill(g,cx-3*d,cy+d,cx-2*d,cy+2*d,0xffec7373);fill(g,cx-2*d,cy-2*d,cx-d,cy-d,0xffec7373);}
        if(blood){fill(g,cx-d,cy+3*d,cx+d,cy+5*d,0xffcf303c);fill(g,cx,cy+2*d,cx+d,cy+3*d,0xffcf303c);}
    }
    public static void menuMarkers(Object screen,Object g,Object model){
        if(number(screen,"tab")!=2)return;Object[] regions=screenParts();int[][][] polygons=(int[][][])get(type(FA+"client.gui.SurvivalLayout"),"BODY");
        double scale=((Number)get(screen,"bodyScale")).doubleValue(),x=((Number)get(screen,"bodyX")).doubleValue(),y=((Number)get(screen,"bodyY")).doubleValue();
        Object player=get(mc(),"player");ClinicalState s=state(model);
        for(int i=0;i<8;i++){double cx=0,cy=0;for(int[] point:polygons[i]){cx+=point[0];cy+=point[1];}cx/=polygons[i].length;cy/=polygons[i].length;
            marker(g,(int)(x+cx*scale),(int)(y+cy*scale),Math.max(1,(int)Math.round(scale)),s.wounds[((Enum<?>)regions[i]).ordinal()],bleeding(player,regions[i]));}
    }
    public static void hudMarkers(int x,int y,Object model,Object g){
        String[] names={"HEAD","BODY","LEFT_ARM","RIGHT_ARM","LEFT_LEG","RIGHT_LEG","LEFT_FOOT","RIGHT_FOOT"};
        int[][] centers={{16,8},{16,28},{4,28},{28,28},{12,48},{20,48},{12,60},{20,60}};ClinicalState s=state(model);Object p=get(mc(),"player");
        for(int i=0;i<8;i++){Object region=get(type(FA+"api.enums.EnumPlayerPart"),names[i]);marker(g,x+8+centers[i][0],y+8+centers[i][1],1,s.wounds[((Enum<?>)region).ordinal()],bleeding(p,region));}
    }
}
