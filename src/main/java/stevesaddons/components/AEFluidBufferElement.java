package stevesaddons.components;

import appeng.api.storage.data.IAEFluidStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import stevesaddons.helpers.AEHelper;
import stevesaddons.tileentities.TileEntityAENode;
import vswe.stevesfactory.components.StackTankHolder;

public class AEFluidBufferElement extends StackTankHolder {
    private IAEFluidStack fluid;
    private TileEntityAENode node;
    private int sizeLeft;

    public AEFluidBufferElement(IAEFluidStack fluid, TileEntityAENode node) {
        super(null, null, null);
        this.fluid = fluid;
        this.node = node;
        this.sizeLeft = (int) fluid.getStackSize();
    }

    @Override
    public FluidStack getFluidStack() {
        return fluid.getFluidStack();
    }

    @Override
    public IFluidHandler getTank() {
        return node.getTank();
    }

    @Override
    public ForgeDirection getSide() {
        return ForgeDirection.UNKNOWN;
    }

    @Override
    public void reduceAmount(int val) {
        AEHelper.extract(node.getNode(), fluid.copy().setStackSize(val), node);
    }

    @Override
    public int getSizeLeft() {
        return (int) Math.min(this.fluid.getStackSize(), this.sizeLeft);
    }

    @Override
    public StackTankHolder getSplitElement(int elementAmount, int id, boolean fair) {
        AEFluidBufferElement element = new AEFluidBufferElement(this.fluid, this.node);
        int oldAmount = this.getSizeLeft();
        int amount = oldAmount / elementAmount;
        if (!fair) {
            int amountLeft = oldAmount % elementAmount;
            if (id < amountLeft) {
                ++amount;
            }
        }
        element.sizeLeft = amount;
        return element;
    }
}
