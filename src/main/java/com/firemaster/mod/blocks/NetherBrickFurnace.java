package com.firemaster.mod.blocks;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class NetherBrickFurnace extends BlockContainer {
	public final boolean isActive;

	@SideOnly(Side.CLIENT)
	private IIcon frontIcon;
	@SideOnly(Side.CLIENT)
	private IIcon topIcon;
	
	public NetherBrickFurnace(boolean active) {
		super(Material.rock);
		
		this.isActive = active;
		if (active) {
			this.setBlockName("NetherBrickFurnaceActive");
			this.setLightLevel(0.625f);
		} else {
			this.setBlockName("NetherBrickFurnaceInactive");
			this.setCreativeTab(FieryMod.fierymodTab);
		}
		
		GameRegistry.registerBlock(this, getUnlocalizedName().substring(5));
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon	= register.registerIcon(FieryMod.ModId + ":NetherBrickFurnaceSide");
		this.frontIcon 	= register.registerIcon(FieryMod.ModId + ":NetherBrickFurnaceFront" + (isActive ? "Active" : "Idle"));
		this.topIcon	= register.registerIcon(FieryMod.ModId + ":NetherBrickFurnaceTop");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return side == 1 ? this.topIcon : (side == 0 ? this.topIcon : (side != metadata ? this.blockIcon : this.frontIcon));
	}
	
	public Item getItemDropped(World world, int x, int y, int z) {
		return Item.getItemFromBlock(FieryMod.blockNetherBrickFurnaceIdle);
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}

	private void setDefaultDirection(World world, int x, int y, int z) {
		if (!world.isRemote) {
			Block b1 = world.getBlock(x, y, z - 1);
			Block b2 = world.getBlock(x, y, z + 1);
			Block b3 = world.getBlock(x - 1, y, z);
			Block b4 = world.getBlock(x + 1, y, z);
			
			byte b0 = 3;
			if (b1.func_149730_j() && !b2.func_149730_j())
            {
                b0 = 3;
            }

            if (b2.func_149730_j() && !b3.func_149730_j())
            {
                b0 = 2;
            }

            if (b3.func_149730_j() && !b4.func_149730_j())
            {
                b0 = 5;
            }

            if (b4.func_149730_j() && !b3.func_149730_j())
            {
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}
	
	// TODO: onBlockActivate

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// TODO: randomDisplayTick
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityPlayer, ItemStack itemStack) {
		int l = MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		} else if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		} else if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		} else if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		
		if (itemStack.hasDisplayName()) {
			// TODO: ((TileEntityNetherFurnace)world.getTileEntity(x, y, z)).setGuiDisplayName(itemStack.getDisplayName());
		}
	}
}
