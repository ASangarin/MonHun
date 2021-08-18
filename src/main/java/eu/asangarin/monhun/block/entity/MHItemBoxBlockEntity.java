package eu.asangarin.monhun.block.entity;

import eu.asangarin.monhun.gui.ItemBoxScreenHandler;
import eu.asangarin.monhun.managers.MHBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MHItemBoxBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
	public MHItemBoxBlockEntity(BlockPos pos, BlockState state) {
		super(MHBlocks.ITEM_BOX_ENTITY, pos, state);
	}

	@Override
	public Text getDisplayName() {
		return new TranslatableText(getCachedState().getBlock().getTranslationKey()).formatted(Formatting.WHITE);
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new ItemBoxScreenHandler(syncId, inv);
	}
}
