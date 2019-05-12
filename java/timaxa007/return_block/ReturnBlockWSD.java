package timaxa007.return_block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants.NBT;

public class ReturnBlockWSD extends WorldSavedData {

	public static final String ID = "ReturnBlockWSD";
	private World world;
	public final ArrayList<ReturnBlockChunk>
	chunkLoad = new ArrayList<ReturnBlockChunk>(),
	chunkUnload = new ArrayList<ReturnBlockChunk>();

	public ReturnBlockWSD(String tag) {
		super(tag);
	}

	public void update() {
		if (chunkLoad.isEmpty()) return;

		for (int i = 0; i < chunkLoad.size(); ++i) {
			ReturnBlockChunk tbc = chunkLoad.get(i);
			if (tbc.returnBlock.isEmpty()) continue;
			for (int j = 0; j < tbc.returnBlock.size(); ++j) {
				ReturnBlock tb = tbc.returnBlock.get(j);
				if (world.getTotalWorldTime() >= tb.time) {
					world.setBlock(tb.x, tb.y, tb.z, tb.block, tb.metadata, 3);
					tbc.returnBlock.remove(j--);
				}
			}
			if (tbc.returnBlock.isEmpty()) {
				tbc.returnBlock.clear();
				tbc.returnBlock.trimToSize();
			}
		}

	}

	public void returningReturnBlock(final int x, final int y, final int z, final long time, final Block block, final int metadata) {
		int chunkX = x / 16;
		int chunkZ = z / 16;
		int i;
		ReturnBlockChunk tbc = null;

		if (!chunkLoad.isEmpty())
			for (i = 0; i < chunkLoad.size(); ++i) {
				ReturnBlockChunk tbcc = chunkLoad.get(i);
				if (tbcc.chunkX == chunkX && tbcc.chunkZ == chunkZ) {
					tbc = tbcc;
					break;
				}
			}

		if (!chunkUnload.isEmpty() && tbc == null)
			for (i = 0; i < chunkUnload.size(); ++i) {
				ReturnBlockChunk tbcc = chunkUnload.get(i);
				if (tbcc.chunkX == chunkX && tbcc.chunkZ == chunkZ) {
					tbc = tbcc;
					break;
				}
			}

		if (tbc == null)
			tbc = new ReturnBlockChunk(chunkX, chunkZ);

		tbc.returnBlock.add(new ReturnBlock(x, y, z, time, block, metadata));

		if (world.getChunkProvider().chunkExists(chunkX, chunkZ))
			chunkLoad.add(tbc); else chunkUnload.add(tbc);
	}

	public void chunkLoad(Chunk chunk) {
		if (chunkUnload.isEmpty()) return;
		for (int i = 0; i < chunkUnload.size(); ++i) {
			ReturnBlockChunk tbc = chunkUnload.get(i);
			if (tbc.chunkX == chunk.xPosition && tbc.chunkZ == chunk.zPosition) {
				chunkLoad.add(tbc);
				chunkUnload.remove(i--);
				break;
			}
		}
	}

	public void chunkUnload(Chunk chunk) {
		if (chunkLoad.isEmpty()) return;
		for (int i = 0; i < chunkLoad.size(); ++i) {
			ReturnBlockChunk tbc = chunkLoad.get(i);
			if (tbc.chunkX == chunk.xPosition && tbc.chunkZ == chunk.zPosition) {
				chunkUnload.add(tbc);
				chunkLoad.remove(i--);
				break;
			}
		}
	}

	public static ReturnBlockWSD get(final World world) {
		if (world.mapStorage == null) return null;
		ReturnBlockWSD data = (ReturnBlockWSD)world.mapStorage.loadData(ReturnBlockWSD.class, ID);
		if (data == null) {
			data = new ReturnBlockWSD(ID);
			data.markDirty();
			world.mapStorage.setData(ID, data);
		}
		data.world = world;
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (!nbt.hasKey("tbc", NBT.TAG_LIST)) return;
		chunkLoad.clear();
		chunkUnload.clear();
		NBTTagList list = nbt.getTagList("tbc", NBT.TAG_COMPOUND);
		NBTTagCompound tagcomp;
		for (int i = 0; i < list.tagCount(); ++i) {
			tagcomp = list.getCompoundTagAt(i);
			ReturnBlockChunk tbc = new ReturnBlockChunk(tagcomp.getInteger("cX"), tagcomp.getInteger("cZ"));
			NBTTagList list2 = tagcomp.getTagList("tb", NBT.TAG_COMPOUND);
			NBTTagCompound tagcomp2;
			for (int j = 0; j < list2.tagCount(); ++j) {
				tagcomp2 = list2.getCompoundTagAt(j);
				tbc.returnBlock.add(new ReturnBlock(
						(tbc.chunkX * 16) + tagcomp2.getByte("x"),
						tagcomp2.getInteger("y"),
						(tbc.chunkZ * 16) + tagcomp2.getByte("z"),
						tagcomp2.getLong("time"),
						Block.getBlockById(tagcomp2.getShort("block")),
						tagcomp2.getByte("metadata")));
			}
			chunkUnload.add(tbc);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();

		if (!chunkLoad.isEmpty())
			for (int i = 0; i < chunkLoad.size(); ++i) {
				ReturnBlockChunk tbc = chunkLoad.get(i);
				if (tbc.returnBlock.isEmpty()) continue;
				list.appendTag(tbc.writeToNBT(new NBTTagCompound()));
			}

		if (!chunkUnload.isEmpty())
			for (int i = 0; i < chunkUnload.size(); ++i) {
				ReturnBlockChunk tbc = chunkUnload.get(i);
				if (tbc.returnBlock.isEmpty()) continue;
				list.appendTag(tbc.writeToNBT(new NBTTagCompound()));
			}

		if (list.tagCount() > 0) nbt.setTag("tbc", list);
	}

}
