package com.projecturanus.terminalchat

import com.projecturanus.terminalchat.command.registerCommands
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.LogManager

const val MODID = "terminalchat"
val log = LogManager.getLogger(MODID)

@Mod(modid = MODID, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object TerminalChat {
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        registerCommands()
    }
}
