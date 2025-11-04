package WayofTime.bloodmagic.event;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/* Его использует трансформер.
* Предназначен для восстановления флага элитр,
* который постоянно сбрасывается проверкой ванильных элитр. */
public class ElytraEvent {
    static private boolean isActive = false;
    static public boolean isEndActive = false;

    static public void updateElytraEventPre(EntityLivingBase entity) {
        isActive = entity.getFlag(7);
        isEndActive = isActive;
    }

    public static void updateElytraEventPost(EntityLivingBase entity) {
        if (!isActive)
            return;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (LivingArmour.hasFullSet(player)) {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgradeFromNBT(BloodMagic.MODID + ".upgrade.elytra", chestStack);
                if (upgrade instanceof LivingArmourUpgradeElytra) {
                    boolean current_active = entity.getFlag(7);
                    if (!current_active) {
                        entity.setFlag(7, true);
                        isEndActive = true;
                    }
                }
            }
        }
    }
}
