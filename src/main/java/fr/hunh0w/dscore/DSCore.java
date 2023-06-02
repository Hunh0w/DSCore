package fr.hunh0w.dscore;

import fr.hunh0w.dscore.abilities.objects.AkazaPunches;
import fr.hunh0w.dscore.commands.DSTestCommand;
import fr.hunh0w.dscore.listeners.ChatManager;
import fr.hunh0w.dscore.listeners.DaemonListeners;
import fr.hunh0w.dscore.managers.DaemonManager;
import fr.hunh0w.dscore.managers.PlayerListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public final class DSCore extends JavaPlugin {

    private static DSCore instance;
    public static DSCore getInstance(){
        return instance;
    }

    private BukkitAudiences adventure;
    public @Nonnull BukkitAudiences adventure() {
        if(this.adventure == null)
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        return this.adventure;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.adventure = BukkitAudiences.create(this);
        getCommand("dstest").setExecutor(new DSTestCommand());
        Bukkit.getPluginManager().registerEvents(new ChatManager(), this);
        Bukkit.getPluginManager().registerEvents(new DaemonListeners(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        DaemonManager.getInstance().init();
    }

    @Override
    public void onDisable() {

    }


    public void setNoDamageTicks(LivingEntity entity, int ticks, boolean force){
        if(entity.getNoDamageTicks() != ticks || force){
            entity.setMaximumNoDamageTicks(ticks);
            entity.setNoDamageTicks(ticks);
        }
    }


}
