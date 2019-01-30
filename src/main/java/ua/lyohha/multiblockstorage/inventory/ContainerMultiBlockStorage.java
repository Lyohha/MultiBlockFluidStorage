package ua.lyohha.multiblockstorage.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public abstract class ContainerMultiBlockStorage extends Container
{
    protected void addPlayerSlots(InventoryPlayer inventoryPlayer, int x, int y, int startSlot)
    {
        //инвентарь, номер слота, позиция х, позиция у
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, startSlot + j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, startSlot + i, x  + i * 18, y + 58));
        }
    }
}
