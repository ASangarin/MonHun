package eu.asangarin.monhun.block.gather;

import eu.asangarin.monhun.util.UtilityMethods;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.collection.DefaultedList;

public abstract class MHColorGatheringBlock extends MHGatheringBlock {
	public static final EnumProperty<MHGatheringType> GATHERING_TYPE = EnumProperty.of("type", MHGatheringType.class);

	public MHColorGatheringBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultStateWith(getStateManager().getDefaultState()).with(GATHERING_TYPE, MHGatheringType.WHITE));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(GATHERING_TYPE);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(GATHERING_TYPE, MHGatheringType.fromStack(ctx.getStack()));
	}

	@Override
	public BlockState cycle(BlockState state) {
		return state.with(GATHERING_TYPE, UtilityMethods.cycleEnum(MHGatheringType.class, state.get(GATHERING_TYPE)));
	}

	@Override
	public MHRarity getRarity(ItemStack stack) {
		return MHGatheringType.fromStack(stack).getRarity();
	}

	@Override
	public String getResourceKey(ItemStack stack) {
		return MHGatheringType.fromStack(stack).asString();
	}

	@Override
	public String getType(ItemStack stack) {
		return MHGatheringType.fromStack(stack).getTranslationKey();
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		for (MHGatheringType type : MHGatheringType.values())
			stacks.add(withType(type));
	}

	private ItemStack withType(MHGatheringType type) {
		ItemStack stack = new ItemStack(this);
		NbtCompound nbt = new NbtCompound();
		nbt.putString("type", type.asString());
		stack.setNbt(nbt);
		return stack;
	}
}
