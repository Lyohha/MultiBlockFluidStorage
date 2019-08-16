package ua.lyohha.multiblockstorage.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import ua.lyohha.multiblockstorage.StorageControllerTileEntity;
import ua.lyohha.multiblockstorage.inventory.ContainerStorageController;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiStorageController extends GuiContainer {
    private StorageControllerTileEntity storageControllerTileEntity;
    public static final ResourceLocation resourceLocation = new ResourceLocation("multiblockstorage:textures/gui/MultiStorage.png");

    public GuiStorageController(InventoryPlayer inventoryPlayer, StorageControllerTileEntity storageControllerTileEntity) {
        super(new ContainerStorageController(inventoryPlayer, storageControllerTileEntity));
        xSize = 176;
        ySize = 166;
        this.storageControllerTileEntity = storageControllerTileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        if (storageControllerTileEntity.getStackInSlot(0) == null) {
            drawTexturedModalRect(guiLeft + 116, guiTop + 16, 176, 0, 16, 16);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String s = I18n.format(storageControllerTileEntity.getInventoryName());
        fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 5, Color.darkGray.getRGB());


        if (storageControllerTileEntity.getFluid() != null) {
            drawFluid(storageControllerTileEntity.getFluid(), 80, 34, 16, 16);
            fontRendererObj.drawString(storageControllerTileEntity.getFluid().getLocalizedName(), 5, 24, Color.darkGray.getRGB());
            fontRendererObj.drawString(storageControllerTileEntity.getFluidAmount() + "\\", 5, 34, Color.darkGray.getRGB());
            fontRendererObj.drawString(Integer.toString(storageControllerTileEntity.getCapacity()), 5, 43, Color.darkGray.getRGB());
        }

    }

    private void drawFluid(FluidStack fluid, int x, int y, int sizeX, int sizeY) {
        IIcon icon = fluid.getFluid().getStillIcon();
        if (icon == null) {
            icon = fluid.getFluid().getIcon();
            if (icon == null) return;
        }
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        int color = fluid.getFluid().getColor();
        GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));
        GL11.glEnable(GL11.GL_BLEND);

        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + sizeY, 0, minU, maxV);
        tessellator.addVertexWithUV(x + sizeX, y + sizeY, 0, maxU, maxV);
        tessellator.addVertexWithUV(x + sizeX, y, 0, maxU, minV);
        tessellator.addVertexWithUV(x, y, 0, minU, minV);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
    }

    private boolean isMouseInRect(int x1, int y1, int x2, int y2, int x3, int y3) {
        return x3 >= x1 && x3 <= x2 && y3 >= y1 && y3 <= y2;
    }
}
