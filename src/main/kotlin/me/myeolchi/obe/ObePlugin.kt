package me.myeolchi.obe

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ObePlugin: JavaPlugin() {
    companion object {
        lateinit var instance: ObePlugin
            private set

        val immortal = ArrayList<Player>()
    }

    override fun onEnable() {
        instance = this
        server.pluginManager.registerEvents(ObeListener(), this)
        server.scheduler.runTaskTimer(this, FakeBodyManager::update, 0L, 1L)

        Bukkit.getOnlinePlayers().forEach {
            println("Joined!")
            FakeBodyManager.join(it)
        }
    }
}