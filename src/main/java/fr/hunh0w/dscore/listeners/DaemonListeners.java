package fr.hunh0w.dscore.listeners;

import fr.hunh0w.dscore.DSCore;
import fr.hunh0w.dscore.managers.LuckPermsManager;
import fr.hunh0w.dscore.utils.ParticleUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class DaemonListeners implements Listener {

    public HashMap<String, Date> lastSneaks = new HashMap<>();
    public HashMap<String, Integer[]> regenTasks = new HashMap<>();

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent evt){
        Player p = evt.getPlayer();
        if(!evt.isSneaking() || p.getFireTicks() > 0){
            Integer[] tasks = regenTasks.get(p.getName());
            if(tasks != null){
                Bukkit.getScheduler().cancelTask(tasks[0]);
            }
            return;
        }
        if(p.getFireTicks() > 0)
            return;
        if(!LuckPermsManager.getInstance().isDaemon(p.getName()))
            return;

        Date lastSneak = lastSneaks.get(p.getName());
        if(lastSneak != null){
            Instant lastSneakInstant = lastSneak.toInstant();
            Instant now = new Date().toInstant();
            now = now.minus(3, ChronoUnit.SECONDS);
            if(!now.isAfter(lastSneakInstant)) return;
            lastSneaks.remove(p.getName());
        }

        double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if(p.getHealth() >= maxHealth)
            return;
        if(lastSneaks.get(p.getName()) == null){
            Random rd = new Random();
            for(int i = 0; i < 5; i++)
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TURTLE_EGG_BREAK, 1, 0.7f);
            p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getEyeLocation(), 10, Material.REDSTONE_BLOCK.createBlockData());
            p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getLocation(), 10, Material.REDSTONE_BLOCK.createBlockData());

            int delay = rd.nextInt(10, 30);
            int taskRegen = Bukkit.getScheduler().scheduleSyncDelayedTask(DSCore.getInstance(), () -> {
                for(int i = 0; i < 5; i++)
                    p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_TURTLE_EGG_BREAK, 1, 0.7f);
                p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getEyeLocation(), 30, Material.REDSTONE_BLOCK.createBlockData());
                p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getLocation(), 30, Material.REDSTONE_BLOCK.createBlockData());
                double newHealth = p.getHealth()+6;
                if(newHealth > maxHealth) newHealth = maxHealth;
                p.setHealth(newHealth);
                lastSneaks.put(p.getName(), new Date());
            }, delay);

            regenTasks.put(p.getName(), new Integer[]{taskRegen});
        }
    }



    @EventHandler
    public void onTakeDamages(EntityDamageByEntityEvent evt){
        if(!(evt.getEntity() instanceof Player))
            return;
        Player p = (Player) evt.getEntity();
        if(!LuckPermsManager.getInstance().isDaemon(p.getName()))
            return;
        String group = LuckPermsManager.getInstance().getPrimaryGroup(p.getName());
        double damageReducement = 1;
        if(group.equalsIgnoreCase("demon")){
            damageReducement = 0.9;
        }else if(group.equalsIgnoreCase("demon-sup")) {
            damageReducement = 0.8;
        }else if(group.equalsIgnoreCase("lune-inf")) {
            damageReducement = 0.5;
        }else if(group.equalsIgnoreCase("lune-sup")) {
            damageReducement = 0.4;
        }else if(group.equalsIgnoreCase("kibutsuji")) {
            damageReducement = 0.2;
        }
        double finalDamage = evt.getDamage() * damageReducement;
        if(finalDamage < 0) finalDamage = 0;
        evt.setDamage(finalDamage);
    }

    @EventHandler
    void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(!p.isSwimming()) return;
        if(!LuckPermsManager.getInstance().isDaemon(p.getName()))
            return;
        String group = LuckPermsManager.getInstance().getPrimaryGroup(p.getName());

        if(group.equalsIgnoreCase("demon")){
            p.setVelocity(p.getLocation().getDirection().multiply(0.4));
        }else if(group.equalsIgnoreCase("demon-sup")) {
            p.setVelocity(p.getLocation().getDirection().multiply(0.6));
        }else if(group.equalsIgnoreCase("lune-inf")) {
            p.setVelocity(p.getLocation().getDirection().multiply(0.8));
        }else if(group.equalsIgnoreCase("lune-sup")) {
            p.setVelocity(p.getLocation().getDirection().multiply(1));
        }else if(group.equalsIgnoreCase("kibutsuji")) {
            p.setVelocity(p.getLocation().getDirection().multiply(1.2));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt){
        Entity attacker = evt.getDamager();
        if(!(evt.getEntity() instanceof LivingEntity))
            return;
        LivingEntity victim = (LivingEntity) evt.getEntity();

        if(!(attacker instanceof Player)) {
            DSCore.getInstance().setNoDamageTicks(victim, 20, false);
            return;
        }
        Player p = (Player)attacker;
        if(!LuckPermsManager.getInstance().isDaemon(p.getName())){
            DSCore.getInstance().setNoDamageTicks(victim, 20, false);
            return;
        }

        String group = LuckPermsManager.getInstance().getPrimaryGroup(p.getName());

        if(p.getInventory().getItemInMainHand().getType() == Material.AIR && (evt.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || evt.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)){
            double damage = evt.getDamage();
            if(group.equalsIgnoreCase("demon")){
                DSCore.getInstance().setNoDamageTicks(victim,16, false);
                evt.setDamage(damage*5);
            }else if(group.equalsIgnoreCase("demon-sup")) {
                DSCore.getInstance().setNoDamageTicks(victim,12, false);
                evt.setDamage(damage*7);
            }else if(group.equalsIgnoreCase("lune-inf")) {
                victim.setVelocity(victim.getEyeLocation().toVector().subtract(p.getLocation().toVector()).multiply(1.05).setY(1));
                DSCore.getInstance().setNoDamageTicks(victim,8, false);
                evt.setDamage(damage*9);
            }else if(group.equalsIgnoreCase("lune-sup")) {
                victim.setVelocity(victim.getEyeLocation().toVector().subtract(p.getLocation().toVector()).multiply(1.05).setY(1));
                DSCore.getInstance().setNoDamageTicks(victim,6, false);
                evt.setDamage(damage*11);
            }else if(group.equalsIgnoreCase("kibutsuji")) {
                victim.setVelocity(victim.getEyeLocation().toVector().subtract(p.getLocation().toVector()).multiply(1.05).setY(1));
                DSCore.getInstance().setNoDamageTicks(victim,4, false);
                evt.setDamage(damage*13);
            }
        }


    }


    /**
     * Double jumps
     */
    @EventHandler
    public void setVelocity(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if(!LuckPermsManager.getInstance().isDaemon(p.getName()))
            return;
        if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR || p.isFlying())
            return;

        String group = LuckPermsManager.getInstance().getPrimaryGroup(p.getName());
        if(!group.equalsIgnoreCase("kibutsuji") && !group.equalsIgnoreCase("lune-sup")
            && !group.equalsIgnoreCase("lune-inf"))
            return;

        e.setCancelled(true);
        p.setAllowFlight(false);
        p.setFlying(false);

        Location start = e.getPlayer().getEyeLocation();
        Vector vector = e.getPlayer().getLocation().getDirection().multiply(2);
        Location end = start.toVector().add(vector).toLocation(start.getWorld());
        
        p.setVelocity(vector);
        start.getWorld().playSound(start, Sound.ENTITY_WITHER_SHOOT, 1, 0.8f);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 3.0F);
        ParticleUtils.spawnParticleAlongLine(start, end, Particle.REDSTONE, 10, 30, 0, 0, 0, 0, dustOptions, false, null);

        giveFlight(p, 30);
    }

    private void giveFlight(Player p, int delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.setAllowFlight(true);
            }
        }.runTaskLater(DSCore.getInstance(), delay);
    }

}
