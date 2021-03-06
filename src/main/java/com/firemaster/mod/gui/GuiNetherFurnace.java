package com.firemaster.mod.gui;

import org.lwjgl.opengl.GL11;

import com.firemaster.mod.FieryMod;
import com.firemaster.mod.container.ContainerNetherFurnace;
import com.firemaster.mod.tileentity.TileEntityNetherFurnace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiNetherFurnace extends GuiContainer {
	public static final ResourceLocation background = new ResourceLocation(FieryMod.ModId + ":textures/gui/GuiNetherBrickFurnace.png");
	
	public TileEntityNetherFurnace netherFurnace;
	
	public GuiNetherFurnace(InventoryPlayer inventory, TileEntityNetherFurnace entity) {
		super(new ContainerNetherFurnace(inventory, entity));
		
		this.netherFurnace = entity;
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int var1, int var2) {
		String name = this.netherFurnace.hasCustomInventoryName() ? this.netherFurnace.getInventoryName() : I18n.format(this.netherFurnace.getInventoryName(), new Object[0]);
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 118, this.ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if (this.netherFurnace.isBurning()) {
			int k = this.netherFurnace.getBurnTimeRemainingScaled(40);
			
			drawTexturedModalRect(guiLeft + 29, guiTop + 65, 176, 0, k, 10);
		}
		
		int k = this.netherFurnace.getCookProgressScaled(24);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 10, k, 16);
	}

}
