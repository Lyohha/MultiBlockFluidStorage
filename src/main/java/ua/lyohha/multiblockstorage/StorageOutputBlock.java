package ua.lyohha.multiblockstorage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class StorageOutputBlock extends Block
{
    public StorageOutputBlock()
    {
        super(Material.iron);
        this.setHarvestLevel("pickaxe",1);
        this.setResistance(10F);
        this.setHardness(15F);
    }
}
