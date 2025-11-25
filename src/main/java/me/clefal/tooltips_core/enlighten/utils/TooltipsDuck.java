package me.clefal.tooltips_core.enlighten.utils;

import net.minecraft.network.chat.Component;

public interface TooltipsDuck {
    void setMessage(Component component);
    Component getMessage();
}
