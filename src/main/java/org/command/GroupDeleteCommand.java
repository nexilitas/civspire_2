package org.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.civspire;  // Adjust to your main plugin class package and name
import org.invoke.Group;

import java.util.Optional;
import java.util.Set;
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

        if (args.length < 1) {
            player.sendMessage("§cUsage: /groupdelete <groupName>");
            return true;
        }

        String targetGroupName = args[0];
        UUID playerUUID = player.getUniqueId();

        Set<Group> groups = plugin.getGroupManager().getGroupsByPlayer(playerUUID);
        if (groups.isEmpty()) {
            player.sendMessage("§cYou are not a member of any group.");
            return true;
        }

        Optional<Group> matchingGroup = groups.stream()
                .filter(g -> g.getName().equalsIgnoreCase(targetGroupName) || g.getId().equalsIgnoreCase(targetGroupName))
                .findFirst();

        if (matchingGroup.isEmpty()) {
            player.sendMessage("§cYou are not a member of a group named '" + targetGroupName + "'.");
            return true;
        }

        Group group = matchingGroup.get();

        if (!group.getOwner().equals(playerUUID)) {
            player.sendMessage("§cOnly the group owner can delete the group.");
            return true;
        }

        plugin.getGroupManager().deleteGroup(group.getId());
        plugin.getGroupManager().saveAll();

        player.sendMessage("§aGroup deleted: " + group.getName());
        return true;
    }
}
