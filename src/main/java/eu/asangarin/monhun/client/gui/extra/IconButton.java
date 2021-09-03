package eu.asangarin.monhun.client.gui.extra;

import eu.asangarin.monhun.util.ItemIcon;
import lombok.Getter;
import net.minecraft.text.Text;

@Getter
public class IconButton extends MHButton {
	private final ItemIcon icon;

	public IconButton(int x, int y, int width, int height, Text name, ItemIcon icon) {
		super(x, y, width, height, name);
		this.icon = icon;
	}
}
