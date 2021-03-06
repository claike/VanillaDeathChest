package com.therandomlabs.vanilladeathchest.api.deathchest;

import java.util.Map;

import com.therandomlabs.vanilladeathchest.api.event.DeathChestRemoveEvent;
import com.therandomlabs.vanilladeathchest.world.storage.VDCSavedData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public final class DeathChestManager {
	private DeathChestManager() {}

	public static void addDeathChest(
			World world, EntityPlayer player, BlockPos pos,
			boolean isDoubleChest
	) {
		final VDCSavedData data = VDCSavedData.get(world);
		final Map<BlockPos, DeathChest> deathChests = data.getDeathChests();
		final DeathChest deathChest = new DeathChest(
				world, player.getUniqueID(), world.getTotalWorldTime(), pos, isDoubleChest, false
		);

		deathChests.put(pos, deathChest);

		if (isDoubleChest) {
			deathChests.put(pos.east(), deathChest);
		}

		data.markDirty();
	}

	public static boolean isDeathChest(World world, BlockPos pos) {
		return getDeathChest(world, pos) != null;
	}

	public static boolean isLocked(World world, BlockPos pos) {
		final DeathChest deathChest = getDeathChest(world, pos);
		return deathChest != null && !deathChest.isUnlocked();
	}

	public static DeathChest getDeathChest(World world, BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();

		if (block != Blocks.CHEST && !(block instanceof BlockShulkerBox)) {
			return null;
		}

		final Map<BlockPos, DeathChest> deathChests = VDCSavedData.get(world).getDeathChests();
		return deathChests.get(pos);
	}

	public static DeathChest removeDeathChest(World world, BlockPos pos) {
		final Map<BlockPos, DeathChest> deathChests = VDCSavedData.get(world).getDeathChests();
		final DeathChest chest = deathChests.remove(pos);

		if (chest == null) {
			return null;
		}

		final BlockPos west;
		final BlockPos east;

		if (chest.isDoubleChest()) {
			if (chest.getPos().equals(pos)) {
				west = pos;
				east = pos.east();
			} else {
				west = pos.west();
				east = pos;
			}
		} else {
			west = pos;
			east = null;
		}

		MinecraftForge.EVENT_BUS.post(new DeathChestRemoveEvent(chest, west, east));

		return chest;
	}
}
