package com.firemaster.mod.blocks;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class IgnisBlock extends Block {
	public IgnisBlock() {
		super(Material.iron);
		
		this.setBlockName("IgnisBlock");
		this.setBlockTextureName(FieryMod.ModId + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		this.setHardness(5.0f);
		this.setResistance(10.0f);
		this.setStepSound(soundTypeMetal);
		this.setLightLevel(1.0f);
		
		GameRegistry.registerBlock(this, getUnlocalizedName().substring(5));
	}
}
