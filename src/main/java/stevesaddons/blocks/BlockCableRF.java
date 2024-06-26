package stevesaddons.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import stevesaddons.StevesAddons;
import stevesaddons.reference.Names;
import stevesaddons.tileentities.TileEntityRFNode;
import vswe.stevesfactory.blocks.ModBlocks;

public class BlockCableRF extends BlockContainer {

    private IIcon[] icons;

    public BlockCableRF() {
        super(Material.iron);
        this.setCreativeTab(ModBlocks.creativeTab);
        this.setStepSound(soundTypeMetal);
        this.setBlockName(Names.CABLE_RF);
        this.setHardness(1.2F);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityRFNode();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registry) {
        icons = new IIcon[4];
        for (int i = 0; i < 4; i++) icons[i] = registry.registerIcon(StevesAddons.ID + ":" + Names.CABLE_RF + "_" + i);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icons[3];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
        TileEntity te = iBlockAccess.getTileEntity(x, y, z);
        if (te instanceof TileEntityRFNode) return getIcon((TileEntityRFNode) te, side);
        return icons[3];
    }

    @SideOnly(Side.CLIENT)
    private IIcon getIcon(TileEntityRFNode te, int side) {
        boolean in = te.isInput(side);
        boolean out = te.isOutput(side);
        if (in) {
            if (out) return icons[3];
            return icons[2];
        } else {
            if (out) return icons[1];
            return icons[0];
        }
    }
}
