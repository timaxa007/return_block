package timaxa007.return_block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ReturnBlockChunk {

	public final int chunkX, chunkZ;
	public final ArrayList<ReturnBlock> returnBlock = new ArrayList<ReturnBlock>();

	public ReturnBlockChunk(final int chunkX, final int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tagcomp) {
		tagcomp.setInteger("cX", chunkX);
		tagcomp.setInteger("cZ", chunkZ);

		NBTTagList list2 = new NBTTagList();
		NBTTagCompound tagcomp2;
		for (int j = 0; j < returnBlock.size(); ++j) {
			ReturnBlock tb = returnBlock.get(j);
			tagcomp2 = new NBTTagCompound();
			tagcomp2.setByte("x", (byte)((chunkX * 16) - tb.x));
			tagcomp2.setInteger("y", tb.y);
			tagcomp2.setByte("z", (byte)((chunkZ * 16) - tb.z));
			tagcomp2.setLong("time", tb.time);
			tagcomp2.setShort("block", (short)Block.getIdFromBlock(tb.block));
			tagcomp2.setByte("metadata", (byte)tb.metadata);
			list2.appendTag(tagcomp2);
		}
		if (list2.tagCount() > 0) tagcomp.setTag("tb", list2);
		return tagcomp;
	}

}
