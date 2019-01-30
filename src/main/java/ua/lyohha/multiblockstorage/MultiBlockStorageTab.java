package ua.lyohha.multiblockstorage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MultiBlockStorageTab extends CreativeTabs
{
    public MultiBlockStorageTab(String label)
    {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return Item.getItemFromBlock(MultiBlockStorage.storageControllerBlock);
    }
}
