package ua.lyohha.multiblockstorage.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import ua.lyohha.multiblockstorage.StorageControllerTileEntity;

public class HatchTileEntity extends TileEntity implements IFluidHandler
{
    private StorageControllerTileEntity storageControllerTileEntity = null;
    private TypeHatch typeHatch;

    public enum TypeHatch
    {
        INPUT,
        OUTPUT
    }

    public HatchTileEntity(TypeHatch typeHatch)
    {
        this.typeHatch = typeHatch;
    }

    public void setControllerBlock(StorageControllerTileEntity storageControllerTileEntity)
    {
        this.storageControllerTileEntity = storageControllerTileEntity;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if(storageControllerTileEntity != null)
        {
            if(storageControllerTileEntity.isInvalid())
            {
                storageControllerTileEntity = null;
            }
        }
    }

    //функции nbt-тегов
    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        if(storageControllerTileEntity != null)
        {
            nbtTagCompound.setInteger("xCoord",storageControllerTileEntity.xCoord);
            nbtTagCompound.setInteger("yCoord",storageControllerTileEntity.yCoord);
            nbtTagCompound.setInteger("zCoord",storageControllerTileEntity.zCoord);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        int INT_ID = 3, xCoord = 0, yCoord = 0, zCoord = 0;
        if(nbtTagCompound.hasKey("xCoord",INT_ID) && nbtTagCompound.hasKey("yCoord",INT_ID) && nbtTagCompound.hasKey("zCoord",INT_ID))
        {
            xCoord = nbtTagCompound.getInteger("xCoord");
            yCoord = nbtTagCompound.getInteger("yCoord");
            zCoord = nbtTagCompound.getInteger("zCoord");
            storageControllerTileEntity = (StorageControllerTileEntity)worldObj.getTileEntity(xCoord,yCoord,zCoord);

        }
    }


    //функции жидкосного хранилища

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if(storageControllerTileEntity != null)
        {
            if(typeHatch == TypeHatch.INPUT)
            {
                return storageControllerTileEntity.fill(resource,doFill);
            }
            else
            {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if(storageControllerTileEntity != null)
        {
            if(typeHatch == TypeHatch.INPUT)
                return null;
            else
            {
                if(storageControllerTileEntity.getFluid() != null)
                    if(storageControllerTileEntity.getFluid().isFluidEqual(resource))
                        return storageControllerTileEntity.drain(resource.amount,doDrain);
            }
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if(storageControllerTileEntity != null)
        {
            if(typeHatch == TypeHatch.INPUT)
            {
                return null;
            }
            else
            {
                return storageControllerTileEntity.drain(maxDrain,doDrain);
            }
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if(typeHatch == TypeHatch.OUTPUT)
            return false;
        if(storageControllerTileEntity != null)
        {
            FluidStack fluidStack = storageControllerTileEntity.getFluid();
            if(fluidStack == null && !storageControllerTileEntity.isFormed())
                return false;
            return fluidStack==null || fluidStack.getFluid() == fluid;
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if(typeHatch == TypeHatch.INPUT)
            return false;
        if(storageControllerTileEntity != null)
        {
            FluidStack fluidStack = storageControllerTileEntity.getFluid();
            if(fluidStack == null && !storageControllerTileEntity.isFormed())
                return false;
            return fluidStack==null || fluidStack.isFluidEqual(new FluidStack(fluid,1));
        }
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        if(storageControllerTileEntity != null)
            return new FluidTankInfo[]{storageControllerTileEntity.getInfo()};
        return new FluidTankInfo[0];
    }
}
