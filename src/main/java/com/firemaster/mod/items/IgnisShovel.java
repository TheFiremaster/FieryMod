package com.firemaster.mod.items;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class IgnisShovel extends ItemSpade {
	public IgnisShovel() {
		super(FieryMod.ignisMaterial);
		
		this.setUnlocalizedName("IgnisShovel");
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
		if (player.worldObj.isRemote || block == null || EnchantmentHelper.getSilkTouchModifier(player) || !block.isToolEffective("shovel", itemstack.getItem().getMetadata(0))) {
			return false;
		}
		
		// We should only auto smelt if the mined block is something that can be smelt.
		ItemStack blockItem = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(Item.getItemFromBlock(block)));
		if (blockItem == null) {
			return false;
		}
		
		// Manually damage the shovel and delete the block.
		itemstack.damageItem(1, player);
		player.worldObj.setBlock(X, Y, Z, Blocks.air);
		int toSpawn = blockItem.stackSize;
		
		// Spawn each item as an individual drop.
		float f = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		float f1 = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		float f2 = player.worldObj.rand.nextFloat() * 0.8f + 0.1f;
		for (int i = 0; i < toSpawn; i++) {
			EntityItem item = new EntityItem(player.worldObj, (double)((float)X + f), (double)((float)Y + f1), (double)((float)Z + f2), new ItemStack(blockItem.getItem(), 1, blockItem.getItemDamage()));
			
			if (blockItem.hasTagCompound()) {
				item.getEntityItem().setTagCompound((NBTTagCompound)blockItem.getTagCompound().copy());
			}
			
			player.worldObj.spawnEntityInWorld(item);
		}
		
		// Finally, spawn some flame particles where the block was.
		for (int i = 0; i < 30; i++) {
			float xPos = (float)X + player.worldObj.rand.nextFloat() - 0.5f;
			float yPos = (float)Y + player.worldObj.rand.nextFloat() - 0.5f;
			float zPos = (float)Z + player.worldObj.rand.nextFloat() - 0.5f;
			
			player.worldObj.spawnParticle("flame", (double)xPos, (double)yPos, (double)zPos, 0.0, 0.0, 0.0);
		}
		
		return true;
	}
}
