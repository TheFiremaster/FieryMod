package com.firemaster.mod.items;

import java.util.ArrayList;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class IgnisHoe extends ItemHoe {
	public IgnisHoe() {
		super(FieryMod.ignisMaterial);
		
		this.setUnlocalizedName("IgnisHoe");
		this.setTextureName(FieryMod.ModId + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		// Don't do anything if the player is not looking at anything or if the item is not damaged.
		MovingObjectPosition object = this.getMovingObjectPositionFromPlayer(world, player, true);
		if (object == null || itemStack.getItemDamage() == 0 || world.isRemote) {
			return itemStack;
		}
		
		int x = object.blockX;
		int y = object.blockY;
		int z = object.blockZ;
		
		Material material = world.getBlock(x, y, z).getMaterial();
        
        // If the player is looking at lava, remove the lava and restore the pick's durability by (up to) 550.
        if (material == Material.lava) {
        	world.setBlock(x, y, z, Blocks.air);
        	
        	// Don't repair beyond full durability.
        	if (itemStack.getItemDamage() - 250 < 0) {
        		itemStack.setItemDamage(0);
        	} else {
        		itemStack.setItemDamage(itemStack.getItemDamage() - 250);
        	}
        }
		
		return itemStack;
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		// This method of auto smelting mined blocks is loosly based on how Tinker's Construct handles its auto smelting.
		
		// First, make sure conditions are correct to properly get the block.
		Block block = player.worldObj.getBlock(X, Y, Z);
		if (player.worldObj.isRemote || block == null || EnchantmentHelper.getSilkTouchModifier(player) || !(block instanceof BlockCrops)) {
			return false;
		}
		
		// Unlike the rest of the Ignis tools, get the list of items that would drop.
		ArrayList<ItemStack> blockItems = block.getDrops(player.worldObj, X, Y, Z, player.worldObj.getBlockMetadata(X, Y, Z), EnchantmentHelper.getFortuneModifier(player));
		if (blockItems == null || blockItems.isEmpty())
		{
			return false;
		}
		
		// Manually damage the hoe and delete the block.
		itemstack.damageItem(1, player);
		player.worldObj.setBlock(X, Y, Z, Blocks.air);
		
		// Then, for each drop in the list, check if it is wheat or it can be smelted. If wheat, replace that drop with bread. If smeltable, replace with what would be smelted.
		for (int i = 0; i < blockItems.size(); i++)
		{
			if (blockItems.get(i).getItem() == Items.wheat)
			{
				blockItems.set(i, new ItemStack(Items.bread));
			}
			else if (FurnaceRecipes.smelting().getSmeltingResult(blockItems.get(i)) != null)
			{
				blockItems.set(i, FurnaceRecipes.smelting().getSmeltingResult(blockItems.get(i)).copy());
			}
		}
		
		// Then for each item in the list, spawn it.
		float f = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		float f1 = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		float f2 = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		for (int i = 0; i < blockItems.size(); i++)
		{
			EntityItem item = new EntityItem(player.worldObj, (double)((float)X + f), (double)((float)Y + f1), (double)((float)Z + f2), blockItems.get(i));
			
			if (blockItems.get(i).hasTagCompound()) {
				item.getEntityItem().setTagCompound((NBTTagCompound)blockItems.get(i).getTagCompound().copy());
			}
			
			player.worldObj.spawnEntityInWorld(item);
		}
		
		for (int i = 0; i < 30; i++) {
			float xPos = (float)X + player.worldObj.rand.nextFloat() - 0.5f;
			float yPos = (float)Y + player.worldObj.rand.nextFloat() - 0.5f;
			float zPos = (float)Z + player.worldObj.rand.nextFloat() - 0.5f;
			
			player.worldObj.spawnParticle("flame", (double)xPos, (double)yPos, (double)zPos, 0.0, 0.0, 0.0);
		}
		
		return true;
	}
}
