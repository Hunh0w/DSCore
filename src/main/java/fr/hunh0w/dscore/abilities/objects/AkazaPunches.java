package fr.hunh0w.dscore.abilities.objects;

import fr.hunh0w.dscore.abilities.SanguinaryAbility;
import fr.hunh0w.dscore.utils.ParticleUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

import java.util.*;

public class AkazaPunches extends SanguinaryAbility implements Listener {

    private static AkazaPunches instance;
    public static AkazaPunches get(){
        if(instance == null)
            instance = new AkazaPunches("Poings Soniques d'Akaza");
        return instance;
    }

    private List<String> players = new ArrayList<>();

    public AkazaPunches(String name) {
        super(name);
    }

    public void setHasAbility(String username, boolean mustHave){
        String user = username.toLowerCase(Locale.ROOT);
        if(mustHave){
            if(!players.contains(user))
                players.add(user);
        }else {
            while(players.contains(user))
                players.remove(user);
        }
    }

    public boolean hasAbility(String username){
        return players.contains(username.toLowerCase(Locale.ROOT));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent evt){
        Player p = evt.getPlayer();
        if(!hasAbility(p.getName()))
            return;
        if(p.getInventory().getItemInMainHand().getType() != Material.AIR)
            return;
        if(evt.getAction() != Action.LEFT_CLICK_AIR && evt.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        World w = p.getWorld();
        RayTraceResult r = w.rayTraceEntities(p.getEyeLocation(), p.getEyeLocation().getDirection(), 50);
        Location target = null;
        if(r == null){
            Block targetBlock = p.getTargetBlock(new HashSet<>(Arrays.asList(Material.WATER)), 50);
            if(targetBlock == null)
                return;
            target = targetBlock.getLocation();
        }else {
            target = r.getHitEntity().getLocation();
        }


        int distance = (int) Math.floor(p.getEyeLocation().distance(target) / 2)+1;

        Location infrontOf = p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.05));
        w.spawnParticle(Particle.FLASH, infrontOf, 1);
        w.playSound(p.getEyeLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 1.2f);
        ParticleUtils.spawnParticleAlongLine(p.getEyeLocation(), target, Particle.SONIC_BOOM, distance, 1, 0, 0, 0, 0, null, false, null);
        w.createExplosion(target, 3F, false, true, p);
    }
}
