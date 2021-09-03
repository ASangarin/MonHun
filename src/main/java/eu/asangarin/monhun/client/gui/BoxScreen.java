package eu.asangarin.monhun.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.gui.ItemBoxScreenHandler;
import eu.asangarin.monhun.util.interfaces.ICustomSlotHighlight;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class BoxScreen extends HandledScreen<ScreenHandler> implements ICustomSlotHighlight {
	private static final Identifier BOX_TEXTURE = MonHun.i("textures/gui/box.png");

	public BoxScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.passEvents = false;
		this.backgroundHeight = 222;
		this.playerInventoryTitleX = -9999;
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BOX_TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, 6 * 18 + 17);
		this.drawTexture(matrices, i, j + 125, 0, 126, this.backgroundWidth, 96);
		if (handler instanceof ItemBoxScreenHandler boxHandler) {
			if (boxHandler.getPage() != 0) {
				if (isMouseOn(i - 23, j + 108, mouseX, mouseY)) {
					this.drawTexture(matrices, i - 23, j + 108, 199, 94, 23, 18);
					this.renderTooltip(matrices, new TranslatableText("gui.monhun.previous"), mouseX, mouseY);
				} else this.drawTexture(matrices, i - 23, j + 108, 176, 94, 23, 18);
				RenderSystem.setShaderTexture(0, BOX_TEXTURE);
			}
			if (boxHandler.hasItems()) {
				if (isMouseOn(i + this.backgroundWidth, j + 108, mouseX, mouseY)) {
					this.drawTexture(matrices, i + this.backgroundWidth, j + 108, 199, 112, 23, 18);
					this.renderTooltip(matrices, new TranslatableText("gui.monhun.next"), mouseX, mouseY);
				} else this.drawTexture(matrices, i + this.backgroundWidth, j + 108, 176, 112, 23, 18);
				RenderSystem.setShaderTexture(0, BOX_TEXTURE);
			}
		}
	}

	protected boolean isMouseOn(int x, int y, double pointX, double pointY) {
		return pointX >= (x - 1) && pointX < (x + 24) && pointY >= (y - 1) && pointY < (y + 19);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	public void drawCustomSlotHighlight(MatrixStack matrices, int x, int y, int z) {
		if (y > 130) {
			HandledScreen.drawSlotHighlight(matrices, x, y, z);
			return;
		}
		RenderSystem.setShaderTexture(0, BOX_TEXTURE);
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		drawTexture(matrices, x - 1, y - 1, 176, 0, 18, 18);
		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
	}


	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (handler instanceof ItemBoxScreenHandler boxHandler) {
			int i = (this.width - this.backgroundWidth) / 2;
			int j = (this.height - this.backgroundHeight) / 2;

			if (client != null) {
				int id = -1;
				if (isMouseOn(i - 23, j + 108, mouseX, mouseY) && boxHandler.getPage() > 0) id = 0;
				else if (isMouseOn(i + this.backgroundWidth, j + 108, mouseX, mouseY) && boxHandler.hasItems()) id = 1;

				if (id != -1 && boxHandler.onButtonClick(this.client.player, id)) {
					if (this.client.interactionManager != null) this.client.interactionManager.clickButton(boxHandler.syncId, id);
					return true;
				}
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void init() {
		super.init();
		titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
	}
}
