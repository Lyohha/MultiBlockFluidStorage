package ua.lyohha.multiblockstorage;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import ua.lyohha.multiblockstorage.gui.GuiHandler;
import ua.lyohha.multiblockstorage.proxy.CommonProxy;
import ua.lyohha.multiblockstorage.tileentity.HatchTileEntity;

@Mod(modid = MultiBlockStorage.modid, version = MultiBlockStorage.version, name = MultiBlockStorage.modname)
public class MultiBlockStorage
{
    public static final String modid = "multiblockstorage";
    public static final String modname = "MultiBlock Storage";
    private static final int build = 1;
    public static final String version = "0.0.2." + build;

    //CreativeTab
    public static CreativeTabs multiBlockStorageTab = new MultiBlockStorageTab("multiblock_storage_tab");

    //Blocks
    public static Block storageCasingBlock;
    public static Block storageControllerBlock;
    public static Block storageInputBlock;
    public static Block storageOutputBlock;

    @Mod.Instance(MultiBlockStorage.modid)
    public static MultiBlockStorage instance;

    @SidedProxy(clientSide = "ua.lyohha.multiblockstorage.proxy.ClientProxy", serverSide = "ua.lyohha.multiblockstorage.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preLoad(FMLPostInitializationEvent event)
    {
        GenerateBlocks();
        RegisterBlocks();
        RegisterEntity();
        RegisterRecipe();
        RegisterGUI();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }

    private void GenerateBlocks()
    {
        //AddBlock
        storageCasingBlock = new StorageCasingBlock().setCreativeTab(MultiBlockStorage.multiBlockStorageTab).setBlockName("storageCasingBlock").setBlockTextureName("multiblockstorage:StorageCasingBlock");
        storageControllerBlock = new StorageControllerBlock().setCreativeTab(MultiBlockStorage.multiBlockStorageTab).setBlockName("storageControllerBlock").setBlockTextureName("multiblockstorage:StorageControllerBlock");
        storageInputBlock = new StorageInputBlock().setCreativeTab(MultiBlockStorage.multiBlockStorageTab).setBlockName("storageInputBlock").setBlockTextureName("multiblockstorage:StorageInputBlock");
        storageOutputBlock = new StorageOutputBlock().setCreativeTab(MultiBlockStorage.multiBlockStorageTab).setBlockName("storageOutputBlock").setBlockTextureName("multiblockstorage:StorageOutputBlock");

    }

    private void RegisterBlocks()
    {
        GameRegistry.registerBlock(storageCasingBlock,"storageCasingBlock");
        GameRegistry.registerBlock(storageControllerBlock,"storageControllerBlock");
        GameRegistry.registerBlock(storageInputBlock,"storageInputBlock");
        GameRegistry.registerBlock(storageOutputBlock,"storageOutputBlock");

    }

    private void RegisterRecipe()
    {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MultiBlockStorage.storageCasingBlock,4), new Object[]{"XXX","XYX","XXX",('X'),"plateBronze"}));
        GameRegistry.addRecipe(new ItemStack(MultiBlockStorage.storageControllerBlock,1), new Object[] {"X","Y",('X'), Blocks.glass,('Y'),MultiBlockStorage.storageCasingBlock});
        GameRegistry.addRecipe(new ItemStack(MultiBlockStorage.storageInputBlock,1), new Object[] {"X","Y",('X'), Blocks.piston,('Y'),MultiBlockStorage.storageCasingBlock});
        GameRegistry.addRecipe(new ItemStack(MultiBlockStorage.storageOutputBlock,1), new Object[] {"X","Y",('Y'), Blocks.piston,('X'),MultiBlockStorage.storageCasingBlock});
    }

    private void RegisterGUI()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

    }

    private void RegisterEntity()
    {
        GameRegistry.registerTileEntity(StorageControllerTileEntity.class,"StorageControllerTileEntity");
        GameRegistry.registerTileEntity(HatchTileEntity.class,"HatchTileEntity");
    }
}
