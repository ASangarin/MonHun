package eu.asangarin.monhun.item;

import eu.asangarin.monhun.util.enums.MHItemTexture;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MHPickaxeItem extends MHToolItem {
	private final float miningSpeed;

	public MHPickaxeItem(int color, boolean mega, double breakChance) {
		super(MHItemTexture.PICKAXE, color, mega, breakChance);
		this.miningSpeed = mega ? 7.5F : 4.0F;
	}

	public MHPickaxeItem(int color, double breakChance) {
		this(color, false, breakChance);
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity user) {
		tryBreak(stack, world, user);
		return true;
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return BlockTags.PICKAXE_MINEABLE.contains(state.getBlock()) ? miningSpeed : 1.0F;
	}

	@Override
	public boolean isSuitableFor(BlockState state) {
		if (state.isIn(BlockTags.NEEDS_DIAMOND_TOOL) || (!isMega() && state.isIn(BlockTags.NEEDS_IRON_TOOL))) return false;
		else return state.isIn(BlockTags.PICKAXE_MINEABLE);
	}

	@Override
	public boolean isBugNet() {
		return false;
	}
}
