package fr.hunh0w.dscore.listeners;

import fr.hunh0w.dscore.DSCore;
import fr.hunh0w.dscore.managers.LuckPermsManager;

import fr.hunh0w.dscore.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatManager implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent evt){
        evt.setCancelled(true);
        Player p = evt.getPlayer();
        String prefix = LuckPermsManager.getInstance().getPrefix(p.getName());
        if(prefix == null) prefix = "";

        String message = !p.hasPermission("chat.color") ? evt.getMessage() :
                ChatColor.translateAlternateColorCodes('&', evt.getMessage());

        Component text;

        String[] gradients = prefix.split("\\$");
        if(gradients.length == 2){
            text = MessageUtils.getGradient(gradients[1]+" "+p.getName(), true, gradients[0].split(":"));
        }else {
            text = Component.text(ChatColor.translateAlternateColorCodes('&', prefix+p.getName()));
        }

        text = text.append(Component.text(": ")
                    .color(TextColor.fromHexString("#2A2A2A"))
                .append(Component.text(message).color(TextColor.fromHexString("#ffffff")))
                    .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
        );

        DSCore.getInstance().adventure().players().sendMessage(text);
    }

}
