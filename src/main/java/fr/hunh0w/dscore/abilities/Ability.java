package fr.hunh0w.dscore.abilities;

import fr.hunh0w.dscore.managers.ListenersManager;
import org.bukkit.event.Listener;

public abstract class Ability {

    protected String name;

    public Ability(String name){
        this.name = name;
        if(this instanceof Listener){
            ListenersManager.getInstance().addListener((Listener) this);
        }
    }


}
