package ua.lyohha.multiblockstorage;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import ua.lyohha.multiblockstorage.gui.GuiHandler;

public class StorageControllerBlock extends StorageBlock {

    public StorageControllerBlock()
    {
        super(Material.iron);
        this.setHarvestLevel("pickaxe",1);
        this.setResistance(10F);
        this.setHardness(15F);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new StorageControllerTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(x,y,z);
            if(tileEntity != null)
            {
                if(((StorageControllerTileEntity)tileEntity).isFormed())
                    player.openGui(MultiBlockStorage.instance, GuiHandler.GUIID_STORAGE_CONTROLLER,world,x,y,z);
                else
                {
                    String msg = ((StorageControllerTileEntity)tileEntity).getErrorMsg();
                    if(msg != null)
                        player.addChatComponentMessage(new ChatComponentText(msg));
                }
            }
        }
        return true;
    }
}
