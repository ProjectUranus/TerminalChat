package com.projecturanus.terminalchat.command

import com.projecturanus.terminalchat.log
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer

/**
 * Command DSL specification
 */
fun command(init: CommandSpec.() -> Unit) = CommandSpec().also(init)

class CommandSpec {
    /**
     * Base command name. Also known as "full name"
     * Notice: All command parsing will bypass Minecraft command system
     */
    var baseName: String? = null

    /**
     * Uses in pair of short names.
     * Start symbols are like /, :, etc.
     */
    var startSymbol: String? = null

    /**
     * A short name of your command.
     * e.g. 'w' for "write", 'q' for "quit".
     * To avoid abuse, you must set a start symbol for this.
     */
    var shortName: String? = null

    val shortFullName = startSymbol + shortName

    /**
     * Arguments will keep being String if they are not deserialized
     */
    var onCommand: (server: MinecraftServer, sender: ICommandSender, arguments: Map<String, *>, fullText: String) -> Unit = { _, _, _, _ -> }

    var arguments = mutableMapOf<String, ArgumentSpec<*>>()

    fun register() {
        if (baseName == null) throw NullPointerException("Base name shouldn't be null")
        if ((startSymbol != null && shortName == null) || (startSymbol == null && shortName == null)) throw NullPointerException("Start symbol and short name must be both assigned")
        TerminalCommandManager.commands[baseName!!] = this
        log.info("Registered command $baseName")
    }

    inline fun <reified T> argument(name: String, optional: Boolean = false, noinline parser: ((String) -> T)?) {
        arguments[name] = ArgumentSpec(T::class.java, optional, parser)
    }
}

class ArgumentSpec<T>(val clazz: Class<T>, val optional: Boolean, val parser: ((String) -> T)?)
