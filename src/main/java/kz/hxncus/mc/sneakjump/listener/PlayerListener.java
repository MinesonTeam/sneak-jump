package kz.hxncus.mc.sneakjump.listener;

import kz.hxncus.mc.sneakjump.SneakJump;
import kz.hxncus.mc.sneakjump.config.Config;
import kz.hxncus.mc.sneakjump.cooldown.CooldownService;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.util.Vector;

/**
 * PlayerListener part of the SneakJump Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class PlayerListener implements Listener {
    private final SneakJump plugin;
    private final Config config;
    private final CooldownService cooldownService;

    public PlayerListener(SneakJump plugin, Config config, CooldownService cooldownService) {
        this.plugin = plugin;
        this.config = config;
        this.cooldownService = cooldownService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJump(PlayerStatisticIncrementEvent event) {
        Statistic statistic = event.getStatistic();

        if (statistic != Statistic.JUMP) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            return;
        }

        if (!config.isEnabled() || !player.hasPermission(config.getPermission())) {
            return;
        }

        World world = player.getWorld();
        if (!config.getAllowedWorlds().contains(world.getName())) {
            return;
        }

        if (cooldownService.isOnCooldown(player.getUniqueId())) {
            ChatMessageType messageType = config.getCooldownMessageType();
            if (messageType == null) {
                return;
            }
            String message = config.getCooldownMessage();
            TextComponent component = new TextComponent(message.replace("{cooldown}",
                        cooldownService.getCooldown(player.getUniqueId()) + ""));
            player.spigot().sendMessage(messageType, component);
            return;
        }
        cooldownService.setCooldown(player.getUniqueId());

        int height = config.getJumpHeight();
        double gravity = config.getGravity();
        double multiplier = config.getMultiplier();

        if (height <= 1) {
            return;
        }

        double vecY = Math.sqrt(2 * gravity * height);

        for (int i = 0; i <= height; i++) {
            if (vecY > 1) {
                vecY /= multiplier;
            }
        }
        
        Vector velocity = player.getVelocity().clone();
        velocity.multiply(2);
        velocity.setY(vecY);
        player.setVelocity(velocity);
    }
}