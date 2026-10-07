import org.objectweb.asm.*;import org.objectweb.asm.tree.*;import java.util.*;
public class PatchV34 extends PatchV33 {
 static final String FA="work/v34/fa";
 static void simple(ClassNode c,String name,String desc,int access,String helper,String helperDesc,int...vars){MethodNode m=new MethodNode(access,name,desc,null,null);m.instructions.add(load(vars));m.instructions.add(hook(helper,helperDesc));Type ret=Type.getReturnType(desc);if(ret.getSort()==Type.OBJECT)m.instructions.add(new TypeInsnNode(CHECKCAST,ret.getInternalName()));m.instructions.add(new InsnNode(ret.getOpcode(IRETURN)));c.methods.add(m);}
 static void event(ClassNode c,String name,String cls,String helper){simple(c,name,"(L"+cls+";)V",ACC_PUBLIC|ACC_STATIC,helper,"(Ljava/lang/Object;)V",0);method(c,name).visibleAnnotations=new ArrayList<>(List.of(new AnnotationNode("Lnet/neoforged/bus/api/SubscribeEvent;")));}
 public static void main(String[]a)throws Exception{
  ClassNode c=read(FA,F+"compat/lrtactical/ClothDressingItem");c.superName=F+"api/healing/ItemHealing";
  MethodNode init=method(c,"<init>");for(AbstractInsnNode n:init.instructions.toArray())if(n instanceof MethodInsnNode mi&&mi.name.equals("<init>")){InsnList p=new InsnList();p.add(new InsnNode(ACONST_NULL));p.add(new InsnNode(ACONST_NULL));init.instructions.insertBefore(n,p);mi.owner=c.superName;mi.desc="(Lnet/minecraft/world/item/Item$Properties;Ljava/util/function/Function;Ljava/util/function/Function;)V";}
  MethodNode use=method(c,"use");reset(use);use.instructions.add(load(0,1,2,3));use.instructions.add(hook("clothUse","(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));use.instructions.add(new TypeInsnNode(CHECKCAST,"net/minecraft/world/InteractionResultHolder"));use.instructions.add(new InsnNode(ARETURN));
  simple(c,"createNewHealer","(Lnet/minecraft/world/item/ItemStack;)L"+F+"api/damagesystem/AbstractPartHealer;",ACC_PUBLIC,"bandageHealer","(Ljava/lang/Object;)Ljava/lang/Object;",1);
  MethodNode time=new MethodNode(ACC_PUBLIC,"getApplyTime","(Lnet/minecraft/world/item/ItemStack;)I",null,null);time.instructions.add(new IntInsnNode(SIPUSH,3300));time.instructions.add(new InsnNode(IRETURN));c.methods.add(time);
  for(String name:List.of("getApplySoundEvent","getApplySoundMode")){
   String ret=name.equals("getApplySoundEvent")?"net/minecraft/sounds/SoundEvent":F+"api/healing/ItemHealing$ApplySoundMode";
   MethodNode m=new MethodNode(ACC_PUBLIC,name,"(Lnet/minecraft/world/item/ItemStack;)L"+ret+";",null,null);m.instructions.add(hook("bandageItem","()Ljava/lang/Object;"));m.instructions.add(new TypeInsnNode(CHECKCAST,F+"api/healing/ItemHealing"));m.instructions.add(load(1));m.instructions.add(new MethodInsnNode(INVOKEVIRTUAL,F+"api/healing/ItemHealing",name,m.desc,false));m.instructions.add(new InsnNode(ARETURN));c.methods.add(m);
  }write(FA,c);
  c=read(FA,F+"common/RegistryObjects");appendReturnHook(method(c,"<clinit>"),()->{InsnList k=new InsnList();k.add(new FieldInsnNode(GETSTATIC,F+"common/RegistryObjects","ITEM_REGISTER","Lnet/neoforged/neoforge/registries/DeferredRegister$Items;"));k.add(hook("registerDirty","(Ljava/lang/Object;)V"));return k;});write(FA,c);
  c=read(FA,F+"compat/lrtactical/MedicalBridge");MethodNode d=method(c,"dressing");reset(d);d.instructions.add(load(0));d.instructions.add(hook("dressing","(Ljava/lang/Object;)Z"));d.instructions.add(new InsnNode(IRETURN));MethodNode dt=method(c,"dressingTicks");reset(dt);dt.instructions.add(new IntInsnNode(BIPUSH,66));dt.instructions.add(new InsnNode(IRETURN));write(FA,c);
  c=read(FA,F+"api/healing/ItemHealing");method(c,"onTreatmentStarted").instructions.insert(load(1));method(c,"onTreatmentStarted").instructions.insert(method(c,"onTreatmentStarted").instructions.getFirst(),hook("ordinaryApplied","(Ljava/lang/Object;)V"));write(FA,c);
  c=read(FA,F+"survival/ClinicalHooks");MethodNode msg=method(c,"lambda$2");InsnList pre=load(1,0);pre.add(hook("removeMessage","(Ljava/lang/Object;Ljava/lang/Object;)Z"));LabelNode next=new LabelNode();pre.add(new JumpInsnNode(IFEQ,next));pre.add(new InsnNode(RETURN));pre.add(next);pre.add(new FrameNode(F_SAME,0,null,0,null));msg.instructions.insert(pre);
  MethodNode tick=method(c,"tick");int completion=0;
  for(AbstractInsnNode n:tick.instructions.toArray())if(n instanceof MethodInsnNode mi&&mi.owner.equals(F+"survival/ClinicalState")&&mi.name.equals("tickDressing")){
    AbstractInsnNode jump=n.getNext();if(!(jump instanceof JumpInsnNode))throw new IllegalStateException();InsnList k=load(0,2);k.add(new VarInsnNode(ILOAD,8));k.add(load(10)); // inspect actual local slots before run!
    k.add(hook("applied","(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/Object;)V"));tick.instructions.insert(jump,k);completion++;
  }
  if(completion!=1)throw new IllegalStateException("completion count");appendReturnHook(tick,()->{InsnList k=load(0,1);k.add(hook("tickHygiene","(Ljava/lang/Object;Ljava/lang/Object;)V"));return k;});
  for(AbstractInsnNode n:method(c,"heal").instructions.toArray())if(n instanceof MethodInsnNode mi&&mi.owner.equals(W)&&mi.name.equals("active")){mi.setOpcode(INVOKESTATIC);mi.owner=H;mi.name="blocksExternalHealing";mi.desc="(Ljava/lang/Object;)Z";}
  MethodNode nativeRecord=method(c,"record");nativeRecord.name="v34NativeRecord";
  MethodNode record=new MethodNode(ACC_PUBLIC|ACC_STATIC,"record",nativeRecord.desc,null,null);InsnList k=record.instructions;k.add(load(2));k.add(hook("zombieSource","(Ljava/lang/Object;)Z"));LabelNode fallback=new LabelNode();k.add(new JumpInsnNode(IFEQ,fallback));k.add(load(0,1,2,3));k.add(hook("recordBite","(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"));k.add(new InsnNode(RETURN));k.add(fallback);k.add(new FrameNode(F_SAME,0,null,0,null));k.add(load(0,1,2,3));k.add(new MethodInsnNode(INVOKESTATIC,c.name,nativeRecord.name,nativeRecord.desc,false));k.add(new InsnNode(RETURN));c.methods.add(record);write(FA,c);
  c=read(FA,F+"client/gui/GuiHealthScreen");MethodNode hand=method(c,"treatmentHand");hand.name="v34NativeTreatmentHand";simple(c,"treatmentHand",hand.desc,ACC_PRIVATE,"treatmentHand","(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",0,1);
  for(AbstractInsnNode n:method(c,"applyHealing").instructions.toArray())if(n instanceof MethodInsnNode mi&&mi.owner.equals(W)&&mi.name.equals("active")){mi.setOpcode(INVOKESTATIC);mi.owner=H;mi.name="isClinical";mi.desc="(Ljava/lang/Object;)Z";}write(FA,c);
  c=read(FA,F+"client/ClientEventHandler");event(c,"keepSlownessFov","net/neoforged/neoforge/client/event/ComputeFovModifierEvent","fov");write(FA,c);
  System.out.println("v3.4: cloth bandage parity, dirty item registration, removal, washing, persistent hygiene, mild cuts and FOV.");
 }
}
