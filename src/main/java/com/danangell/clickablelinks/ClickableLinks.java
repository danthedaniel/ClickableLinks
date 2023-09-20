package com.danangell.clickablelinks;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public class ClickableLinks extends JavaPlugin implements Listener {
    private static final String PERMISSION_NAME = "clickablelinks.enabled";
    private static final Pattern URL_REGEX = Pattern.compile(
            "https?://[a-z0-9]+(\\.[a-z0-9]+)*(\\.[a-z0-9]{1,10})((/+)[^/ ]*)*",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final TextReplacementConfig REPLACER = TextReplacementConfig.builder()
            .match(URL_REGEX)
            .replacement((c) -> c
                    .clickEvent(ClickEvent.openUrl(c.content()))
                    .color(TextColor.color(0x2a4dea))
                    .decorate(TextDecoration.UNDERLINED))
            .build();

    @Override
    public void onEnable() {
        Permission permission = new Permission(PERMISSION_NAME,
                "Enables the plugin for a user", PermissionDefault.OP);
        PluginManager pm = this.getServer().getPluginManager();
        pm.addPermission(permission);
        pm.registerEvents(this, this);
    }

    @EventHandler()
    public void on(final AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(PERMISSION_NAME)) {
            event.message(event.message().replaceText(REPLACER));
        }
    }
}
