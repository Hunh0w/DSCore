package fr.hunh0w.dscore.managers;

import fr.hunh0w.dscore.DSCore;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.event.user.track.UserTrackEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Set;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent evt){
        Bukkit.getScheduler().scheduleSyncDelayedTask(DSCore.getInstance(), () -> {
            DaemonManager.getInstance().initDaemon(evt.getPlayer());
        }, 1);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent evt){
        if(evt.getNewGameMode() != GameMode.ADVENTURE && evt.getNewGameMode() != GameMode.SURVIVAL)
            return;
        DaemonManager.getInstance().initDaemon(evt.getPlayer());
    }

    @EventHandler
    public void onSpawn(PlayerRespawnEvent evt){
        Bukkit.getScheduler().scheduleSyncDelayedTask(DSCore.getInstance(), () -> {
            DaemonManager.getInstance().initDaemon(evt.getPlayer());
        }, 1);
    }

    public PlayerListener(){
        EventBus eventBus = LuckPermsManager.getInstance().getApi().getEventBus();
        eventBus.subscribe(DSCore.getInstance(), NodeMutateEvent.class, e -> {
            if(!e.isUser()) return;
            User user = (User) e.getTarget();
            Player p = Bukkit.getPlayer(user.getUsername());
            if(p != null){
                DaemonManager.getInstance().initDaemon(p);
                boolean wasDemon = containsDemonRank(e.getDataBefore());
                boolean nowDemon = containsDemonRank(e.getDataAfter());
                if(wasDemon && !nowDemon){
                    //TODO remove demon stuffs
                }else if(!wasDemon && nowDemon){
                    //TODO remove pourfendeur stuffs
                }
            }
        });
    }


    private boolean containsDemonRank(Set<Node> nodes){
        return nodes.stream().filter(node -> {
            if(node.getKey().equalsIgnoreCase("group.demon"))
                return true;
            if(node.getKey().equalsIgnoreCase("group.demon-sup"))
                return true;
            if(node.getKey().equalsIgnoreCase("group.lune-inf"))
                return true;
            if(node.getKey().equalsIgnoreCase("group.lune-sup"))
                return true;
            if(node.getKey().equalsIgnoreCase("group.kibutsuji"))
                return true;
            return false;
        }).count() > 0;
    }
}
