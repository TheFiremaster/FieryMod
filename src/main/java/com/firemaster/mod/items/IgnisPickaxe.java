package com.firemaster.mod.items;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class IgnisPickaxe extends ItemPickaxe {
	public IgnisPickaxe() {
		super(FieryMod.ignisMaterial);
		
		this.setUnlocalizedName("IgnisPickaxe");
		this.setTextureName(FieryMod.ModId + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		Block block = player.worldObj.getBlock(X, Y, Z);
		if (block == null || EnchantmentHelper.getSilkTouchModifier(player) || !block.isToolEffective("pickaxe", itemstack.getItem().getMetadata(0))) {
			return false;
		}
		
		ItemStack blockItem = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(Item.getItemFromBlock(block)));
		if (blockItem == null) {
			return false;
		}
		
		itemstack.damageItem(1, player);
		player.worldObj.setBlock(X, Y, Z, Blocks.air);
		
		if (player.worldObj.rand.nextFloat() <= 0.2f) {
			blockItem.stackSize++;
		}
		
		System.out.println("Item name: " + blockItem.getItem().getUnlocalizedName() + "\nItem amount: " + blockItem.stackSize);
		float f = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		float f1 = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		float f2 = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		for (int i = 0; i < blockItem.stackSize; i++) {
			EntityItem item = new EntityItem(player.worldObj, (double)((float)X + f), (double)((float)Y + f1), (double)((float)Z + f2), new ItemStack(blockItem.getItem(), 1));
		}
		
		return true;
	}
}
