package WayofTime.bloodmagic.network;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeElytra;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/* Отвечает за раскрытие элитр на серверной стороне от имени мода.
* Раскрытие требуется, для корректной обработки коллизии на серверной стороне. */
public class LivingArmorElytraProccessor implements IMessage, IMessageHandler<LivingArmorElytraProccessor, IMessage> {
    boolean use;

    public LivingArmorElytraProccessor() {}
    public LivingArmorElytraProccessor(boolean use) {
        this.use = use;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        use = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(use);
    }

    @Override
    public IMessage onMessage(LivingArmorElytraProccessor message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            message.onMessageFromClient(ctx.getServerHandler().player);
        }
        return null;
    }

    public void onMessageFromClient(EntityPlayerMP player) {
        if (!player.isElytraFlying() && player.motionY < 0.0D && !player.capabilities.isFlying && !player.isInWater() && use) {
            ItemStack itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST && LivingArmour.hasFullSet(player) && ItemLivingArmour.getUpgradeFromNBT(BloodMagic.MODID + ".upgrade.elytra", itemstack) instanceof LivingArmourUpgradeElytra) {
                player.setElytraFlying();
            }
        } else
            if (player.isElytraFlying() && !use)
                player.clearElytraFlying();
    }
}
