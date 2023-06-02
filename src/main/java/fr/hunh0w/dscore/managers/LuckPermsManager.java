package fr.hunh0w.dscore.managers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LuckPermsManager {

    private static LuckPermsManager instance;

    @Nonnull
    public static LuckPermsManager getInstance(){
        if(instance == null)
            instance = new LuckPermsManager();
        return instance;
    }

    private final RegisteredServiceProvider<LuckPerms> provider;

    @Nullable
    public RegisteredServiceProvider<LuckPerms> getProvider() {
        return provider;
    }

    public LuckPermsManager(){
        provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    }

    public String getPrefix(String username){
        LuckPerms api = getApi();
        User user = api.getUserManager().getUser(username);
        CachedMetaData metaData = user.getCachedData().getMetaData();
        return metaData.getPrefix();
    }

    public Boolean isDaemon(String username){
        LuckPerms api = getApi();
        User user = api.getUserManager().getUser(username);
        String primaryGroup = user.getPrimaryGroup();
        if(primaryGroup.equalsIgnoreCase("demon"))
            return true;
        if(primaryGroup.equalsIgnoreCase("demon-sup"))
            return true;
        if(primaryGroup.equalsIgnoreCase("lune-inf"))
            return true;
        if(primaryGroup.equalsIgnoreCase("lune-sup"))
            return true;
        if(primaryGroup.equalsIgnoreCase("kibutsuji"))
            return true;
        return false;
    }

    public String getPrimaryGroup(String username){
        LuckPerms api = getApi();
        User user = api.getUserManager().getUser(username);
        return user.getPrimaryGroup();
    }

    @Nullable
    public LuckPerms getApi(){
        if (provider != null) {
            return provider.getProvider();
        }
        return null;
    }

}
