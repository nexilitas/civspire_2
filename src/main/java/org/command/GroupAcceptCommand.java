package org.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.civspire;
import org.invoke.Group;

import java.util.Optional;
import java.util.UUID;

public class GroupAcceptCommand implements CommandExecutor {
    private final civspire plugin;

    public GroupAcceptCommand(civspire plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can accept invites.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUsage: /groupaccept <group-name-or-id>");
            return true;
        }

        UUID playerId = player.getUniqueId();

        // Find group by name or ID
        String input = args[0];
        Optional<Group> groupOpt = plugin.getGroupManager().getAllGroups().stream()
                .filter(g -> g.getName().equalsIgnoreCase(input) || g.getId().equalsIgnoreCase(input))
                .findFirst();

        if (groupOpt.isEmpty()) {
            player.sendMessage("§cGroup not found.");
            return true;
        }

        Group group = groupOpt.get();

        if (!plugin.getInviteManager().hasInvite(playerId, group.getId())) {
            player.sendMessage("§cYou were not invited to this group.");
            return true;
        }

        // Add member
        group.addMember(playerId, "member");
        plugin.getGroupManager().saveAll();
        plugin.getInviteManager().removeInvite(playerId, group.getId());

        player.sendMessage("§aYou joined the group: " + group.getName());
        return true;
    }
}
