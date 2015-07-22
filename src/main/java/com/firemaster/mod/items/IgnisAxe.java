package com.firemaster.mod.items;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemAxe;

public class IgnisAxe extends ItemAxe {
	public IgnisAxe() {
		super(FieryMod.ignisMaterial);
		
		this.setUnlocalizedName("IgnisAxe");
		this.setTextureName(FieryMod.ModId + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
}
