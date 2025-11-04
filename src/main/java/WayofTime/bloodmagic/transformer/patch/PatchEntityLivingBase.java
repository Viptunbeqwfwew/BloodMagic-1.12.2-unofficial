package WayofTime.bloodmagic.transformer.patch;

import WayofTime.bloodmagic.transformer.BasePatch;
import WayofTime.bloodmagic.transformer.LoaderTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/* Патчим поведения флага элитр.
* Патч travel предназначен для того что-бы не вставать каждый раз,
* как ударяемся о землю, ведь при зажатом пробеле,
* мы постоянно будем пытаться полететь. Это удобно. */
public class PatchEntityLivingBase extends BasePatch {
    public PatchEntityLivingBase() {
        super("net.minecraft.entity.EntityLivingBase");
    }
    @Override
    public void patch(ClassNode classNode) {
        LoaderTransformer.log.info("Patch Class " + this.getNameClass() + ".");
        for (MethodNode method: classNode.methods) {
            if (method.name.equals("updateElytra") || method.name.equals("func_184616_r")) {
                AbstractInsnNode first = method.instructions.getFirst();
                method.instructions.insertBefore(first, new VarInsnNode(25, 0));
                method.instructions.insertBefore(first, new MethodInsnNode(184, "WayofTime/bloodmagic/event/ElytraEvent", "updateElytraEventPre", "(Lnet/minecraft/entity/EntityLivingBase;)V", false));
                InsnList post = new InsnList();
                post.add(new VarInsnNode(25, 0));
                post.add(new MethodInsnNode(184, "WayofTime/bloodmagic/event/ElytraEvent", "updateElytraEventPost", "(Lnet/minecraft/entity/EntityLivingBase;)V", false));
                method.instructions.insert(method.instructions.getLast().getPrevious().getPrevious(), post);
                LoaderTransformer.log.info("Patch Method " + method.name + ".");
            } else if (method.name.equals("travel") || method.name.equals("func_191986_a")) {
                int c = method.instructions.size();
                for (int i = 0; i < c; i++) {
                    AbstractInsnNode inst = method.instructions.get(i);
                    if (!(inst instanceof MethodInsnNode)) continue;
                    MethodInsnNode methodInsnNode = (MethodInsnNode) inst;
                    if (!(methodInsnNode.name.equals("setFlag") || methodInsnNode.name.equals("func_70052_a"))) continue;
                    AbstractInsnNode arg1 = method.instructions.get(i - 2);
                    if (arg1.getOpcode() != Opcodes.BIPUSH) continue;
                    if ((((IntInsnNode) arg1).operand != 7)) continue;
                    LabelNode labelNode = null;
                    for (int j = i - 1; j >= 0; j--) {
                        AbstractInsnNode inst0 = method.instructions.get(j);
                        if (inst0 instanceof LabelNode) {
                            labelNode = (LabelNode) inst0;
                            break;
                        }
                    }
                    if (labelNode == null) continue;
                    InsnList newInsnList = new InsnList();
                    newInsnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "WayofTime/bloodmagic/event/ElytraEvent", "isEndActive", "Z"));
                    LabelNode labelElse43 = null;
                    for (int j = i + 1; j < c; j++) {
                        AbstractInsnNode inst0 = method.instructions.get(j);
                        if (inst0 instanceof LabelNode) {
                            labelElse43 = (LabelNode) inst0;
                            break;
                        }
                    }
                    if (labelElse43 == null) break;
                    newInsnList.add(new JumpInsnNode(Opcodes.IFNE, labelElse43));
                    method.instructions.insertBefore(labelNode, newInsnList);
                    LoaderTransformer.log.info("Patch Method " + method.name + ".");
                    break;
                }
            }
        }
    }
}
