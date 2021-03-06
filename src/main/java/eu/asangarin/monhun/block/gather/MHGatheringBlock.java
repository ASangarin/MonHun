package eu.asangarin.monhun.block.gather;

import eu.asangarin.monhun.block.entity.gather.MHAbstractGatheringBlockEntity;
import eu.asangarin.monhun.network.MHNetwork;
import eu.asangarin.monhun.network.MHS2CPackets;
import eu.asangarin.monhun.network.PacketHelper;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.interfaces.GatheringBlockEntityProvider;
import eu.asangarin.monhun.util.interfaces.IGatheringSpot;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class MHGatheringBlock extends Block implements GatheringBlockEntityProvider, IGatheringSpot {
	public static final Map<Class<? extends MHGatheringBlock>, AvailableResources> AVAILABLE_RESOURCES = new HashMap<>();

	public MHGatheringBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultStateWith(getStateManager().getDefaultState()));
	}

	protected abstract BlockState getDefaultStateWith(BlockState state);

	@Override
	@SuppressWarnings({"ConstantConditions", "deprecation"})
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (player.getAbilities().creativeMode) {
			if (!world.isClient) {
				if (!player.isSneaking()) world.setBlockState(pos, cycle(state));
				else if (world.getBlockEntity(pos) instanceof MHAbstractGatheringBlockEntity gatherEntity)
					gatherEntity.setShiny(!gatherEntity.isShiny());
			}

			return ActionResult.SUCCESS;
		} else if (canGather(stack)) {
			playClientGatherEffects(world, pos);

			if (!world.isClient) {
				LootTable lootTable = world.getServer().getLootManager().getTable(getLootTableIdent());
				LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.BLOCK_STATE, state);
				lootTable.generateLoot(builder.build(LootContextType.create().require(LootContextParameters.BLOCK_STATE).build()),
						(loot) -> player.getInventory().offerOrDrop(loot));
				if (!player.getAbilities().creativeMode) tryBreak(stack, world, player);
				playServerGatherEffects(world, pos);
				if (world.getBlockEntity(pos) instanceof MHAbstractGatheringBlockEntity gatherEntity && gatherEntity.gather()) {
					for (ServerPlayerEntity serverPlayer : PlayerLookup.tracking(gatherEntity))
						MHNetwork.sendToClient(MHS2CPackets.BLOCK_BREAK, serverPlayer, PacketHelper.blockPos(pos));
					world.removeBlock(pos, true);
				}
			}
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	protected void tryBreak(ItemStack stack, World world, PlayerEntity player) {
	}

	protected abstract boolean canGather(ItemStack stack);

	protected void playClientGatherEffects(World world, BlockPos pos) {
	}

	protected void playServerGatherEffects(World world, BlockPos pos) {
	}

	protected abstract Identifier getLootTableIdent();

	protected BlockState cycle(BlockState state) {
		return state;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(getRarity(stack).asText().formatted(Formatting.BOLD));
		final List<Text> resources = getAvailableResources(stack);
		if (resources == null || resources.isEmpty()) return;
		tooltip.add(new TranslatableText("block.monhun.possible_resources").formatted(Formatting.GRAY).formatted(Formatting.UNDERLINE));
		for (Text text : resources)
			tooltip.add(text.copy().formatted(Formatting.AQUA));
	}

	protected abstract MHRarity getRarity(ItemStack stack);

	protected abstract String getResourceKey(ItemStack stack);

	private List<Text> getAvailableResources(ItemStack stack) {
		if (!AVAILABLE_RESOURCES.containsKey(getClass())) return Collections.emptyList();
		AvailableResources resources = AVAILABLE_RESOURCES.get(getClass());
		return resources.get(getResourceKey(stack));
	}

	@Override
	public String getType(ItemStack stack) {
		return "base";
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World abstractWorld, BlockState blockState, BlockEntityType<T> type) {
		return type.supports(blockState) ? (world, pos, state, be) -> tick(be) : null;
	}

	public static void tick(BlockEntity be) {
		MHAbstractGatheringBlockEntity gatherEntity = (MHAbstractGatheringBlockEntity) be;
		if (gatherEntity.isShiny()) MHAbstractGatheringBlockEntity.shinyTick(gatherEntity);
	}

	public double getShinyY(Random random) {
		return random.nextDouble() * 0.8 + 0.4d;
	}

	public static class AvailableResources {
		Map<String, List<Text>> available = new HashMap<>();

		public void clear() {
			this.available.clear();
		}

		public void putAll(Map<String, List<Text>> available) {
			this.available.putAll(available);
		}

		public List<Text> get(String type) {
			return this.available.get(type);
		}
	}
}
