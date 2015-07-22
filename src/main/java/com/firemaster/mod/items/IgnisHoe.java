package com.firemaster.mod.items;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemHoe;

public class IgnisHoe extends ItemHoe {
	public IgnisHoe() {
		super(FieryMod.ignisMaterial);
		
		this.setUnlocalizedName("IgnisHoe");
		this.setTextureName(FieryMod.ModId + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
}
