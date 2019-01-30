package ua.lyohha.multiblockstorage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ua.lyohha.multiblockstorage.tileentity.HatchTileEntity;

public class StorageOutputBlock extends BlockContainer
{
    public StorageOutputBlock()
    {
        super(Material.iron);
        this.setHarvestLevel("pickaxe",1);
        this.setResistance(10F);
        this.setHardness(15F);
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int data) {
        return new HatchTileEntity(HatchTileEntity.TypeHatch.OUTPUT);
    }
}
