package kz.hxncus.mc.sneakjump.config;

import kz.hxncus.mc.sneakjump.SneakJump;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Config part of the SneakJump Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class Config {
    private final SneakJump plugin;
    private FileConfiguration config;

    private Set<String> allowedWorlds;
    private List<String> helpMessages;

    public Config(SneakJump plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reload() {
        this.plugin.reloadConfig();
        this.config = plugin.getConfig();
        this.allowedWorlds = null;
        this.helpMessages = null;
    }

    public boolean isEnabled() {
        return config.getBoolean("enabled", true);
    }

    public double getGravity() {
        return config.getDouble("gravity", 0.0882);
    }

    public int getJumpHeight() {
        return config.getInt("jump-height", 2);
    }

    public double getMultiplier() {
        return config.getDouble("multiplier", 0.997);
    }

    public long getCooldown() {
        return config.getLong("cooldown", 3000);
    }

    public boolean isEffectsEnabled() {
        return config.getBoolean("effects.enabled", true);
    }

    public boolean isSoundEffectEnabled() {
        return config.getBoolean("effects.sound.enabled", true);
    }

    public boolean isParticleEffectEnabled() {
        return config.getBoolean("effects.particle.enabled", true);
    }

    public Sound getSoundEffect() {
        try {
            String soundEffectStr = config.getString("effects.sound.type", "ENTITY_BAT_TAKEOFF").toUpperCase(Locale.ENGLISH);
            return Sound.valueOf(soundEffectStr);
        } catch (IllegalArgumentException ignored) {
            return Sound.ENTITY_BAT_TAKEOFF;
        }
    }

    public float getSoundEffectVolume() {
        return (float) config.getDouble("effects.sound.volume", 1.0);
    }

    public float getSoundEffectPitch() {
        return (float) config.getDouble("effects.sound.pitch", 1.0);
    }

    public boolean isErrorSoundEffectEnabled() {
        return config.getBoolean("effects.error-sound.enabled", true);
    }

    public Sound getErrorSoundEffect() {
        try {
            String errorSoundEffectStr = config.getString("effects.error-sound.type", "ENTITY_VILLAGER_NO").toUpperCase(Locale.ENGLISH);
            return Sound.valueOf(errorSoundEffectStr);
        } catch (IllegalArgumentException ignored) {
            return Sound.ENTITY_VILLAGER_NO;
        }
    }

    public float getErrorSoundEffectVolume() {
        return (float) config.getDouble("effects.error-sound.volume", 1.0);
    }

    public float getErrorSoundEffectPitch() {
        return (float) config.getDouble("effects.error-sound.pitch", 1.0);
    }

    public Particle getParticleEffectType() {
        try {
            String particleEffectTypeStr = config.getString("effects.particle.type", "CLOUD").toUpperCase(Locale.ENGLISH);
            return Particle.valueOf(particleEffectTypeStr);
        } catch (IllegalArgumentException ignored) {
            return Particle.CLOUD;
        }
    }

    public int getParticleEffectCount() {
        return config.getInt("effects.particle.count", 5);
    }

    public double getParticleEffectOffsetX() {
        return config.getDouble("effects.particle.offset.x", 0.5);
    }

    public double getParticleEffectOffsetY() {
        return config.getDouble("effects.particle.offset.y", 0.5);
    }

    public double getParticleEffectOffsetZ() {
        return config.getDouble("effects.particle.offset.z", 0.5);
    }

    public double getParticleEffectSpawnOffsetX() {
        return config.getDouble("effects.particle.spawn-offset.x", 0.5);
    }

    public double getParticleEffectSpawnOffsetY() {
        return config.getDouble("effects.particle.spawn-offset.y", 0.5);
    }

    public double getParticleEffectSpawnOffsetZ() {
        return config.getDouble("effects.particle.spawn-offset.z", 0.5);
    }

    public double getParticleEffectExtra() {
        return config.getDouble("effects.particle.extra", 0);
    }

    public int getFoodCost() {
        return config.getInt("food-cost", 1);
    }

    public boolean isSaturationFirst() {
        return config.getBoolean("saturation-first", true);
    }

    public float getSaturationCost() {
        return (float) config.getDouble("saturation-cost", 1.0D);
    }

    public ChatMessageType getCooldownMessageType() {
        try {
            return ChatMessageType.valueOf(config.getString("cooldown-message-type"));
        } catch (IllegalArgumentException ignored) {
            return ChatMessageType.ACTION_BAR;
        }
    }

    public String getCooldownMessage() {
        return config.getString("cooldown-message");
    }

    public ChatMessageType getEnergyMessageType() {
        try {
            String energyMessageTypeStr = config.getString("energy-message-type", "ACTION_BAR").toUpperCase(Locale.ENGLISH);
            return ChatMessageType.valueOf(energyMessageTypeStr);
        } catch (IllegalArgumentException ignored) {
            return ChatMessageType.ACTION_BAR;
        }
    }

    public String getEnergyMessage() {
        return config.getString("energy-message", "§8[§aSneakJump§8] §fYou have {energy} energy left.");
    }

    public ChatMessageType getNoEnergyMessageType() {
        try {
            String noEnergyMessageTypeStr = config.getString("no-energy-message-type", "ACTION_BAR").toUpperCase(Locale.ENGLISH);
            return ChatMessageType.valueOf(noEnergyMessageTypeStr);
        } catch (IllegalArgumentException ignored) {
            return ChatMessageType.ACTION_BAR;
        }
    }

    public String getNoEnergyMessage() {
        return config.getString("no-energy-message", "§8[§aSneakJump§8] §fYou don't have enough energy to sneak jump.");
    }

    public Set<String> getAllowedWorlds() {
        if (allowedWorlds == null) {
            allowedWorlds = new HashSet<>(config.getStringList("allowed-worlds"));
        }
        return allowedWorlds;
    }

    public String getPermission() {
        return config.getString("permission", "sneakjump.jump");
    }

    public String getReloadMessage() {
        return config.getString("reload-message");
    }

    public List<String> getHelpMessages() {
        if (helpMessages == null) {
            helpMessages = config.getStringList("help-messages");
        }
        return helpMessages;
    }
}