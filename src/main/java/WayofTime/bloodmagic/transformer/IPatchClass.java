package WayofTime.bloodmagic.transformer;

import org.objectweb.asm.tree.ClassNode;

/* Для каждого класса, своя реализация patch. */
public interface IPatchClass {
    String getNameClass();

    void patch(ClassNode classNode);
}
