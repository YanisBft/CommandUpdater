package com.yanisbft.commandupdater.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IconButtonWidget extends ButtonWidget {
    protected final Identifier texture;
    protected final int textureWidth;
    protected final int textureHeight;

    public IconButtonWidget(int x, int y, Text message, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction onPress) {
        super(x, y, 20, 20, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.setTooltip(Tooltip.of(message));
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);
        int i = this.getX() + this.getWidth() / 2 - this.textureWidth / 2;
        int j = this.getY() + this.getHeight() / 2 - this.textureHeight / 2;
        context.drawGuiTexture(this.texture, i, j, this.textureWidth, this.textureHeight);
    }

    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
    }
}
