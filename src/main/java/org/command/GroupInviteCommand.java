package org.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.civspire;
import org.invoke.Group;

import java.util.Optional;
import java.util.UUID;

public class GroupInviteCommand implements CommandExecutor {
    private final civspire plugin;

    public GroupInviteCommand(civspire plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUsage: /groupinvite <player>");
            return true;
        }

        UUID senderId = player.getUniqueId();
        Optional<Group> groupOpt = plugin.getGroupManager().getGroupByPlayer(senderId);

        if (groupOpt.isEmpty()) {
            player.sendMessage("§cYou are not in a group.");
            return true;
        }

        Group group = groupOpt.get();

        // Optional: check permissions via ranks here
        if (!group.memberHasPermission(senderId, "invite")) {
            player.sendMessage("§cYou don't have permission to invite.");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null || !target.hasPlayedBefore()) {
            player.sendMessage("§cThat player doesn't exist or hasn't joined the server.");
            return true;
        }

        // 🔶 Placeholder for a real invitation system
        // For now, just notify
        player.sendMessage("§aYou invited " + target.getName() + " to join your group.");
        if (target.isOnline()) {
            target.getPlayer().sendMessage("§eYou were invited to join the group: §b" + group.getName());
            target.getPlayer().sendMessage("§7Use /groupaccept " + group.getName() + " to accept.");
        }

        return true;
    }
}
