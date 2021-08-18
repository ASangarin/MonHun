package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Getter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

import java.util.Random;

public class MHToolItem extends MHBaseItem {
	protected static final Random random = new Random();

	@Getter
	private final boolean mega;
	private final double breakChance;

	public MHToolItem(MHItemTexture texture, int color, boolean mega, double breakChance) {
		super(texture, color, mega ? MHRarity.RARE_3 : MHRarity.RARE_1, 5, MonHun.TOOL_GROUP);
		this.mega = mega;
		this.breakChance = breakChance;
	}

	public void tryBreak(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient && random.nextDouble() < breakChance) {
			user.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
			stack.decrement(1);
			if (user instanceof PlayerEntity) ((PlayerEntity) user).incrementStat(Stats.BROKEN.getOrCreateStat(stack.getItem()));
		}
	}

	public boolean isBugNet() {
		return true;
	}
}
