package com.projecturanus.terminalchat.command

import com.projecturanus.terminalchat.MODID
import com.projecturanus.terminalchat.util.ReflectionExtensions.theChatLines
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ChatLine
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * All terminal commands goes here.
 */
@Mod.EventBusSubscriber(modid = MODID)
object TerminalCommandManager {
    val commands = mutableMapOf<String, CommandSpec>()

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    @JvmStatic
    fun parseServerCommand(event: ServerChatEvent) {
        for ((name, command) in commands) {
            if (event.message.startsWith(name) || event.message.startsWith(command.shortFullName)) {
                command.onCommand(event.player.server, event.player, emptyMap<String, Any>(), event.message)
                event.isCanceled = true
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    @JvmStatic
    fun parseClientCommand(event: ClientChatEvent) {
        val globalPattern = Regex(":%s/(.+)/(.+)/g")
        val pattern = Regex(":%s/(.+)/(.+)")
        var isGlobal: Boolean? = null
        val input: String?
        val output: String?
        when {
            event.message.matches(globalPattern) -> {
                val args = pattern.matchEntire(event.message)?.groupValues?.get(1)?.split("/")
                input = args?.get(0)
                output = args?.get(1)
                if (input != null && output != null) {
                    isGlobal = true
                }
            }
            event.message.matches(pattern) -> {
                val args = pattern.matchEntire(event.message)?.groupValues?.get(1)?.split("/")
                input = args?.get(0)
                output = args?.get(1)
                if (input != null && output != null) {
                    isGlobal = false
                }
            }
            else -> return
        }

        if (isGlobal != null) {
            val iterator = Minecraft.getMinecraft().ingameGUI.chatGUI.theChatLines.listIterator()
            var replaced = 0
            while (iterator.hasNext()) {
                val chatLine = iterator.next()
                val component = chatLine.chatComponent as TextComponentTranslation
                if (chatLine.chatComponent.unformattedComponentText.contains(Regex(input!!))) {
                    component.formatArgs[1] = TextComponentString((component.formatArgs[1] as TextComponentString).text.replace(Regex(input), output!!))
                    iterator.set(ChatLine(chatLine.updatedCounter, TextComponentTranslation(component.key, *component.formatArgs), chatLine.chatLineID))
                    replaced++
                    if (isGlobal == false) break
                }
            }
            event.isCanceled = true
            Minecraft.getMinecraft().player.sendMessage(TextComponentString("$replaced substitutions on $replaced lines"))
            Minecraft.getMinecraft().ingameGUI.chatGUI.refreshChat()
        }
    }
}
