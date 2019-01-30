package ua.lyohha.multiblockstorage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class StorageCasingBlock extends Block
{
    public StorageCasingBlock()
    {
        super(Material.iron);
        this.setHarvestLevel("pickaxe",1);
        this.setResistance(10F);
        this.setHardness(15F);
    }
}
