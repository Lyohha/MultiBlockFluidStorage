package ua.lyohha.multiblockstorage;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class StorageBlock extends BlockContainer
{
    public StorageBlock()
    {
        super(Material.iron);
    }

    public StorageBlock(Material material)
    {
        super(material);
    }
}
