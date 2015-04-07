package stevesaddons.components;

import appeng.api.storage.data.IAEItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import stevesaddons.helpers.AEHelper;
import stevesaddons.reference.Null;
import stevesaddons.tileentities.TileEntityAENode;
import vswe.stevesfactory.components.SlotStackInventoryHolder;

public class AEItemBufferElement extends SlotStackInventoryHolder
{
    private IAEItemStack item;
    private TileEntityAENode inventory;
    private int sizeLeft;

    public AEItemBufferElement(IAEItemStack item, TileEntityAENode node)
    {
        super(Null.NULL_STACK, null, 0);
        this.item = item;
        this.inventory = node;
        this.sizeLeft = (int)item.getStackSize();
    }

    @Override
    public ItemStack getItemStack() {
        return item.getItemStack();
    }

    @Override
    public IInventory getInventory() {
        return null;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public void remove() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public int getSizeLeft() {
        return (int)Math.min(item.getStackSize(), sizeLeft);
    }

    public void reduceAmount(int val) {
        AEHelper.extract(inventory.getNode(), item.copy().setStackSize(val), inventory);
    }

    public SlotStackInventoryHolder getSplitElement(int elementAmount, int id, boolean fair) {
        AEItemBufferElement element = new AEItemBufferElement(this.item, this.inventory);
        int oldAmount = this.getSizeLeft();
        int amount = oldAmount / elementAmount;
        if(!fair) {
            int amountLeft = oldAmount % elementAmount;
            if(id < amountLeft) {
                ++amount;
            }
        }
        element.sizeLeft = amount;
        return element;
    }
}
