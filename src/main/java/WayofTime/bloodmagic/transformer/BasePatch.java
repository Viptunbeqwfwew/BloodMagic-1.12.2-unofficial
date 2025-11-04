package WayofTime.bloodmagic.transformer;

import org.objectweb.asm.tree.ClassNode;

public abstract class BasePatch implements IPatchClass{
    private final String name;

    public BasePatch(String name) {
        this.name = name;
    }

    @Override
    public String getNameClass() {
        return name;
    }

    @Override
    public abstract void patch(ClassNode classNode);
}
