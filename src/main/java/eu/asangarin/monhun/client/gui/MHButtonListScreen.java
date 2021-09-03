package eu.asangarin.monhun.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.client.gui.extra.IconButton;
import eu.asangarin.monhun.client.gui.extra.MHButton;
import eu.asangarin.monhun.gui.MHButtonListScreenHandler;
import eu.asangarin.monhun.util.ItemIcon;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class MHButtonListScreen extends MHHandledScreen {
	private static final Identifier BOX_LIST_TEXTURE = MonHun.i("textures/gui/box_list.png");
	private final MHButtonListScreenHandler.TitleBoxType titleBox;

	public MHButtonListScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.titleBox = ((MHButtonListScreenHandler) handler).getTitleBox();
		this.passEvents = false;
		this.backgroundWidth = 125;
		this.backgroundHeight = 63;
		this.addButton(new IconButton(6, 25, 26, 26, new TranslatableText("gui.monhun.blacksmith.weapon"), ItemIcon.WEAPON));
		this.addButton(new IconButton(35, 25, 26, 26, new TranslatableText("gui.monhun.blacksmith.armor"), ItemIcon.ARMOR));
		this.addButton(new IconButton(64, 25, 26, 26, new TranslatableText("gui.monhun.blacksmith.decoration"), ItemIcon.DECORATION));
		this.addButton(new IconButton(93, 25, 26, 26, new TranslatableText("gui.monhun.blacksmith.mystery"), ItemIcon.MYSTERY));
	}

	@Override
	protected void init() {
		super.init();
		buttonX = (this.width - this.backgroundWidth) / 2;
		buttonY = ((this.height - this.backgroundHeight) / 2) + 12;
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BOX_LIST_TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		drawTexture(matrices, i, j + 13, this.getZOffset(), 0, 0, this.backgroundWidth, 55, 128, 128);
		drawTexture(matrices, (this.width - 77) / 2, j, this.getZOffset(), 0, 81 + titleBox.getOffset(), this.backgroundWidth, 13, 128, 128);
		drawButtons(matrices, mouseX, mouseY);
		RenderSystem.disableBlend();
	}

	@Override
	protected void drawButton(MatrixStack matrices, MHButton button, int mouseX, int mouseY) {
		drawTexture(matrices, buttonX + button.getX(), buttonY + button.getY(), this.getZOffset(), 0, 55, 26, 26, 128, 128);
		if (button instanceof IconButton iconButton) {
			renderIcon(itemRenderer, buttonX + button.getX() + 5, buttonY + button.getY() + 5, iconButton.getIcon());
			RenderSystem.setShaderTexture(0, BOX_LIST_TEXTURE);
		}
		if (isMouseOn(button, mouseX, mouseY)) {
			drawTexture(matrices, buttonX + button.getX(), buttonY + button.getY(), this.getZOffset(), 26, 55, 26, 26, 128, 128);
			int i = (this.width) / 2;
			int j = (this.height - this.backgroundHeight) / 2;
			drawCenteredTextWithShadow(matrices, textRenderer, button.getName().asOrderedText(), i, j + 20, 0xFFFFFF);
		}
		RenderSystem.setShaderTexture(0, BOX_LIST_TEXTURE);
	}

	private void renderIcon(ItemRenderer itemRenderer, int x, int y, ItemIcon icon) {
		itemRenderer.renderInGuiWithOverrides(icon.getIcon(), x, y);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
	}
}
