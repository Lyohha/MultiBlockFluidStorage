package ua.lyohha.multiblockstorage.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ua.lyohha.multiblockstorage.StorageControllerTileEntity;

public class ContainerStorageController extends ContainerMultiBlockStorage
{
    StorageControllerTileEntity storageControllerTileEntity;

    public ContainerStorageController(InventoryPlayer inventoryPlayer, StorageControllerTileEntity storageControllerTileEntity)
    {
        addSlotToContainer(new SlotStorage(storageControllerTileEntity,0,116,16));
        addSlotToContainer(new SlotStorage(storageControllerTileEntity,1,116,52));
        this.addPlayerSlots(inventoryPlayer,8,84,0);
        this.storageControllerTileEntity = storageControllerTileEntity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return storageControllerTileEntity.isUseableByPlayer(player) && storageControllerTileEntity.isFormed();
        //return storageControllerTileEntity.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        return null;
    }
}
