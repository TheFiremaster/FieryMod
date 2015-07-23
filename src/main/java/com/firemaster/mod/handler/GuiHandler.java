package com.firemaster.mod.handler;

import com.firemaster.mod.FieryMod;
import com.firemaster.mod.container.ContainerNetherFurnace;
import com.firemaster.mod.gui.GuiNetherFurnace;
import com.firemaster.mod.tileentity.TileEntityNetherFurnace;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if (entity != null) {
			switch (ID)
			{
			case FieryMod.guiIDNetherBrickFurnace:
				if (entity instanceof TileEntityNetherFurnace) {
					return new ContainerNetherFurnace(player.inventory, (TileEntityNetherFurnace)entity);
				}
				break;
			}
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if (entity != null) {
			switch (ID)
			{
			case FieryMod.guiIDNetherBrickFurnace:
				if (entity instanceof TileEntityNetherFurnace) {
					return new GuiNetherFurnace(player.inventory, (TileEntityNetherFurnace)entity);
				}
				break;
			}
		}
		
		return null;
	}

}
