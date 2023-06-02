package fr.hunh0w.dscore.managers;

import fr.hunh0w.dscore.DSCore;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenersManager {

    private static ListenersManager instance;
    public static ListenersManager getInstance(){
        if(instance == null)
            instance = new ListenersManager();
        return instance;
    }

    public void addListener(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, DSCore.getInstance());
    }

}
