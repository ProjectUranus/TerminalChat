package com.projecturanus.terminalchat.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ChatLine
import net.minecraft.client.gui.GuiNewChat
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * An object here to avoid lambda invoking in server side
 */
@SideOnly(Side.CLIENT)
object ReflectionExtensions {
    val GuiNewChat.theChatLines
        by lazy { ObfuscationReflectionHelper.findField(GuiNewChat::class.java, "field_146252_h").get(Minecraft.getMinecraft().ingameGUI.chatGUI) as MutableList<ChatLine> }
}
