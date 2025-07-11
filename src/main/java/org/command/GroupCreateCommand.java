package org.command;

import org.civspire;
import org.invoke.Group;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupCreateCommand implements CommandExecutor {

    private final civspire plugin;

    public GroupCreateCommand(civspire plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUsage: /groupcreate <name>");
            return true;
        }

        UUID uuid = player.getUniqueId();

        // Check if player is already in a group
        if (!plugin.getGroupManager().getGroupsByPlayer(uuid).isEmpty()) {
            player.sendMessage("§cYou are already in a group.");
            return true;
        }


        String name = args[0];

        Group group = plugin.getGroupManager().createGroup(name, uuid);
        player.sendMessage("§aGroup created: " + group.getName());
        return true;
    }
}
