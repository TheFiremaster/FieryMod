package com.firemaster.mod.items;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemSpade;

public class IgnisShovel extends ItemSpade {
	public IgnisShovel() {
		super(FieryMod.ignisMaterial);
		
		this.setUnlocalizedName("IgnisShovel");
		this.setTextureName(FieryMod.ModId + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
}
