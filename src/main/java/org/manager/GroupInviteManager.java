package org.manager;

import java.util.*;

public class GroupInviteManager {
    private final Map<UUID, Set<String>> invites = new HashMap<>();

    public void invitePlayer(UUID player, String groupId) {
        invites.computeIfAbsent(player, k -> new HashSet<>()).add(groupId);
    }

    public boolean hasInvite(UUID player, String groupId) {
        return invites.getOrDefault(player, Collections.emptySet()).contains(groupId);
    }

    public void removeInvite(UUID player, String groupId) {
        Set<String> playerInvites = invites.get(player);
        if (playerInvites != null) {
            playerInvites.remove(groupId);
            if (playerInvites.isEmpty()) {
                invites.remove(player);
            }
        }
    }

    public Set<String> getInvites(UUID player) {
        return invites.getOrDefault(player, Collections.emptySet());
    }

    public void clearAllInvites(UUID player) {
        invites.remove(player);
    }
}
