import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import java.nio.file.*;
public class ValidateAll {
 public static void main(String[] args)throws Exception {
  int classes=0,methods=0;
  for(String root:args)try(var paths=Files.walk(Path.of(root))){
   for(Path p:paths.filter(p->p.toString().endsWith(".class")).toList()){
    ClassNode c=new ClassNode();new ClassReader(Files.readAllBytes(p)).accept(c,0);
    for(MethodNode m:c.methods)if((m.access&(Opcodes.ACC_ABSTRACT|Opcodes.ACC_NATIVE))==0){new Analyzer<>(new BasicVerifier()).analyze(c.name,m);methods++;}
    classes++;
   }
  }
  System.out.println("PASS: JVM operand/local data flow for "+classes+" edited classes, "+methods+" methods.");
 }
}
