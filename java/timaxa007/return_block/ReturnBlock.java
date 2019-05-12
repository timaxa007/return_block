package timaxa007.return_block;

import net.minecraft.block.Block;

public class ReturnBlock {

	public final int x, y, z;
	public final long time;
	public final Block block;
	public final int metadata;

	public ReturnBlock(final int x, final int y, final int z, final long time, final Block block, final int metadata) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.time = time;
		this.block = block;
		this.metadata = metadata;
	}

}
