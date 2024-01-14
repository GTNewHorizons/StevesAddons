package vswe.stevesfactory.blocks;

import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyHandler;
import stevesaddons.helpers.StevesEnum;
import stevesaddons.tileentities.TileEntityRFNode;

public class TileEntityRFCluster extends TileEntityCluster implements IEnergyHandler {

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int toReceive = 0;
        for (Pair i : getRegistrations(StevesEnum.RECEIVE_ENERGY)) {
            toReceive += ((TileEntityRFNode) i.te).receiveEnergy(from, maxReceive - toReceive, simulate);
        }
        return toReceive;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        int toExtract = maxExtract;
        for (Pair i : getRegistrations(StevesEnum.EXTRACT_ENERGY)) {
            toExtract -= ((TileEntityRFNode) i.te).extractEnergy(from, toExtract, simulate);
        }
        return maxExtract - toExtract;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        for (Pair i : getRegistrations(StevesEnum.CONNECT_ENERGY)) {
            return ((TileEntityRFNode) i.te).getEnergyStored(from);
        }
        return -1;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        for (Pair i : getRegistrations(StevesEnum.CONNECT_ENERGY)) {
            return ((TileEntityRFNode) i.te).getMaxEnergyStored(from);
        }
        return -1;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        for (Pair i : getRegistrations(StevesEnum.CONNECT_ENERGY)) {
            if (((TileEntityRFNode) i.te).canConnectEnergy(from)) return true;
        }
        return false;
    }
}
