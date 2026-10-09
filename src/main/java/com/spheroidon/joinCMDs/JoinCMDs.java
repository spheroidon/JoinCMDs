package com.spheroidon.joinCMDs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class JoinCMDs extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("JoinCMDs is enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling JoinCMDs...");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FileConfiguration config = getConfig();
        Player player = event.getPlayer();

        List<String> commands = config.getStringList("JoinCommands");
        if (commands.isEmpty()) {
            return;
        }

        CommandSender sender = config.getBoolean("Quiet", true)
                ? Bukkit.createCommandSender(feedback -> { })
                : Bukkit.getConsoleSender();

        for (String command : commands) {
            String parsed = command
                    .replace("{player}", player.getName())
                    .replace("{uuid}", player.getUniqueId().toString())
                    .trim();

            if (parsed.startsWith("/")) {
                parsed = parsed.substring(1);
            }

            if (parsed.isEmpty()) {
                continue;
            }

            Bukkit.dispatchCommand(sender, parsed);
        }
    }
}