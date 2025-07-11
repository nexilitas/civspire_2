package org.manager;

import org.invoke.Group;
import org.invoke.GroupMember;
import org.storagelogic.GroupStorage;

import java.util.*;

public class GroupManager {
    private final Map<String, Group> groups = new HashMap<>();
    private final Map<UUID, Set<String>> playerGroupsMap = new HashMap<>();


    public GroupManager() {
        // Load from disk
        for (Group group : GroupStorage.loadGroups()) {
            groups.put(group.getId(), group);
            for (GroupMember member : group.getMembers()) {
                playerGroupMap.put(member.getUuid(), group.getId());
            }
        }
    }

    public void saveAll() {
        GroupStorage.saveGroups(groups.values());
    }

    public Group createGroup(String name, UUID owner) {
        String id = UUID.randomUUID().toString();
        Group group = new Group(id, name, owner);
        groups.put(id, group);
        playerGroupMap.put(owner, id);
        return group;
    }

    // delete a group
    public boolean deleteGroup(String id) {
        Group removed = groups.remove(id);
        if (removed != null) {
            for (GroupMember member : removed.getMembers()) {
                UUID uuid = member.getUuid();
                Set<String> groupSet = playerGroupsMap.get(uuid);
                if (groupSet != null) {
                    groupSet.remove(id);
                    if (groupSet.isEmpty()) {
                        playerGroupsMap.remove(uuid);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public Optional<Group> getGroupById(String id) {
        return Optional.ofNullable(groups.get(id));
    }

    public Set<Group> getGroupsByPlayer(UUID player) {
        Set<String> ids = playerGroupsMap.getOrDefault(player, Collections.emptySet());
        Set<Group> playerGroups = new HashSet<>();
        for (String id : ids) {
            Group group = groups.get(id);
            if (group != null) {
                playerGroups.add(group);
            }
        }
        return playerGroups;
    }


    public void joinGroup(UUID player, String groupId) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.addMember(player, "member");
            playerGroupsMap.computeIfAbsent(player, k -> new HashSet<>()).add(groupId);
        }
    }


    public void leaveGroup(UUID player, String groupId) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.removeMember(player);
            Set<String> groupSet = playerGroupsMap.get(player);
            if (groupSet != null) {
                groupSet.remove(groupId);
                if (groupSet.isEmpty()) {
                    playerGroupsMap.remove(player);
                }
            }
        }
    }


    public Collection<Group> getAllGroups() {
        return groups.values();
    }
}
