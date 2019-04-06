package com.therandomlabs.vanilladeathchest;

import com.therandomlabs.randomlib.config.CommandConfigReload;
import com.therandomlabs.vanilladeathchest.config.VDCConfig;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
		modid = VanillaDeathChest.MOD_ID, version = VanillaDeathChest.VERSION,
		acceptedMinecraftVersions = VanillaDeathChest.ACCEPTED_MINECRAFT_VERSIONS,
		acceptableRemoteVersions = VanillaDeathChest.ACCEPTABLE_REMOTE_VERSIONS,
		guiFactory = VanillaDeathChest.GUI_FACTORY, updateJSON = VanillaDeathChest.UPDATE_JSON,
		certificateFingerprint = VanillaDeathChest.CERTIFICATE_FINGERPRINT
)
public final class VanillaDeathChest {
	public static final String MOD_ID = "vanilladeathchest";
	public static final String VERSION = "@VERSION@";
	public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.10,1.12)";
	public static final String ACCEPTABLE_REMOTE_VERSIONS = "*";
	public static final String GUI_FACTORY =
			"com.therandomlabs.vanilladeathchest.config.VDCGuiConfigFactory";
	public static final String UPDATE_JSON =
			"https://raw.githubusercontent.com/TheRandomLabs/VanillaDeathChest/misc/versions.json";
	public static final String CERTIFICATE_FINGERPRINT = "@FINGERPRINT@";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final boolean CUBIC_CHUNKS_LOADED = Loader.isModLoaded("cubicchunks");

	@SidedProxy(clientSide = "com.therandomlabs.vanilladeathchest.ClientProxy",
			serverSide = "com.therandomlabs.vanilladeathchest.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}

	@Mod.EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init();
	}

	@Mod.EventHandler
	public static void serverStarting(FMLServerStartingEvent event) {
		if(VDCConfig.Misc.vdcreload) {
			event.registerServerCommand(new CommandConfigReload(
					"vdcreload", VDCConfig.class, Side.SERVER,
					"VanillaDeathChest configuration reloaded!"
			));
		}
	}
}
