package eu.asangarin.monhun.client.gui.extra;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.text.Text;

@Getter
@RequiredArgsConstructor
public class MHButton {
	private final int x, y, width, height;
	private final Text name;
}
