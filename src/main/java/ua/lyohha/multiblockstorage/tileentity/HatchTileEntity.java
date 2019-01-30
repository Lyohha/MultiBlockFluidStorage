package ua.lyohha.multiblockstorage.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import ua.lyohha.multiblockstorage.StorageControllerTileEntity;

public class HatchTileEntity extends TileEntity implements IFluidTank
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
                storageControllerTileEntity = null;
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
    public FluidStack getFluid()
    {

        if(storageControllerTileEntity != null)
        {
            return storageControllerTileEntity.getFluid();
        }
        return null;
    }

    @Override
    public int getFluidAmount()
    {
        if(storageControllerTileEntity != null)
        {
            return storageControllerTileEntity.getFluidAmount();
        }
        return 0;
    }

    @Override
    public int getCapacity()
    {
        if(storageControllerTileEntity != null)
        {
            return storageControllerTileEntity.getCapacity();
        }
        return 0;
    }

    @Override
    public FluidTankInfo getInfo()
    {
        if(storageControllerTileEntity != null)
        {
            return storageControllerTileEntity.getInfo();
        }
        return null;

    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
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
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
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
}
