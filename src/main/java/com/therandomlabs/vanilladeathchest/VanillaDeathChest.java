package com.therandomlabs.vanilladeathchest;

import com.therandomlabs.vanilladeathchest.api.event.BlockEvent;
import com.therandomlabs.vanilladeathchest.api.event.GameRuleEvent;
import com.therandomlabs.vanilladeathchest.api.event.PlayerEvent;
import com.therandomlabs.vanilladeathchest.command.CommandVDCReload;
import com.therandomlabs.vanilladeathchest.config.VDCConfig;
import com.therandomlabs.vanilladeathchest.handler.DeathChestDropHandler;
import com.therandomlabs.vanilladeathchest.handler.DeathChestInteractionHandler;
import com.therandomlabs.vanilladeathchest.handler.DeathChestPlaceHandler;
import com.therandomlabs.vanilladeathchest.handler.VDCGameRuleAdder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.commands.CommandRegistry;
import net.fabricmc.fabric.events.PlayerInteractionEvent;
import net.fabricmc.fabric.events.TickEvent;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class VanillaDeathChest implements ModInitializer {
	public static final String MOD_ID = "vanilladeathchest";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		VDCConfig.reload();

		if(!VDCConfig.misc.gameRuleName.isEmpty()) {
			GameRuleEvent.MODIFY.register(new VDCGameRuleAdder());
		}

		if(VDCConfig.misc.vdcreload) {
			CommandRegistry.INSTANCE.register(false, CommandVDCReload::register);
		}

		final DeathChestPlaceHandler placeHandler = new DeathChestPlaceHandler();

		PlayerEvent.DROP_ALL_ITEMS.register(placeHandler);
		TickEvent.SERVER.register(placeHandler);

		final DeathChestInteractionHandler interactionHandler = new DeathChestInteractionHandler();

		PlayerInteractionEvent.ATTACK_BLOCK.register(interactionHandler);
		PlayerInteractionEvent.INTERACT_BLOCK.register(interactionHandler);

		final DeathChestDropHandler dropHandler = new DeathChestDropHandler();

		BlockEvent.GET_DROP.register(dropHandler);
		TickEvent.SERVER.register(dropHandler);
	}

	public static void crashReport(String message, Throwable throwable) {
		throw new CrashException(new CrashReport(message, throwable));
	}
}
