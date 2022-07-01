package me.myeolchi.obe

import io.github.monun.tap.fake.FakeEntity
import io.github.monun.tap.fake.FakeEntityServer
import io.github.monun.tap.fake.PlayerData
import org.bukkit.Location
import org.bukkit.entity.Player

object FakeBodyManager {
    private val server = FakeEntityServer.create(ObePlugin.instance)

    fun spawnAt(location: Location, playerData: PlayerData): FakeEntity<Player> {
        println("Created")
        return server.spawnPlayer(location, playerData)
    }

    fun join(player: Player) {
        server.addPlayer(player)
    }

    fun quit(player: Player) {
        server.removePlayer(player)
    }

    fun update() {
        server.update()
    }
}