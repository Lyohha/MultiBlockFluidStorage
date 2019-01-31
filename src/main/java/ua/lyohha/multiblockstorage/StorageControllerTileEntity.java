package ua.lyohha.multiblockstorage;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.*;
import ua.lyohha.multiblockstorage.tileentity.HatchTileEntity;

import javax.annotation.Nullable;

public class StorageControllerTileEntity extends TileEntity implements ISidedInventory,IFluidTank
{
    private ItemStack[] itemStacks = new ItemStack[2];
    private FluidStack fluidStack = null;
    private boolean formed = false;
    private boolean foundSide = false;
    private int size = 0;
    private int store = 0;
    private int maxstore = 0;
    private final static int maxsize = 250;
    private final static int bucket_per_block = 1000;
    private int offsetX, offsetY, offsetZ;
    private WorldSide worldSide = WorldSide.WEST;

    public StorageControllerTileEntity()
    {
        itemStacks[0] = null;
        itemStacks[1] = null;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if(!worldObj.isRemote)
        {
            checkMultiBlock();
            if(formed)
                updateInventory();
        }
    }

    private void updateInventory()
    {
        if(itemStacks[0] != null)
        {
            if (itemStacks[0].getItem() == Items.bucket)
            {
                if(fluidStack != null )
                {
                    if(itemStacks[1] == null && drain(1000,false) != null && drain(1000,false).amount == 1000)
                    {
                        if(fluidStack.getFluid().getBlock() == Blocks.water)
                        {
                            ItemStack itemStack = new ItemStack(Items.water_bucket, 1);
                            decrStackSize(0,1);
                            setInventorySlotContents(1,itemStack);
                            drain(1000,true);
                        }
                        else if(fluidStack.getFluid().getBlock() == Blocks.lava)
                        {
                            ItemStack itemStack = new ItemStack(Items.lava_bucket, 1);
                            decrStackSize(0,1);
                            setInventorySlotContents(1,itemStack);
                            drain(1000,true);
                        }
                    }
                }
            }
            else if(itemStacks[0].getItem() == Items.water_bucket)
            {
                if(fluidStack != null )
                {
                    if(getFluid().getFluid().getBlock() == Blocks.water)
                    {

                        if(fill(new FluidStack(FluidRegistry.WATER,1000),false) == 1000)
                        {
                            if(itemStacks[1] == null)
                            {
                                decrStackSize(0,1);
                                setInventorySlotContents(1, new ItemStack(Items.bucket,1));
                                fill(new FluidStack(FluidRegistry.WATER,1000),true);
                            }
                            else if(itemStacks[1].getItem() == Items.bucket && itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)//getItem().getItemStackLimit(itemStacks[1])
                            {
                                decrStackSize(0,1);
                                setInventorySlotContents(1, new ItemStack(itemStacks[1].getItem(),itemStacks[1].stackSize + 1));
                                fill(new FluidStack(FluidRegistry.WATER,1000),true);
                            }
                        }
                    }
                }
                else
                {
                    if(itemStacks[1] == null)
                    {
                        decrStackSize(0,1);
                        setInventorySlotContents(1, new ItemStack(Items.bucket,1));
                        fill(new FluidStack(FluidRegistry.WATER,1000),true);
                    }
                    else if(itemStacks[1].getItem() == Items.bucket && itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)//getItem().getItemStackLimit(itemStacks[1])
                    {
                        decrStackSize(0,1);
                        setInventorySlotContents(1, new ItemStack(itemStacks[1].getItem(),itemStacks[1].stackSize + 1));
                        fill(new FluidStack(FluidRegistry.WATER,1000),true);
                    }
                }
            }
            else if(itemStacks[0].getItem() == Items.lava_bucket)
            {
                if(fluidStack != null )
                {
                    if(getFluid().getFluid().getBlock() == Blocks.lava)
                    {
                        if(fill(new FluidStack(FluidRegistry.LAVA,1000),false) == 1000)
                        {
                            if(itemStacks[1] == null)
                            {
                                decrStackSize(0,1);
                                setInventorySlotContents(1, new ItemStack(Items.bucket,1));
                                fill(new FluidStack(FluidRegistry.LAVA,1000),true);
                            }
                            else if(itemStacks[1].getItem() == Items.bucket && itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)
                            {
                                decrStackSize(0,1);
                                setInventorySlotContents(1, new ItemStack(itemStacks[1].getItem(),itemStacks[1].stackSize + 1));
                                fill(new FluidStack(FluidRegistry.LAVA,1000),true);
                            }
                        }
                    }
                }
                else
                {
                    if(itemStacks[1] == null)
                    {
                        decrStackSize(0,1);
                        setInventorySlotContents(1, new ItemStack(Items.bucket,1));
                        fill(new FluidStack(FluidRegistry.LAVA,1000),true);
                    }
                    else if(itemStacks[1].getItem() == Items.bucket && itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)
                    {
                        decrStackSize(0,1);
                        setInventorySlotContents(1, new ItemStack(itemStacks[1].getItem(),itemStacks[1].stackSize + 1));
                        fill(new FluidStack(FluidRegistry.LAVA,1000),true);
                    }
                }
            }
            else if(itemStacks[0].getItem() instanceof IFluidContainerItem)
            {
                if(itemStacks[0].stackSize != 1)//проверка на наличие предметов в первом слоте
                {
                    if(((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]) == null)//если в предмете нету жидкости
                    {
                        if(fluidStack != null)//в баке есть жидкость
                        {
                            int capacity = ((IFluidContainerItem) itemStacks[0].getItem()).getCapacity(itemStacks[0]);//сколько можно вместить в предмет
                            if(capacity <= getFluidAmount())//можно ли заполнить весь предмет
                            {
                                ItemStack itemStack = new ItemStack(itemStacks[0].getItem(),1);
                                int fill = ((IFluidContainerItem)itemStack.getItem()).fill(itemStack,fluidStack,false);
                                if(/*fill == capacity*/fill > 0)//дает ли предмет его заполнить
                                {
                                    ((IFluidContainerItem)itemStack.getItem()).fill(itemStack,fluidStack,true);
                                    if(itemStacks[1] == null)//если во втором слоте нету предметов, то положить новый с заполнением
                                    {
                                        drain(fill,true);
                                        decrStackSize(0,1);
                                        setInventorySlotContents(1,itemStack);
                                    }
                                    else//если премдеты есть
                                    {
                                        //if(((IFluidContainerItem)itemStack.getItem()).getFluid(itemStack).isFluidEqual(itemStacks[0]))
                                        if(itemStacks[1].getItem() == itemStack.getItem())//сравнить предметы
                                        {
                                            if(((IFluidContainerItem)itemStack.getItem()).getFluid(itemStack).isFluidEqual(itemStacks[1]))//если в них одинаковые жидкости
                                            {
                                                if(((IFluidContainerItem)itemStack.getItem()).getFluid(itemStack).amount == ((IFluidContainerItem)itemStacks[1].getItem()).getFluid(itemStacks[1]).amount)//одинаковые емкости
                                                {
                                                    if(itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)//и есть место в стаке, то добавить еще один предмет
                                                    {
                                                        drain(fill,true);
                                                        decrStackSize(0,1);
                                                        itemStack.stackSize = itemStacks[1].stackSize + 1;
                                                        setInventorySlotContents(1, itemStack);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if (fluidStack != null) //если в баке что-то есть
                        {
                            if(!((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]).isFluidEqual(fluidStack))//и это что-то не равну содержимому капсулы, то выходим
                                return;
                        }
                        int fillAmount = fill(((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]), false);
                        if (fillAmount == ((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]).amount)
                        {
                            if (itemStacks[1] == null)//во втором слоте пусто
                            {
                                fill(((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]), true);
                                ItemStack itemStack = new ItemStack(itemStacks[0].getItem(), 1);
                                decrStackSize(0, 1);
                                setInventorySlotContents(1, itemStack);
                            } else //не пусто
                            {
                                ItemStack itemStack = new ItemStack(itemStacks[0].getItem(), 1);
                                if (itemStack.getItem() == itemStacks[1].getItem())//если предметы одинаковы
                                {
                                    if (((IFluidContainerItem) itemStacks[1].getItem()).getFluid(itemStacks[1]) == null)//в предметах во втором слоте нету жидкости
                                    {
                                        if(itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)//и есть место в стаке, то добавить еще один предмет
                                        {
                                            fill(((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]), true);
                                            decrStackSize(0, 1);
                                            setInventorySlotContents(1, new ItemStack(itemStack.getItem(), itemStacks[1].stackSize + 1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    if(((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]) == null)//если капсула пустая
                    {
                        if(fluidStack != null)
                        {
                            int drainAmount = ((IFluidContainerItem) itemStacks[0].getItem()).fill(itemStacks[0],fluidStack,false);
                            ItemStack itemStack = new ItemStack(itemStacks[0].getItem(),1);
                            ((IFluidContainerItem)itemStack.getItem()).fill(itemStack,fluidStack,true);
                            if(itemStacks[1] == null)
                            {
                                drain(drainAmount, true);
                                decrStackSize(0,1);
                                setInventorySlotContents(1, itemStack);
                            }
                            else
                            {
                                if (itemStack.getItem() == itemStacks[1].getItem())//одинаковые предметы
                                {
                                    if (((IFluidContainerItem) itemStacks[1].getItem()).getFluid(itemStacks[1]) != null && ((IFluidContainerItem) itemStacks[1].getItem()).getFluid(itemStacks[1]).isFluidEqual(fluidStack))
                                    //если в этих предметах есть такая же жидкость
                                    {
                                        if (((IFluidContainerItem) itemStacks[1].getItem()).getFluid(itemStacks[1]).amount == drainAmount)//так еще и по количеству жидкости столько-же
                                        {
                                            if (itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)//и есть место в стаке, то добавить еще один предмет
                                            {
                                                decrStackSize(0, 1);
                                                drain(drainAmount, true);
                                                itemStack.stackSize = itemStacks[1].stackSize + 1;
                                                setInventorySlotContents(1, itemStack);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else//если все-таки в ней что-то есть
                    {
                        if(fluidStack != null)//в баке есть что-то
                        {
                            if(!((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]).isFluidEqual(fluidStack))//и это что-то не равну содержимому капсулы, то выходим
                                return;
                        }
                        int fillAmount = fill(((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]),false);
                        if(itemStacks[1] == null)//если второй слот пустой
                        {
                            fill(((IFluidContainerItem) itemStacks[0].getItem()).drain(itemStacks[0],fillAmount,true),true);
                            setInventorySlotContents(1,itemStacks[0]);
                            decrStackSize(0,1);
                        }
                        else//если второй слот не пустой
                        {
                            ItemStack itemStack = new ItemStack(itemStacks[0].getItem(),1);
                            ((IFluidContainerItem)itemStack.getItem()).fill(itemStack,((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]),true);
                            ((IFluidContainerItem)itemStack.getItem()).drain(itemStack,fillAmount,true);
                            if (itemStack.getItem() == itemStacks[1].getItem())//если одинаковые предметы
                            {
                                if(((IFluidContainerItem)itemStack.getItem()).getFluid(itemStack) == null)//жидкости нету
                                {
                                    if(((IFluidContainerItem)itemStacks[1].getItem()).getFluid(itemStacks[1]) == null)//и в предметах тоже жидкости нету
                                    {
                                        if (itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)//и есть место в стаке, то добавить еще один предмет
                                        {
                                            fill(((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]),true);
                                            decrStackSize(0, 1);
                                            itemStack.stackSize = itemStacks[1].stackSize + 1;
                                            setInventorySlotContents(1, itemStack);
                                        }
                                    }
                                }
                                else//в предметах есть жидкость
                                {
                                    if(((IFluidContainerItem)itemStacks[1].getItem()).getFluid(itemStacks[1]) != null && ((IFluidContainerItem)itemStacks[1].getItem()).getFluid(itemStacks[1]).isFluidEqual(itemStack))
                                    //если жидоксть в них есть, и она такае же
                                    {
                                        if(((IFluidContainerItem)itemStack.getItem()).getFluid(itemStack).amount == ((IFluidContainerItem)itemStacks[1].getItem()).getFluid(itemStacks[1]).amount)//количество жидкости одинаково
                                        {
                                            if (itemStacks[1].getMaxStackSize() > itemStacks[1].stackSize)//и есть место в стаке, то добавить еще один предмет
                                            {
                                                fill(((IFluidContainerItem) itemStacks[0].getItem()).getFluid(itemStacks[0]),true);
                                                decrStackSize(0, 1);
                                                itemStack.stackSize = itemStacks[1].stackSize + 1;
                                                setInventorySlotContents(1, itemStack);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkMultiBlock()
    {
        if(!foundSide)
            checkSide();
        if(foundSide)
        {
            if (offsetY > maxsize - 2)
                offsetY = -1;
            if(offsetY == -1)
            {
                if (!checkBase()) {
                    formed = false;
                    foundSide = false;
                    this.markDirty();
                }
            }
            else
            {
                if(!checkTier())
                {
                    if(checkBase())
                    {
                        size = offsetY + 2;
                        offsetY = -2;
                        formed = true;
                        setMaxStore();
                    }
                    else
                    {
                        foundSide = false;
                        formed = false;

                    }
                    this.markDirty();
                }
            }
            offsetY++;
        }
    }

    public boolean isFormed()
    {
        return formed;
    }

    private void setMaxStore()
    {
        maxstore = size*9*bucket_per_block;
        if(store > maxstore)
        {
            store = maxstore;
            fluidStack.amount = store;
        }
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        //System.out.println("Max Store:" + maxstore);
    }

    private boolean checkTier()
    {
        if(yCoord + offsetY > 255)
            return false;
        for(int i=-1;i<2;i++)
        {
            for(int j=-1;j<2;j++)
            {
                Block block = worldObj.getBlock(xCoord+offsetX+i,yCoord+offsetY,zCoord+offsetZ+j);
                if(i != 0 && j != 0)
                {
                    if(block != MultiBlockStorage.storageCasingBlock)
                        return false;
                }
                else if(i == 0 && j == 0)
                {
                    if(block != Blocks.air)
                        return false;
                }
                else
                {
                    if(offsetY == 0)
                    {
                        if(offsetX == 0)
                        {
                            if((j+offsetZ) == 0)
                            {
                                if (block != MultiBlockStorage.storageControllerBlock)
                                    return false;
                            }
                            else
                            {
                                if (!(block == MultiBlockStorage.storageCasingBlock || block == MultiBlockStorage.storageInputBlock || block == MultiBlockStorage.storageOutputBlock))
                                    return false;
                                if(block == MultiBlockStorage.storageInputBlock || block == MultiBlockStorage.storageOutputBlock)
                                {
                                    TileEntity tileEntity = worldObj.getTileEntity(xCoord+offsetX+i,yCoord+offsetY,zCoord+offsetZ+j);
                                    if(tileEntity != null)
                                        ((HatchTileEntity)tileEntity).setControllerBlock(this);
                                }
                            }

                        }
                        else
                        {
                            if((i+offsetX) == 0)
                            {
                                if (block != MultiBlockStorage.storageControllerBlock)
                                    return false;
                            }
                            else
                            {
                                if (!(block == MultiBlockStorage.storageCasingBlock || block == MultiBlockStorage.storageInputBlock || block == MultiBlockStorage.storageOutputBlock))
                                    return false;
                                if(block == MultiBlockStorage.storageInputBlock || block == MultiBlockStorage.storageOutputBlock)
                                {
                                    TileEntity tileEntity = worldObj.getTileEntity(xCoord+offsetX+i,yCoord+offsetY,zCoord+offsetZ+j);
                                    if(tileEntity != null)
                                        ((HatchTileEntity)tileEntity).setControllerBlock(this);
                                }
                            }
                        }
                    }
                    else
                    {
                        if (!(block == MultiBlockStorage.storageCasingBlock || block == MultiBlockStorage.storageInputBlock || block == MultiBlockStorage.storageOutputBlock))
                            return false;
                        if(block == MultiBlockStorage.storageInputBlock || block == MultiBlockStorage.storageOutputBlock)
                        {
                            TileEntity tileEntity = worldObj.getTileEntity(xCoord+offsetX+i,yCoord+offsetY,zCoord+offsetZ+j);
                            if(tileEntity != null)
                                ((HatchTileEntity)tileEntity).setControllerBlock(this);
                        }
                    }

                }
            }
        }
        return true;
    }

    private boolean checkBase()
    {
        if(yCoord == 0)
            return false;
        if(yCoord + offsetY > 255)
            return false;
        for(int i=-1;i<2;i++)
        {
            for(int j=-1;j<2;j++)
            {
                Block block = worldObj.getBlock(xCoord+offsetX+i,yCoord+offsetY,zCoord+offsetZ+j);
                if(block != MultiBlockStorage.storageCasingBlock)
                    return false;
            }
        }
        return true;
    }

    private void checkSide()
    {
        //проверка куда смотрит мультиблок
        int offsetX=0, offsetZ=0;
        if(worldSide == WorldSide.WEST)
        {
            offsetX = 0;
            offsetZ = 2;
            this.offsetX = 0;
            this.offsetZ = 1;
            this.offsetY = -1;
            worldSide = WorldSide.NORTH;
        }
        else if(worldSide == WorldSide.NORTH)
        {
            offsetX = 0;
            offsetZ = -2;
            this.offsetX = 0;
            this.offsetZ = -1;
            this.offsetY = -1;
            worldSide = WorldSide.SOUTH;
        }
        else if(worldSide == WorldSide.SOUTH)
        {
            offsetX = -2;
            offsetZ = 0;
            this.offsetX = -1;
            this.offsetZ = 0;
            this.offsetY = -1;
            worldSide = WorldSide.EAST;
        }
        else if(worldSide == WorldSide.EAST)
        {
            offsetX = 2;
            offsetZ = 0;
            this.offsetX = 1;
            this.offsetZ = 0;
            this.offsetY = -1;
            worldSide = WorldSide.WEST;
        }

        Block block = worldObj.getBlock(xCoord+offsetX, yCoord, zCoord + offsetZ);
        if(block == MultiBlockStorage.storageCasingBlock || block == MultiBlockStorage.storageInputBlock || block == MultiBlockStorage.storageOutputBlock)
            foundSide = true;
    }

    //функции для NBT тегов

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        int metadata = getBlockMetadata();
        return new S35PacketUpdateTileEntity(xCoord,yCoord,zCoord,metadata,nbtTagCompound);
    }


    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("store",store);
        nbtTagCompound.setInteger("maxstore",maxstore);
        nbtTagCompound.setInteger("size",size);
        nbtTagCompound.setBoolean("formed", formed);
        nbtTagCompound.setBoolean("foundSide",foundSide);
        switch (worldSide)
        {
            case SOUTH:
                nbtTagCompound.setInteger("worldSide",1);
                break;
            case WEST:
                nbtTagCompound.setInteger("worldSide",2);
                break;
            case EAST:
                nbtTagCompound.setInteger("worldSide",3);
                break;
            case NORTH:
                nbtTagCompound.setInteger("worldSide",4);
                break;
            case NONE:
                nbtTagCompound.setInteger("worldSide",0);
                break;
        }
        if(itemStacks[0] != null)
        {
            NBTTagCompound dataSlot = new NBTTagCompound();
            itemStacks[0].writeToNBT(dataSlot);
            nbtTagCompound.setTag("itemStacks_0",dataSlot);
        }
        if(itemStacks[1] != null)
        {
            NBTTagCompound dataSlot = new NBTTagCompound();
            itemStacks[1].writeToNBT(dataSlot);
            nbtTagCompound.setTag("itemStacks_1",dataSlot);
        }
        if(fluidStack != null)
        {
            NBTTagCompound dataFluid = new NBTTagCompound();
            fluidStack.writeToNBT(dataFluid);
            nbtTagCompound.setTag("fluidStack",dataFluid);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        int INT_ID = 3, LONG_ID = 4, NBT_ID=10;
        if(nbtTagCompound.hasKey("store",INT_ID))
            store = nbtTagCompound.getInteger("store");
        if(nbtTagCompound.hasKey("maxstore",INT_ID))
            maxstore = nbtTagCompound.getInteger("maxstore");
        if(nbtTagCompound.hasKey("size",INT_ID))
            size = nbtTagCompound.getInteger("size");
        if(nbtTagCompound.hasKey("formed"))
            formed = nbtTagCompound.getBoolean("formed");
        if(nbtTagCompound.hasKey("foundSide"))
            foundSide = nbtTagCompound.getBoolean("foundSide");
        if(nbtTagCompound.hasKey("worldSide",INT_ID))
        {
            switch (nbtTagCompound.getInteger("worldSide"))
            {
                case 1:
                    worldSide = WorldSide.SOUTH;
                    break;
                case 2:
                    worldSide = WorldSide.WEST;
                    break;
                case 3:
                    worldSide = WorldSide.EAST;
                    break;
                case 4:
                    worldSide = WorldSide.NORTH;
                    break;
            }
        }
        if(nbtTagCompound.hasKey("itemStacks_0",NBT_ID))
        {
            NBTTagCompound nbt = nbtTagCompound.getCompoundTag("itemStacks_0");
            itemStacks[0] = ItemStack.loadItemStackFromNBT(nbt);
        }
        if(nbtTagCompound.hasKey("itemStacks_1",NBT_ID))
        {
            NBTTagCompound nbt = nbtTagCompound.getCompoundTag("itemStacks_1");
            itemStacks[1] = ItemStack.loadItemStackFromNBT(nbt);
        }
        if(nbtTagCompound.hasKey("fluidStack",NBT_ID))
        {
            NBTTagCompound nbt = nbtTagCompound.getCompoundTag("fluidStack");
            fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
        }
    }

    //Функции от инвенторя
    //функции для закидывания через воронку+
    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }

    //функции для работы с инвентарем
    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        return itemStacks[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack itemStackInSlot = itemStacks[slot];
        if(itemStackInSlot == null)
            return null;
        ItemStack itemStackRemoved;
        if(itemStackInSlot.stackSize <= amount)
        {
            itemStackRemoved = itemStackInSlot;
            setInventorySlotContents(slot,null);
        }
        else
        {
            itemStackRemoved = itemStackInSlot.splitStack(amount);
            if(itemStackInSlot.stackSize == 0)
                setInventorySlotContents(slot,null);
        }
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        return itemStackRemoved;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        itemStacks[slot] = stack;
        if(stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
    }

    @Override
    public String getInventoryName() {
        return MultiBlockStorage.storageControllerBlock.getUnlocalizedName() + ".container.name";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if(slot == 1)
            return false;
        else
        {
            if(stack.getItem() == Items.bucket || stack.getItem() == Items.lava_bucket || stack.getItem() == Items.water_bucket || stack.getItem() instanceof IFluidContainerItem)
            {
                return true;
            }
        }
        return false;
    }

    //теперь это еще и жидкосное хранилище

    @Override
    public FluidStack getFluid()
    {
        if(!formed)
            return null;
        return fluidStack;
    }

    @Override
    public int getFluidAmount()
    {
        if(!formed)
            return 0;
        return store;
    }

    @Override
    public int getCapacity()
    {
        if(!formed)
            return 0;
        return maxstore;
    }

    @Override
    public FluidTankInfo getInfo()
    {
        if(!formed)
            return null;
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if(!formed)
            return 0;
        if(resource != null)
        {
            if(fluidStack == null)
            {
                if(getFluidAmount() + resource.amount <= getCapacity())
                {
                    if(doFill)
                    {
                        fluidStack = new FluidStack(resource.getFluid(), resource.amount);
                        store = resource.amount;
                    }
                    return resource.amount;
                }
                else
                {
                    int amount = getCapacity() - getFluidAmount();
                    if(doFill)
                    {
                        fluidStack = new FluidStack(resource.getFluid(), amount);
                        store = amount;
                    }
                    return amount;
                }
            }
            else if(fluidStack.isFluidEqual(resource))
            {
                if(getFluidAmount() + resource.amount <= getCapacity())
                {
                    if(doFill)
                    {
                        store = store + resource.amount;
                        fluidStack.amount = store;
                    }
                    return resource.amount;
                }
                else
                {
                    int amount = getCapacity() - getFluidAmount();
                    if(doFill)
                    {
                        store = store + amount;
                        fluidStack.amount = store;
                    }
                    return amount;
                }
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if(!formed)
            return null;
        if(fluidStack != null)
        {
            if(getFluidAmount() - maxDrain > 0)
            {
                if(doDrain) {
                    store = store - maxDrain;
                    fluidStack.amount = store;
                }
                return new FluidStack(fluidStack.getFluid(),maxDrain);
            }
            else
            {
                int amount = getFluidAmount();
                Fluid fluid = fluidStack.getFluid();
                if(doDrain)
                {
                    store = 0;
                    fluidStack = null;
                }
                return new FluidStack(fluid,amount);
            }
        }
        return null;
    }
}
