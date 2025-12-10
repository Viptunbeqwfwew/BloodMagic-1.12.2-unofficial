package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.tile.TileIncenseAltar;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class DataProviderIncenseAltar implements IWailaDataProvider {
    public static final IWailaDataProvider INSTANCE = new DataProviderIncenseAltar();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_INCENSE_ALTAR))
            return currenttip;

        if (accessor.getNBTData().hasKey("altar")) {
            NBTTagCompound altarData = accessor.getNBTData().getCompoundTag("altar");
            currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentTranquility", altarData.getInteger("tranquility")));
            currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentBonus", altarData.getInteger("incenseAddition")));
        }

        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileIncenseAltar altar = (TileIncenseAltar) te;

        boolean hasSigil = false;
        boolean hasSeer = false;

        switch (ConfigHandler.compat.wailaAltarDisplayMode) {
            case ALWAYS: {
                hasSigil = hasSeer = true;
                break;
            }
            case SIGIL_HELD: {
                hasSeer = DataProviderBloodAltar.holdingSigil(player, RegistrarBloodMagicItems.SIGIL_SEER);
                hasSigil = hasSeer || DataProviderBloodAltar.holdingSigil(player, RegistrarBloodMagicItems.SIGIL_DIVINATION);
                break;
            }
            case SIGIL_CONTAINED: {
                hasSeer = DataProviderBloodAltar.hasStack(new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER), player);
                hasSigil = hasSeer || DataProviderBloodAltar.hasStack(new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION), player);
                break;
            }
        }

        if (!hasSeer && !hasSigil)
            return tag;

        NBTTagCompound altarData = new NBTTagCompound();
        altarData.setInteger("tranquility", (int) ((100D * (int) (100 * altar.tranquility)) / 100D));
        altarData.setInteger("incenseAddition", (int) (100 * altar.incenseAddition));

        tag.setTag("altar", altarData);

        return tag;
    }
}
