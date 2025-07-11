package org;

import org.bukkit.plugin.java.JavaPlugin;
import org.command.*;
import org.manager.*;

public final class civspire extends JavaPlugin {
    private GroupManager groupManager;
    private GroupInviteManager inviteManager;
    private static civspire instance;

    public void onEnable() {
        instance = this;
        groupManager = new GroupManager();
        inviteManager = new GroupInviteManager();
        getLogger().info("GroupCore enabled.");

        // Register commands
        getCommand("groupcreate").setExecutor(new GroupCreateCommand(this));
        getCommand("groupdelete").setExecutor(new GroupDeleteCommand(this));
        getCommand("groupinvite").setExecutor(new GroupInviteCommand(this));
        getCommand("grouprename").setExecutor(new GroupRenameCommand(this));
        getCommand("groupaccept").setExecutor(new GroupAcceptCommand(this));
        getCommand("groupleave").setExecutor(new EmptyCommand());
        getCommand("groupjoin").setExecutor(new EmptyCommand());

        // Load your config and data
        saveDefaultConfig();
        getLogger().info("Config loaded (default if missing).");
        // REGULAR DATA SAVING
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            getGroupManager().saveAll();
            getLogger().info("Group data autosaved.");
        }, 1200L, 12000L); // 1200 ticks = 1 minute
        // 5 min interval

    }
    public GroupManager getGroupManager() {
        return groupManager;
    }
    public GroupInviteManager getInviteManager() {
        return inviteManager;
    }


    @Override
    public void onDisable() {
        getLogger().info("GroupCore disabling...");

        // TODO: Save group data
        getLogger().info("Data saved.");
        getLogger().info("Saving groups...");
        groupManager.saveAll();
        instance = null;
    }

    public static civspire getInstance() {
        return instance;
    }
}