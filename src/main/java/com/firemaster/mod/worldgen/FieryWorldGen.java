package com.firemaster.mod.worldgen;

import java.util.Random;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class FieryWorldGen implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		// Not generating end ores. If change mind on that, use case 1.
		switch (world.provider.dimensionId)
		{
		case 0:
			this.surfaceGeneration(world, random, chunkX * 16, chunkZ * 16);
			break;
		case -1:
			this.netherGeneration(world, random, chunkX * 16, chunkZ * 16);
			break;
		}
	}

	private void surfaceGeneration(World world, Random random, int x, int z) {
		this.spawnOre(FieryMod.oreEmberOre, world, random, x, z, 4, 8, 10, 0, 30, Blocks.stone);
	}

	private void netherGeneration(World world, Random random, int x, int z) {
		this.spawnOre(FieryMod.oreNetherEmberOre, world, random, x, z, 4, 8, 25, 0, 126, Blocks.netherrack);
		this.spawnOre(FieryMod.oreNetherIgnisOre, world, random, x, z, 2, 5, 12, 0, 126, Blocks.netherrack);
	}

	public void spawnOre(Block spawnBlock, World world, Random random, int xPos, int yPos, int minVeinSize, int maxVeinSize, int maxChunkVeins, int minY, int maxY, Block generateIn) {
		int veinSize = minVeinSize + random.nextInt(maxVeinSize - minVeinSize);
		int heightRange = maxY - minY;
		
		WorldGenMinable gen = new WorldGenMinable(spawnBlock, veinSize, generateIn);
		for (int i = 0; i < maxChunkVeins; i++) {
			int xRand = yPos + random.nextInt(16);
			int yRand = minY + random.nextInt(heightRange);
			int zRand = xPos + random.nextInt(16);
			gen.generate(world, random, xRand, yRand, zRand);
		}
	}
}
