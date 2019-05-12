package timaxa007.return_block;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.BlockOre;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class EventsForge {

	@SubscribeEvent
	public void blockBreak(BlockEvent.BreakEvent event) {
		if (event.getPlayer().capabilities.isCreativeMode) return;
		ReturnBlockWSD returnBlock = ReturnBlockWSD.get(event.world);
		if (returnBlock == null) return;
		if (event.block instanceof BlockOre) {
			returnBlock.removingReturnBlock(event.x, event.y, event.z, event.world.getTotalWorldTime() + 60L, event.block, event.blockMetadata);
		}
	}

	@SubscribeEvent
	public void blockPlace(BlockEvent.PlaceEvent event) {
		ReturnBlockWSD returnBlock = ReturnBlockWSD.get(event.world);
		if (returnBlock == null) return;
		if (returnBlock.chunkLoad.isEmpty()) return;

		for (int i = 0; i < returnBlock.chunkLoad.size(); ++i) {
			ReturnBlockChunk tbc = returnBlock.chunkLoad.get(i);
			if (tbc.returnBlock.isEmpty()) continue;
			for (int j = 0; j < tbc.returnBlock.size(); ++j) {
				ReturnBlock tb = tbc.returnBlock.get(j);
				if (tb.x == event.x && tb.y == event.y && tb.z == event.z) 
					tbc.returnBlock.remove(j--);
			}
			if (tbc.returnBlock.isEmpty()) {
				tbc.returnBlock.clear();
				tbc.returnBlock.trimToSize();
			}
		}
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event) {
		ReturnBlockWSD.get(event.world);
	}

	@SubscribeEvent
	public void worldSave(WorldEvent.Save event) {
		ReturnBlockWSD returnBlock = ReturnBlockWSD.get(event.world);
		if (returnBlock == null) return;
		returnBlock.markDirty();
	}

	@SubscribeEvent
	public void chunkLoad(ChunkEvent.Load event) {
		ReturnBlockWSD returnBlock = ReturnBlockWSD.get(event.world);
		if (returnBlock == null) return;
		returnBlock.chunkLoad(event.getChunk());
	}

	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload event) {
		ReturnBlockWSD returnBlock = ReturnBlockWSD.get(event.world);
		if (returnBlock == null) return;
		returnBlock.chunkUnload(event.getChunk());
	}

}
