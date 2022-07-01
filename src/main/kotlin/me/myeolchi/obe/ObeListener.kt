package me.myeolchi.obe

import io.github.monun.tap.fake.PlayerData
import io.github.monun.tap.fake.invisible
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.ceil

class ObeListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        FakeBodyManager.join(e.player)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        FakeBodyManager.quit(e.player)
    }

    @EventHandler
    fun onObe(e: PlayerInteractEvent) {
        val result = e.player.world.rayTraceEntities(e.player.eyeLocation, e.player.eyeLocation.direction, 20.0) { entity -> entity is Player && entity != e.player }
        result?.hitEntity?.let { target ->
            println(target.name)
            val playerData = PlayerData(target.name, target.name)
            val initTargetPos = target.location.clone()
            val displacement = target.location.clone().subtract(e.player.eyeLocation).toVector()
            val loop = ceil(displacement.length()).toInt()
            val delta = displacement.multiply(1.0 / loop)

            var index = 0
            object: BukkitRunnable() {
                val current = e.player.eyeLocation.clone()
                override fun run() {
                    if (index++ == loop) {
                        if (initTargetPos.getNearbyEntities(2.0, 2.0, 2.0).contains(target)) {
                            target.world.spawnParticle(Particle.EXPLOSION_HUGE, initTargetPos, 20, 0.0, 0.0, 0.0)
                            val fake = FakeBodyManager.spawnAt(initTargetPos, playerData)

                            object: BukkitRunnable() {
                                override fun run() {
                                    target.velocity = delta
                                    object: BukkitRunnable() {
                                        override fun run() {
                                            (target as Player).allowFlight = true
                                            target.isFlying = true
                                        }
                                    }.runTaskLater(ObePlugin.instance, 10)
                                }
                            }.runTaskLater(ObePlugin.instance, 20)
                            
                            ObePlugin.immortal.add(target as Player)
                            target.isInvisible = true

                            // after five seconds
                            object: BukkitRunnable() {
                                override fun run() {
                                    if (target.gameMode == GameMode.SURVIVAL || target.gameMode == GameMode.SPECTATOR) {
                                        target.allowFlight = false
                                    }
                                    target.teleport(initTargetPos)
                                    ObePlugin.immortal.remove(target)
                                    target.isInvisible = false
                                    fake.remove()
                                }
                            }.runTaskLater(ObePlugin.instance, 100)
                        }

                        cancel()
                        return
                    }
                    println(current)
                    e.player.world.spawnParticle(Particle.SONIC_BOOM, current.add(delta), 1,0.0, 0.0, 0.0, 0.1)
                }
            }.runTaskTimer(ObePlugin.instance, 0, 5)
        }
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        if (ObePlugin.immortal.contains(e.entity)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBreakBlock(e: BlockBreakEvent) {
        if (ObePlugin.immortal.contains(e.player)) {
            e.isCancelled = true
        }
    }
}