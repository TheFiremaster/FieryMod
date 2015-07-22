package com.firemaster.mod.blocks;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class OreBlock extends Block {
	public OreBlock(String unlocName) {
		super(Material.rock);
		
		this.setBlockName(unlocName);
		this.setBlockTextureName(FieryMod.ModId + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		this.setHardness(3.0f);
		this.setResistance(5.0f);
		this.setStepSound(soundTypeStone);
		
		GameRegistry.registerBlock(this, getUnlocalizedName().substring(5));
	}
}
