package ua.lyohha.multiblockstorage.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ua.lyohha.multiblockstorage.StorageControllerTileEntity;
import ua.lyohha.multiblockstorage.inventory.ContainerStorageController;

public class GuiHandler implements IGuiHandler
{
    public final static int GUIID_STORAGE_CONTROLLER = 20;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GUIID_STORAGE_CONTROLLER)
        {
            if(!world.isRemote)
            {
                TileEntity tileEntity = world.getTileEntity(x,y,z);
                if(tileEntity instanceof StorageControllerTileEntity)
                {
                    return new ContainerStorageController(player.inventory,(StorageControllerTileEntity)tileEntity);
                }
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GUIID_STORAGE_CONTROLLER)
        {

            TileEntity tileEntity = world.getTileEntity(x,y,z);
            if(tileEntity instanceof StorageControllerTileEntity)
            {
                return new GuiStorageController(player.inventory,(StorageControllerTileEntity)tileEntity);
            }
        }
        return null;
    }
}
