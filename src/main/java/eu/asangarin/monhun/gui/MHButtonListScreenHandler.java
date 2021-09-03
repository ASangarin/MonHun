package eu.asangarin.monhun.gui;

import eu.asangarin.monhun.managers.MHScreens;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

public class MHButtonListScreenHandler extends ScreenHandler {
	@Getter
	private final TitleBoxType titleBox;

	public MHButtonListScreenHandler(int syncId, TitleBoxType titleBox) {
		super(MHScreens.BUTTON_LIST_SCREEN, syncId);
		this.titleBox = titleBox;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		System.out.println("Button '" + id + "' was clicked");
		return false;
	}

	@Override
	public void close(PlayerEntity player) {
	}

	public enum TitleBoxType {
		SMITHY, OTHER1, OTHER2;

		public int getOffset() {
			return ordinal() * 13;
		}
	}
}
