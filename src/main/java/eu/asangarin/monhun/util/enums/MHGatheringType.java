package eu.asangarin.monhun.util.enums;

import eu.asangarin.monhun.util.ItemColors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.StringIdentifiable;

@Getter
@RequiredArgsConstructor
public enum MHGatheringType implements StringIdentifiable {
	WHITE(MHRarity.RARE_1, ItemColors.WHITE), RED(MHRarity.RARE_2, ItemColors.RED), BLUE(MHRarity.RARE_2, ItemColors.BLUE),
	GOLD(MHRarity.RARE_4, ItemColors.YELLOW), BLACK(MHRarity.RARE_6, ItemColors.BLACK);

	private final MHRarity rarity;
	private final int color;

	public static MHGatheringType fromString(String value) {
		for (MHGatheringType type : MHGatheringType.values())
			if (type.asString().equalsIgnoreCase(value)) return type;
		return MHGatheringType.WHITE;
	}

	public static MHGatheringType fromStack(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt != null && nbt.contains("type")) return MHGatheringType.fromString(nbt.getString("type"));
		return MHGatheringType.WHITE;
	}

	public float getValue() {
		return ((float) ordinal()) / values().length;
	}

	@Override
	public String asString() {
		return name().toLowerCase();
	}

	public int getColor() {
		return color;
	}

	public String getTranslationKey() {
		return "block.monhun.gathering_color." + asString();
	}
}
