package com.firemaster.mod.tileentity;

import com.firemaster.mod.blocks.NetherBrickFurnace;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityNetherFurnace extends TileEntity implements ISidedInventory {
	private String localizedName;
	
	private ItemStack[] slots = new ItemStack[3];
	
	private static final int[] slots_top = new int[]{0};
	private static final int[] slots_side = new int[]{1};
	private static final int[] slots_bottom = new int[]{2, 1};
	
	public int furnaceSpeed = 150;
	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;
	
	public void setGuiDisplayName(String displayName) {
		this.localizedName = displayName;
	}

	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.localizedName : "container.NetherBrickFurnace";
	}

	public boolean hasCustomInventoryName() {
		return this.localizedName != null && this.localizedName.length() > 0;
	}
	
	public int getSizeInventory() {
		return this.slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int num) {
		if (this.slots[slot] != null) {
			ItemStack itemStack;
			
			if (this.slots[slot].stackSize < num) {
				itemStack = this.slots[slot];
				this.slots[slot] = null;
				return itemStack;
			} else {
				itemStack = this.slots[slot].splitStack(num);
				
				if (this.slots[slot].stackSize == 0) {
					this.slots[slot] = null;
				}
				
				return itemStack;
			}
		}

		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.slots[slot] != null) {
			ItemStack itemStack = this.slots[slot];
			this.slots[slot] = null;
			return itemStack;
		}
		
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.slots[slot] = itemStack;
		
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5, (double)this.yCoord + 0.5, (double)this.zCoord + 0.5) <= 64.0;
	}

	public void openInventory() {}
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 2 ? false : (slot == 1 ? isItemFuel(itemStack) : true);
	}
	
	public static boolean isItemFuel(ItemStack itemStack) {
		return getItemBurnTime(itemStack) > 0;
	}

	private static int getItemBurnTime(ItemStack itemStack) {
		if (itemStack == null) {
			return 0;
		} else {
			Item item = itemStack.getItem();
			
			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);
				
				if (block == Blocks.log) return 300;
				if (block == Blocks.planks) return 300;
				if (block == Blocks.sapling) return 100;
			} else {
				if (item == Items.coal) return 1600;
				if (item == Items.stick) return 100;
				if (item == Items.blaze_rod) return 2400;
				if (item == Items.lava_bucket) return 20000;
			}
		}

		return 0;
	}
	
	public boolean isBurning() {
		return this.burnTime > 0;
	}
	
	public boolean canSmelt() {
		if (this.slots[0] == null) {
			return false;
		} else {
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
			
			if (itemStack == null) {
				return false;
			}
			
			if (this.slots[2] == null) {
				return true;
			}
			
			if (!this.slots[2].isItemEqual(itemStack)) {
				return false;
			}
			
			int result = this.slots[2].stackSize + itemStack.stackSize;
			
			return (result <= this.slots[2].getMaxStackSize() && result <= this.getInventoryStackLimit());
		}
	}
	
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
			
			if (this.slots[2] == null) {
				this.slots[2] = itemStack.copy();
			} else if (this.slots[2].isItemEqual(itemStack)) {
				this.slots[2].stackSize += itemStack.stackSize;
			}
			
			this.slots[0].stackSize--;
			
			if (this.slots[0].stackSize <= 0) {
				this.slots[0] = null;
			}
		}
	}
	
	public void updateEntity() {
		boolean flag = this.burnTime > 0;
		boolean flag1 = false;
		
		if (this.isBurning()) {
			burnTime--;
		}
		
		if (!this.worldObj.isRemote) {
			if (this.burnTime == 0 && this.canSmelt()) {
				this.currentItemBurnTime = this.burnTime = getItemBurnTime(this.slots[1]);
				
				if (this.isBurning()) {
					flag1 = true;
					
					if (this.slots[1] != null) {
						this.slots[1].stackSize--;
						
						if (this.slots[1].stackSize == 0) {
							this.slots[1] = this.slots[1].getItem().getContainerItem(this.slots[1]);
						}
					}
				}
			}
			
			if (this.isBurning() && this.canSmelt()) {
				this.cookTime++;
				
				if (this.cookTime == this.furnaceSpeed) {
					this.cookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
			} else {
				this.cookTime = 0;
			}
			
			if (flag != this.isBurning()) {
				flag1 = true;
				NetherBrickFurnace.updateNetherBrickFurnaceBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}
		
		if (flag1) {
			this.markDirty();
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if (side == 0) {
			return this.slots_bottom;
		} else if (side == 1) {
			return this.slots_top;
		} else {
			return this.slots_side;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return slot != 0 || slot != 1 || itemStack.getItem() == Items.bucket;
	}
	
	public int getBurnTimeRemainingScaled(int i) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = this.furnaceSpeed;
		}
		
		return this.burnTime * i / this.currentItemBurnTime;
	}
	
	public int getCookProgressScaled(int i) {
		return this.cookTime * i / this.furnaceSpeed;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		NBTTagList list = nbt.getTagList("Items", 10);
		this.slots = new ItemStack[this.getSizeInventory()];
		
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound compound = (NBTTagCompound)list.getCompoundTagAt(i);
			byte b = compound.getByte("Slot");
			
			if (b >= 0 && b < this.slots.length) {
				this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		
		this.burnTime = (int)nbt.getShort("BurnTime");
		this.cookTime = (int)nbt.getShort("CookTime");
		this.currentItemBurnTime = (int)nbt.getShort("CurrentItemBurnTime");
		
		if (nbt.hasKey("CustomName")) {
			this.localizedName = nbt.getString("CustomName");
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		nbt.setShort("BurnTime", (short)this.burnTime);
		nbt.setShort("CookTime", (short)this.cookTime);
		nbt.setShort("CurrentItemBurnTime", (short)this.currentItemBurnTime);
		
		NBTTagList list = new NBTTagList();
		
		for (int i = 0; i < this.slots.length; i++) {
			if (this.slots[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				this.slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}
		
		nbt.setTag("Items", list);
		
		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.localizedName);
		}
	}
}
