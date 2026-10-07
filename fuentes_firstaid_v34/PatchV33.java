import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.nio.file.*;
import java.util.*;
public class PatchV33 implements Opcodes {
 static final String F="ichttt/mods/firstaid/",H=F+"compat/lrtactical/TreatmentV33",W=F+"survival/ClinicalState$Wound";
 static ClassNode read(String root,String name)throws Exception{ClassNode c=new ClassNode();new ClassReader(Files.readAllBytes(Path.of(root,name+".class"))).accept(c,0);return c;}
 static MethodNode method(ClassNode c,String name){return c.methods.stream().filter(m->m.name.equals(name)).findFirst().orElseThrow();}
 static void write(String root,ClassNode c)throws Exception{ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);c.accept(w);Files.write(Path.of(root,c.name+".class"),w.toByteArray());}
 static MethodInsnNode hook(String n,String d){return new MethodInsnNode(INVOKESTATIC,H,n,d,false);}
 static void reset(MethodNode m){m.instructions.clear();m.tryCatchBlocks.clear();if(m.localVariables!=null)m.localVariables.clear();}
 static InsnList load(int...vars){InsnList i=new InsnList();for(int v:vars)i.add(new VarInsnNode(ALOAD,v));return i;}
 static void appendReturnHook(MethodNode m,InsnListMaker supplier){for(AbstractInsnNode i:m.instructions.toArray())if(i.getOpcode()==RETURN)m.instructions.insertBefore(i,supplier.make());}
 interface InsnListMaker{InsnList make();}
 static void wrapBoolPrefix(ClassNode c,String name,String helper,InsnList args){
  MethodNode original=method(c,name);original.name="v33Native"+name;MethodNode m=new MethodNode(original.access,name,original.desc,null,null);InsnList i=m.instructions;
  i.add(args);i.add(hook(helper,"(Ljava/lang/Object;)Z"));LabelNode fallback=new LabelNode();i.add(new JumpInsnNode(IFEQ,fallback));i.add(new InsnNode(ICONST_1));i.add(new InsnNode(IRETURN));i.add(fallback);i.add(new FrameNode(F_SAME,0,null,0,null));
  i.add(new VarInsnNode(ALOAD,0));int index=1;for(Type t:Type.getArgumentTypes(original.desc)){i.add(new VarInsnNode(t.getOpcode(ILOAD),index));index+=t.getSize();}
  i.add(new MethodInsnNode(INVOKESPECIAL,c.name,original.name,original.desc,false));i.add(new InsnNode(IRETURN));c.methods.add(m);
 }
 public static void main(String[]a)throws Exception {
  String fa="work/v33/fa",hordes="work/v33/hordes";ClassNode c=read(fa,F+"survival/ClinicalHooks");
  MethodNode record=method(c,"record");InsnList bite=load(0,1,2,3);bite.add(hook("recordBite","(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"));record.instructions.insert(bite);
  MethodNode message=method(c,"lambda$2");int removed=0;
  for(AbstractInsnNode ins:message.instructions.toArray())if(ins instanceof LdcInsnNode ldc&&"Primero extrae las balas alojadas con tijeras.".equals(ldc.cst)){
   AbstractInsnNode jump=ins.getPrevious().getPrevious();if(!(jump instanceof JumpInsnNode j))throw new IllegalStateException("bullet guard");
   AbstractInsnNode begin=jump;for(int n=0;n<5;n++)begin=begin.getPrevious();AbstractInsnNode stop=j.label;
   for(AbstractInsnNode k=begin;k!=stop;){AbstractInsnNode next=k.getNext();message.instructions.remove(k);k=next;}removed++;
  }
  if(removed!=1)throw new IllegalStateException("Expected one bullet dressing guard");
  int recovery=0;
  for(MethodNode m:c.methods)for(AbstractInsnNode ins:m.instructions.toArray()){
   if(ins instanceof MethodInsnNode mi&&mi.owner.equals(F+"compat/lrtactical/MedicalBridge")&&mi.name.equals("begin"))mi.owner=H;
   if(m.name.equals("tick")&&ins instanceof MethodInsnNode mi&&mi.owner.equals(F+"survival/ClinicalHooks")&&mi.name.equals("extraction")){
    m.instructions.insertBefore(ins,new VarInsnNode(ALOAD,2));mi.owner=H;mi.name="validExtraction";mi.desc="(Ljava/lang/Object;Ljava/lang/Object;)Z";
   }
   if(m.name.equals("tick")&&ins instanceof FieldInsnNode field&&field.owner.equals(W)&&field.name.equals("bullets")){
    AbstractInsnNode next=ins.getNext();if(!(next instanceof JumpInsnNode jump)||jump.getOpcode()!=IFNE)continue;
    m.instructions.set(ins,hook("recoveryAllowed","(L"+W+";)Z"));jump.setOpcode(IFEQ);recovery++;
   }
   if(ins instanceof LdcInsnNode ldc&&ldc.cst instanceof String str){
    if(str.equals("Esta herida requiere tijeras para extraer la bala y después una venda o tirita."))ldc.cst="Equipa una venda, tirita o tela; las tijeras permiten extraer la bala.";
    if(str.equals("Herida vendada. La recuperación será gradual."))ldc.cst="Vendaje aplicado. Con bala alojada hay un 50 % de posibilidad de recuperación.";
   }
  }
  if(recovery!=1)throw new IllegalStateException("recovery guard count");
  appendReturnHook(method(c,"tick"),()->{InsnList i=load(0,1);i.add(hook("finishServer","(Ljava/lang/Object;Ljava/lang/Object;)V"));return i;});write(fa,c);
  c=read(fa,F+"survival/ClinicalData");MethodNode save=method(c,"write");
  for(AbstractInsnNode ins:save.instructions.toArray())if(ins.getOpcode()==ARETURN){InsnList i=load(0,1);i.add(hook("writeExtra","(Ljava/lang/Object;Ljava/lang/Object;)V"));save.instructions.insertBefore(ins,i);}
  appendReturnHook(method(c,"read"),()->{InsnList i=load(0,1);i.add(hook("readExtra","(Ljava/lang/Object;Ljava/lang/Object;)V"));return i;});write(fa,c);
  c=read(fa,F+"common/damagesystem/PlayerDamageModel");
  for(String name:List.of("getPainLevel","calculateChronicIntensity")){
   MethodNode m=method(c,name);int op=name.equals("getPainLevel")?IRETURN:FRETURN;
   for(AbstractInsnNode ins:m.instructions.toArray())if(ins.getOpcode()==op){InsnList i=new InsnList();i.add(new VarInsnNode(ALOAD,0));i.add(new InsnNode(SWAP));i.add(hook(name.equals("getPainLevel")?"painFloor":"intensityFloor",name.equals("getPainLevel")?"(Ljava/lang/Object;I)I":"(Ljava/lang/Object;F)F"));m.instructions.insertBefore(ins,i);}
  }write(fa,c);
  c=read(fa,F+"client/gui/GuiHealthScreen");MethodNode health=method(c,"health");reset(health);InsnList i=health.instructions;i.add(load(0,1,2));i.add(new VarInsnNode(ILOAD,3));i.add(new VarInsnNode(ILOAD,4));i.add(hook("grid","(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;II)I"));i.add(new InsnNode(IRETURN));
  MethodNode injuries=method(c,"injuries");injuries.name="v33NativeInjuries";MethodNode injuryWrapper=new MethodNode(ACC_PRIVATE,"injuries",injuries.desc,null,null);i=injuryWrapper.instructions;i.add(load(0,1,0,1));i.add(new MethodInsnNode(INVOKESPECIAL,c.name,injuries.name,injuries.desc,false));i.add(hook("extraInjuries","(Ljava/lang/Object;Ljava/lang/Object;Ljava/util/List;)Ljava/util/List;"));i.add(new InsnNode(ARETURN));c.methods.add(injuryWrapper);
  MethodNode label=method(c,"treatmentLabel");reset(label);label.instructions.add(load(0));label.instructions.add(hook("treatmentLabel","(Ljava/lang/Object;)Ljava/lang/String;"));label.instructions.add(new InsnNode(ARETURN));
  for(AbstractInsnNode ins:method(c,"applyHealing").instructions.toArray())if(ins instanceof MethodInsnNode mi&&mi.owner.equals(F+"compat/lrtactical/MedicalBridge")&&mi.name.equals("clientBegin"))mi.owner=H;
  appendReturnHook(method(c,"drawBody"),()->{InsnList k=load(0,1,2);k.add(hook("menuMarkers","(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"));return k;});
  for(AbstractInsnNode ins:method(c,"drawBody").instructions.toArray())if(ins instanceof FieldInsnNode fld&&fld.owner.equals(W)&&fld.name.equals("bullets")){InsnList k=new InsnList();k.add(new InsnNode(POP));k.add(new InsnNode(ICONST_0));method(c,"drawBody").instructions.insertBefore(ins,k);method(c,"drawBody").instructions.remove(ins);}
  MethodNode oldClick=method(c,"mouseClicked");oldClick.name="v33NativeMouseClicked";MethodNode click=new MethodNode(ACC_PUBLIC,"mouseClicked",oldClick.desc,null,null);i=click.instructions;i.add(load(0));i.add(new VarInsnNode(DLOAD,1));i.add(new VarInsnNode(DLOAD,3));i.add(new VarInsnNode(ILOAD,5));i.add(hook("gridClick","(Ljava/lang/Object;DDI)Z"));LabelNode normal=new LabelNode();i.add(new JumpInsnNode(IFEQ,normal));i.add(new InsnNode(ICONST_1));i.add(new InsnNode(IRETURN));i.add(normal);i.add(new FrameNode(F_SAME,0,null,0,null));i.add(load(0));i.add(new VarInsnNode(DLOAD,1));i.add(new VarInsnNode(DLOAD,3));i.add(new VarInsnNode(ILOAD,5));i.add(new MethodInsnNode(INVOKESPECIAL,c.name,oldClick.name,oldClick.desc,false));i.add(new InsnNode(IRETURN));c.methods.add(click);
  wrapBoolPrefix(c,"mouseScrolled","gridScroll",load(0));write(fa,c);
  c=read(fa,F+"client/util/PlayerModelRenderer");appendReturnHook(method(c,"renderPlayerHealth"),()->{InsnList k=new InsnList();k.add(new VarInsnNode(ILOAD,0));k.add(new VarInsnNode(ILOAD,1));k.add(load(2,4));k.add(hook("hudMarkers","(IILjava/lang/Object;Ljava/lang/Object;)V"));return k;});write(fa,c);
  c=read(fa,F+"client/ClientEventHandler");MethodNode nativeSelect=method(c,"selectPendingHealing");nativeSelect.name="v33NativeSelectPendingHealing";MethodNode select=new MethodNode(ACC_PUBLIC|ACC_STATIC,"selectPendingHealing",nativeSelect.desc,null,null);i=select.instructions;i.add(load(0,1));i.add(new MethodInsnNode(INVOKESTATIC,c.name,nativeSelect.name,nativeSelect.desc,false));LabelNode no=new LabelNode();i.add(new JumpInsnNode(IFEQ,no));i.add(load(0,1));i.add(hook("autoSelected","(Ljava/lang/Object;Ljava/lang/Object;)V"));i.add(new InsnNode(ICONST_1));i.add(new InsnNode(IRETURN));i.add(no);i.add(new FrameNode(F_SAME,0,null,0,null));i.add(new InsnNode(ICONST_0));i.add(new InsnNode(IRETURN));c.methods.add(select);
  MethodNode held=method(c,"isHealingUseHeld");reset(held);held.instructions.add(load(0));held.instructions.add(hook("isHealingHeld","(Ljava/lang/Object;)Z"));held.instructions.add(new InsnNode(IRETURN));
  method(c,"clientTick").instructions.insert(hook("clientTick","()V"));
  MethodNode mouse=new MethodNode(ACC_PUBLIC|ACC_STATIC,"cancelAutomaticTreatmentOnClick","(Lnet/neoforged/neoforge/client/event/InputEvent$MouseButton$Pre;)V",null,null);mouse.visibleAnnotations=new ArrayList<>();mouse.visibleAnnotations.add(new AnnotationNode("Lnet/neoforged/bus/api/SubscribeEvent;"));mouse.instructions.add(load(0));mouse.instructions.add(hook("mouseCancel","(Ljava/lang/Object;)V"));mouse.instructions.add(new InsnNode(RETURN));c.methods.add(mouse);write(fa,c);
  c=read(hordes,"net/smileycorp/hordes/infection/InfectionEventHandler");MethodNode damage=method(c,"onDamage");damage.name="v33NativeOnDamage";MethodNode wrapper=new MethodNode(ACC_PUBLIC,"onDamage",damage.desc,null,null);wrapper.visibleAnnotations=damage.visibleAnnotations;damage.visibleAnnotations=null;i=wrapper.instructions;i.add(load(1));i.add(hook("managedZombieDamage","(Ljava/lang/Object;)Z"));LabelNode orig=new LabelNode();i.add(new JumpInsnNode(IFEQ,orig));i.add(new InsnNode(RETURN));i.add(orig);i.add(new FrameNode(F_SAME,0,null,0,null));i.add(load(0,1));i.add(new MethodInsnNode(INVOKEVIRTUAL,c.name,damage.name,damage.desc,false));i.add(new InsnNode(RETURN));c.methods.add(wrapper);write(hordes,c);
  System.out.println("Patched clinical rules, state persistence, two-column GUI, markers, automatic treatment/cancel, and native Hordes infection guard.");
 }
}
