package eu.asangarin.monhun.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NbtCompound;

public class EntityTickableComponent implements Component {
	@Getter
	@Setter
	private boolean tickable = true;

	@Override
	public void readFromNbt(NbtCompound tag) {
		tickable = tag.getBoolean("tickable");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("tickable", tickable);
	}
}
