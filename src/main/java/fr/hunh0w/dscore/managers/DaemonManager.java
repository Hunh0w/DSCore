package fr.hunh0w.dscore.managers;

import fr.hunh0w.dscore.DSCore;
import fr.hunh0w.dscore.abilities.objects.AkazaPunches;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.w3c.dom.Attr;

public class DaemonManager {

    private static DaemonManager instance;
    public static DaemonManager getInstance(){
        if(instance == null)
            instance = new DaemonManager();
        return instance;
    }

    public void initDaemon(Player p){
        if(!LuckPermsManager.getInstance().isDaemon(p.getName()))
            return;
        String group = LuckPermsManager.getInstance().getPrimaryGroup(p.getName());
        AttributeInstance maxHeath = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance speed = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeInstance knockbackResistance = p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if(group.equalsIgnoreCase("demon")){
            speed.setBaseValue(0.15);
            maxHeath.setBaseValue(26);
            knockbackResistance.setBaseValue(0);
            p.setAllowFlight(false);
            AkazaPunches.get().setHasAbility(p.getName(), false);
        }else if(group.equalsIgnoreCase("demon-sup")) {
            speed.setBaseValue(0.17);
            maxHeath.setBaseValue(30);
            knockbackResistance.setBaseValue(0);
            p.setAllowFlight(false);
            AkazaPunches.get().setHasAbility(p.getName(), false);
        }else if(group.equalsIgnoreCase("lune-inf")) {
            speed.setBaseValue(0.19);
            maxHeath.setBaseValue(36);
            knockbackResistance.setBaseValue(0.1);
            p.setAllowFlight(true);
            AkazaPunches.get().setHasAbility(p.getName(), false);
        }else if(group.equalsIgnoreCase("lune-sup")) {
            speed.setBaseValue(0.21);
            maxHeath.setBaseValue(44);
            knockbackResistance.setBaseValue(0.2);
            p.setAllowFlight(true);
            AkazaPunches.get().setHasAbility(p.getName(), true);
        }else if(group.equalsIgnoreCase("kibutsuji")) {
            speed.setBaseValue(0.25);
            maxHeath.setBaseValue(52);
            knockbackResistance.setBaseValue(0.5);
            p.setAllowFlight(true);
            AkazaPunches.get().setHasAbility(p.getName(), true);
        }
        p.setFlying(false);
    }

    public void init(){
        Bukkit.getScheduler().runTaskTimer(DSCore.getInstance(), () -> {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.getGameMode() != GameMode.ADVENTURE && p.getGameMode() != GameMode.SURVIVAL)
                    continue;
                World w = p.getWorld();
                if(w.hasStorm()) continue;
                if(!LuckPermsManager.getInstance().isDaemon(p.getName())) continue;

                if(isDay(w)) {
                    if(p.getActivePotionEffects().stream().filter(potionEffect ->
                            potionEffect.getType().equals(PotionEffectType.FIRE_RESISTANCE)).count() != 0)
                        continue;
                    if(p.getEyeLocation().getBlock().getLightFromSky() == 15 && p.getFireTicks() <= 5)
                        p.setFireTicks(20);
                }else {
                    // Maybe ?
                }
            }
        }, 0L, 4L);
    }

    public boolean isDay(World world){
        return (world.getTime() >= 0 && world.getTime() <= 12800) || world.getTime() >= 23500;
    }

}
