package eu.asangarin.monhun.client.gui;

import eu.asangarin.monhun.client.gui.extra.MHButton;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public abstract class MHHandledScreen extends HandledScreen<ScreenHandler> {
	protected int buttonX, buttonY;
	private final Map<MHButton, Integer> buttons = new HashMap<>();

	public MHHandledScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	protected abstract void drawButton(MatrixStack matrices, MHButton button, int mouseX, int mouseY);

	protected boolean isMouseOn(MHButton button, double mouseX, double mouseY) {
		return mouseX >= (buttonX + button.getX() - 1) && mouseX < (buttonX + button.getX() + button.getWidth() + 1) && mouseY >= (buttonY + button.getY() - 1) && mouseY < (buttonY + button.getY() + button.getHeight() + 1);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (handleClick(mouseX, mouseY)) return true;
		return super.mouseClicked(mouseX, mouseY, button);
	}

	protected void drawButtons(MatrixStack matrices, int mouseX, int mouseY) {
		for (MHButton button : buttons.keySet())
			drawButton(matrices, button, mouseX, mouseY);
	}

	protected void addButton(MHButton button) {
		buttons.put(button, buttons.size());
	}

	public boolean handleClick(double mouseX, double mouseY) {
		if (client != null) {
			int id = -1;
			for (MHButton button : buttons.keySet())
				if (isMouseOn(button, mouseX, mouseY)) id = buttons.get(button);

			if (id != -1 && handler.onButtonClick(this.client.player, id)) {
				if (this.client.interactionManager != null) this.client.interactionManager.clickButton(handler.syncId, id);
				return true;
			}
		}

		return false;
	}
}
