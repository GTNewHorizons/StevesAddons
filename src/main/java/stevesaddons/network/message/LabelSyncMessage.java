package stevesaddons.network.message;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import stevesaddons.items.ItemLabeler;
import stevesaddons.registry.ItemRegistry;

public class LabelSyncMessage implements IMessage, IMessageHandler<LabelSyncMessage, IMessage> {

    List<String> save;
    String text;

    public LabelSyncMessage() {}

    public LabelSyncMessage(List<String> save, String text) {
        this.save = save;
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readShort();
        save = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            save.add(ByteBufUtils.readUTF8String(buf));
        }
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(save.size());
        for (String s : save) {
            ByteBufUtils.writeUTF8String(buf, s);
        }
        ByteBufUtils.writeUTF8String(buf, text);
    }

    @Override
    public IMessage onMessage(LabelSyncMessage message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player != null) {
            ItemStack current = player.inventory.getCurrentItem();
            if (current != null && current.getItem() == ItemRegistry.labeler) {
                ItemLabeler.saveStrings(current, message.save);
                ItemLabeler.setLabel(current, message.text);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, current);
            }
        }
        return null;
    }
}
