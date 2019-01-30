package ua.lyohha.multiblockstorage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class StorageInputBlock extends Block
{
    public  StorageInputBlock()
    {
        super(Material.iron);
        this.setHarvestLevel("pickaxe",1);
        this.setResistance(10F);
        this.setHardness(15F);
    }
}
