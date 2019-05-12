package timaxa007.return_block;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class EventsFML {

	@SubscribeEvent
	public void tickWorld(TickEvent.WorldTickEvent event) {
		if (event.side.isClient()) return;
		ReturnBlockWSD returnBlockWSD = ReturnBlockWSD.get(event.world);
		if (returnBlockWSD == null) return;
		switch(event.phase) {
		case END:
			returnBlockWSD.update();
			break;
		default:break;
		}
	}

}
