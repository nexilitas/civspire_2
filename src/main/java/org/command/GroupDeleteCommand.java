package org.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.civspire;
import org.invoke.Group;

import java.util.Optional;
import java.util.UUID;

public class GroupDeleteCommand implements CommandExecutor {
    private final civspire plugin;

    public GroupDeleteCommand(civspire plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        Optional<Group> groupOpt = plugin.getGroupManager().getGroupByPlayer(uuid);

        if (groupOpt.isEmpty()) {
            player.sendMessage("§cYou are not in a group.");
            return true;
        }

        Group group = groupOpt.get();
        if (!group.getOwner().equals(uuid)) {
            player.sendMessage("§cOnly the group owner can delete the group.");
            return true;
        }

        plugin.getGroupManager().deleteGroup(group.getId());
        player.sendMessage("§aGroup deleted: " + group.getName());
        return true;
    }
}
