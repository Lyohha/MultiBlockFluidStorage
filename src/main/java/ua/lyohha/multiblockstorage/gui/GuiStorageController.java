package ua.lyohha.multiblockstorage.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import ua.lyohha.multiblockstorage.StorageControllerTileEntity;
import ua.lyohha.multiblockstorage.inventory.ContainerStorageController;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiStorageController extends GuiContainer
{
    private StorageControllerTileEntity storageControllerTileEntity;
    public static final ResourceLocation resourceLocation = new ResourceLocation("multiblockstorage:textures/gui/MultiStorage.png");
    public GuiStorageController(InventoryPlayer inventoryPlayer, StorageControllerTileEntity storageControllerTileEntity)
    {
        super(new ContainerStorageController(inventoryPlayer,storageControllerTileEntity));
        xSize = 176;
        ySize = 166;
        this.storageControllerTileEntity = storageControllerTileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        drawTexturedModalRect(guiLeft,guiTop,0,0,xSize,ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        String s = I18n.format(storageControllerTileEntity.getInventoryName());
        fontRendererObj.drawString(s, xSize/2 - fontRendererObj.getStringWidth(s)/2,3,Color.darkGray.getRGB());
        List<String> hoveringText = new ArrayList<String>();
        if(isMouseInRect(guiLeft+79,guiTop+12,guiLeft+96,guiTop+66,x,y))
            hoveringText.add(storageControllerTileEntity.getFluidAmount()+ "/"+storageControllerTileEntity.getCapacity());
        if(storageControllerTileEntity.getFluid() != null)
        {
            if(isMouseInRect(guiLeft+79,guiTop+12,guiLeft+96,guiTop+66,x,y))
                hoveringText.add(storageControllerTileEntity.getFluid().getFluid().getName());
        }
        if (!hoveringText.isEmpty())
            drawHoveringText(hoveringText, x - guiLeft, y - guiTop, fontRendererObj);

    }

    private boolean isMouseInRect(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        return x3 >= x1 && x3 <= x2 && y3 >= y1 && y3 <= y2;
    }
}
