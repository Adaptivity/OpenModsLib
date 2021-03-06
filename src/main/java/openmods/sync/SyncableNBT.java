package openmods.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

/***
 * Note: you must manually .markDirty() right now
 */
public class SyncableNBT extends SyncableObjectBase implements ISyncableValueProvider<NBTTagCompound> {

	private NBTTagCompound tag;

	public SyncableNBT() {
		tag = new NBTTagCompound();
	}

	public SyncableNBT(NBTTagCompound nbt) {
		tag = (NBTTagCompound)nbt.copy();
	}

	@Override
	public NBTTagCompound getValue() {
		return (NBTTagCompound)tag.copy();
	}

	public void setValue(NBTTagCompound tag) {
		this.tag = (NBTTagCompound)tag.copy();
	}

	@Override
	public void readFromStream(DataInputStream stream) throws IOException {
		if (stream.readBoolean()) {
			tag = CompressedStreamTools.readCompressed(stream);
		} else {
			tag = null;
		}

	}

	@Override
	public void writeToStream(DataOutputStream stream, boolean fullData) throws IOException {
		if (tag == null) {
			stream.writeBoolean(false);
		} else {
			stream.writeBoolean(true);
			CompressedStreamTools.writeCompressed(tag, stream);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String name) {
		nbt.setTag(name, nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String name) {
		nbt.getCompoundTag(name);
	}

}
