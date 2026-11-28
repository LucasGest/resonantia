package com.lucanemone.resonantia.client.screen;

import com.lucanemone.resonantia.Resonantia;
import com.lucanemone.resonantia.menu.ConcasseurMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ConcasseurScreen extends AbstractContainerScreen<ConcasseurMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Resonantia.MOD_ID, "textures/gui/container/concasseur.png");

    private static final int TEXTURE_WIDTH = 200;
    private static final int TEXTURE_HEIGHT = 166;
    private static final int ENERGY_BAR_HEIGHT = 52;

    public ConcasseurScreen(ConcasseurMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();
        if (progress > 0) {
            int filled = (int) ((float) progress / maxProgress * 24);
            guiGraphics.blit(TEXTURE, x + 79, y + 34, 176, 0, filled, 17, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }

        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();
        if (maxEnergy > 0 && energy > 0) {
            int filledHeight = (int) ((float) energy / maxEnergy * ENERGY_BAR_HEIGHT);
            guiGraphics.blit(
                    TEXTURE,
                    x + 152, y + 17 + (ENERGY_BAR_HEIGHT - filledHeight),
                    176, 20 + (ENERGY_BAR_HEIGHT - filledHeight),
                    14, filledHeight,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, 8, 6, 0xC8D0DA, false);
        guiGraphics.drawString(font, playerInventoryTitle, 8, imageHeight - 94, 0xC8D0DA, false);
        guiGraphics.drawString(font, "Fréquence: " + menu.getFrequency(), 60, 60, 0xC8D0DA, false);
    }

    // Info-bulles : objets des cases, énergie stockée
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (isHovering(152, 17, 14, ENERGY_BAR_HEIGHT, mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, Component.translatable("gui.resonantia.energy", menu.getEnergy(), menu.getMaxEnergy()), mouseX, mouseY);
        } else {
            renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }
}
