package com.firemaster.mod.container;

import com.firemaster.mod.tileentity.TileEntityNetherFurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerNetherFurnace extends Container {
	private TileEntityNetherFurnace netherFurnace;
	
	public ContainerNetherFurnace(InventoryPlayer inventory, TileEntityNetherFurnace entity) {
		this.netherFurnace = entity;
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		// TODO Auto-generated method stub
		return false;
	}

}
