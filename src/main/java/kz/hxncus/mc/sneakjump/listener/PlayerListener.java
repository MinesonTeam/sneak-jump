package kz.hxncus.mc.sneakjump.listener;

import kz.hxncus.mc.sneakjump.SneakJump;
import kz.hxncus.mc.sneakjump.config.Config;
import kz.hxncus.mc.sneakjump.cooldown.CooldownService;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
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

        if (!config.isEnabled()) {
            return;
        }

        String permission = config.getPermission();
        if (permission != null) {
            permission = permission.trim();
            if (!permission.isEmpty() && !player.hasPermission(permission)) {
                return;
            }
        }

        World world = player.getWorld();
        if (!config.getAllowedWorlds().contains(world.getName())) {
            return;
        }

        int height = config.getJumpHeight();
        double gravity = config.getGravity();
        double multiplier = config.getMultiplier();

        if (height <= 1) {
            return;
        }

        if (player.getFoodLevel() < config.getFoodCost()) {
            ChatMessageType noEnergyMessageType = config.getNoEnergyMessageType();
            player.spigot().sendMessage(noEnergyMessageType, new TextComponent(config.getNoEnergyMessage()));
            return;
        }

        if (cooldownService.isOnCooldown(player.getUniqueId())) {
            ChatMessageType cooldownMessageType = config.getCooldownMessageType();
            if (cooldownMessageType == null) {
                return;
            }
            String message = config.getCooldownMessage();
            TextComponent component = new TextComponent(message.replace("{cooldown}",
                    String.valueOf((int) Math.ceil(cooldownService.getCooldown(player.getUniqueId()) / 1000D))));
            player.spigot().sendMessage(cooldownMessageType, component);
            return;
        }
        cooldownService.setCooldown(player.getUniqueId());

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

        int foodCost = config.getFoodCost();
        float saturationCost = config.getSaturationCost();
        float currentSaturation = player.getSaturation();

        if (currentSaturation > 0 && config.isSaturationFirst()) {
            if (currentSaturation >= foodCost) {
                player.setSaturation(currentSaturation - saturationCost);
            } else {
                player.setSaturation(0F);

                int remainingCost = (int) (saturationCost - currentSaturation);
                player.setFoodLevel(Math.max(0, player.getFoodLevel() - remainingCost));
            }
        } else {
            player.setFoodLevel(Math.max(0, player.getFoodLevel() - foodCost));
        }

        ChatMessageType energyMessageType = config.getEnergyMessageType();
        String energyMessage = config.getEnergyMessage();
        player.spigot().sendMessage(energyMessageType, new TextComponent(energyMessage.replace("{energy}", String.valueOf(player.getFoodLevel() + player.getSaturation()))));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldownService.removeCooldown(event.getPlayer().getUniqueId());
    }
}