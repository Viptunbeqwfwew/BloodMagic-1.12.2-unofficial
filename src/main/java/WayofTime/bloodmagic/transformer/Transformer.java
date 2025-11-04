package WayofTime.bloodmagic.transformer;

import WayofTime.bloodmagic.transformer.patch.PatchEntityLivingBase;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import java.util.HashMap;

public class Transformer implements IClassTransformer {
    private final HashMap<String, IPatchClass> targets = new HashMap<>();

    public Transformer() {
        IPatchClass[] patchs = new IPatchClass[]{
                new PatchEntityLivingBase(),
        };

        for (IPatchClass patch : patchs) {
            targets.put(patch.getNameClass(), patch);
        }

        LoaderTransformer.log.info("Transformer init.");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(basicClass == null || !targets.containsKey(transformedName)) return basicClass;
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(node, 0);
        targets.get(transformedName).patch(node);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
