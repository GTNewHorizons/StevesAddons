package stevesaddons.naming;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WailaLabelProvider implements IWailaDataProvider
{
    public static final List<String> empty = new ArrayList<String>();

    public static void register()
    {
        try{
            ModuleRegistrar.instance().registerBodyProvider(new WailaLabelProvider(), Block.class);
        }catch (Exception ignore){}
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return list;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        if (iWailaDataAccessor.getBlock()!=null)
        {
            BlockCoord coord = new BlockCoord(iWailaDataAccessor.getPosition().blockX, iWailaDataAccessor.getPosition().blockY, iWailaDataAccessor.getPosition().blockZ);
            String label = NameRegistry.getSavedName(iWailaDataAccessor.getWorld().provider.dimensionId, coord);
            if (label!=null)
            {
                list.add(StatCollector.translateToLocalFormatted("stevesaddons.waila.labelled",label));
            }
        }
        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, int i, int i1, int i2)
    {
        return null;
    }
}
