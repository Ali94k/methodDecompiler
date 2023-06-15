package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;

public class Main {
//    public static void main(String[] args) throws IOException {
//        ClassVisitor v= new ClassVisitor(Opcodes.ASM9) {
//
//            @Override
//            public void visit(int version, int access, String name,
//                              String signature, String superName, String[] interfaces) {
//                System.out.println(" class: "+name);
//                System.out.println("parent  class: "+superName);
//                super.visit(version, access, name, signature, superName, interfaces);
//            }
//
//            @Override
//            public MethodVisitor visitMethod(int i, String method, String desc, String signature, String[] strings) {
//                System.out.println("method name/block= " + method);
//                System.out.println("descriptor   = " + desc);
//                System.out.println("signature  = " + signature);
//                System.out.println("declared exceptions=" + Arrays.toString(strings));
//                return super.visitMethod(i, method, desc, signature, strings);
//            }
//        } ;
//
//        ClassReader classReader=new ClassReader("org.example.example.PublicFunctional");
//        classReader.accept(v, 0);
//    }

    public static void main(String[] args) throws IOException {
        Class<Opcodes> opcodesClass = Opcodes.class;
        Field[] fields = opcodesClass.getDeclaredFields();

        Map<Integer, String> opcodeDictionary = new HashMap<>();

        for (Field field : fields) {
            if (field.getType() == int.class) {
                try {
                    int value = field.getInt(null);
                    String name = field.getName();
                    opcodeDictionary.put(value, name);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

//        for (Map.Entry<Integer, String> entry : opcodeDictionary.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }

        var pClassName = "org.example.example.PublicFunctional";
        var pMethodName = "bar:()I";

        var mName = pMethodName.split(":")[0];
        var mDesc = pMethodName.split(":")[1];

        ClassReader cr = new ClassReader(pClassName);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);

        var mNode = classNode.methods.stream().filter(m -> m.name.equals(mName) && m.desc.equals(mDesc)).findFirst().orElseThrow();

        for(var i = 0; i < mNode.instructions.size(); i++) {
            var instruction = mNode.instructions.get(i);

            // print out the opcode and the corresponding name, format so that names are aligned
            System.out.printf("%-5s %s\n", instruction.getOpcode(), opcodeDictionary.get(instruction.getOpcode()));
        }
    }
}
