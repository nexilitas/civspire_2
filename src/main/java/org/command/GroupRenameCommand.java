package org.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.civspire;  // Use your actual main plugin class name & package
import org.invoke.Group;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class GroupRenameCommand implements CommandExecutor {
    private final civspire plugin;

    public GroupRenameCommand(civspire plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        // We require 2 arguments: group name/ID, and new name
        if (args.length < 2) {
            player.sendMessage("§cUsage: /grouprename <groupName> <newName>");
            return true;
        }

        String targetGroupName = args[0];
        String newName = args[1];
        UUID playerUUID = player.getUniqueId();

        // Get all groups player belongs to
        Set<Group> groups = plugin.getGroupManager().getGroupsByPlayer(playerUUID);

        if (groups.isEmpty()) {
            player.sendMessage("§cYou are not a member of any group.");
            return true;
        }

        // Find group matching input name/ID among player's groups
        Optional<Group> matchingGroup = groups.stream()
                .filter(g -> g.getName().equalsIgnoreCase(targetGroupName) || g.getId().equalsIgnoreCase(targetGroupName))
                .findFirst();

        if (matchingGroup.isEmpty()) {
            player.sendMessage("§cYou are not a member of a group named '" + targetGroupName + "'.");
            return true;
        }

        Group group = matchingGroup.get();

        // Check ownership
        if (!group.getOwner().equals(playerUUID)) {
            player.sendMessage("§cOnly the group owner can rename the group.");
            return true;
        }

        group.setName(newName);
        plugin.getGroupManager().saveAll();
        player.sendMessage("§aGroup renamed to: " + newName);
        return true;
    }
}
