package timaxa007.return_block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = ReturnBlockMod.MODID, name = ReturnBlockMod.NAME, version = ReturnBlockMod.VERSION)
public class ReturnBlockMod {

	public static final String
	MODID = "return_block",
	NAME = "Returnorary Block Mod",
	VERSION = "0.02";

	@Mod.Instance(MODID)
	public static ReturnBlockMod instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventsForge());
		FMLCommonHandler.instance().bus().register(new EventsFML());
	}

}
