package ru.oganesyan.artem;

import org.bukkit.plugin.java.JavaPlugin;
import ru.oganesyan.artem.listener.TreeListener;

public final class Artem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new TreeListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
