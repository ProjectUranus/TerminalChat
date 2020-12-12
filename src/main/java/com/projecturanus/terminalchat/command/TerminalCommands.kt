package com.projecturanus.terminalchat.command

import net.minecraft.command.CommandBase

fun registerCommands() {
    command {
        baseName = ":quit"
        startSymbol = ":"
        shortName = "q"
        onCommand = { server, sender, arguments, fullText ->
            server.addScheduledTask {
                if (server.worlds != null && server.getCommandManager().commands["stop"] != null) {
                    CommandBase.notifyCommandListener(sender, server.getCommandManager().commands["stop"]!!, "commands.stop.start")
                }

                server.initiateShutdown()
            }
        }
        register()
    }
}
