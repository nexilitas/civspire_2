package net.manager;

import net.invoke.Group;
import net.invoke.GroupMember;
import net.storagelogic.GroupStorage;

import java.util.*;

public class GroupManager {

    // Map group ID to Group object
    private final Map<String, Group> groups = new HashMap<>();

    // Map player UUID to set of group IDs they belong to
    private final Map<UUID, Set<String>> playerGroupsMap = new HashMap<>();

    public GroupManager() {
        // Load groups from storage
        Collection<Group> loadedGroups = GroupStorage.loadGroups();

        for (Group group : loadedGroups) {
            groups.put(group.getId(), group);
            for (GroupMember member : group.getMembers()) {
                UUID memberUUID = member.getUuid();
                playerGroupsMap.computeIfAbsent(memberUUID, k -> new HashSet<>()).add(group.getId());
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
        playerGroupsMap.computeIfAbsent(owner, k -> new HashSet<>()).add(id);
        return group;
    }

    public boolean deleteGroup(String id) {
        Group removed = groups.remove(id);
        if (removed != null) {
            for (GroupMember member : removed.getMembers()) {
                UUID memberUUID = member.getUuid();
                Set<String> groupSet = playerGroupsMap.get(memberUUID);
                if (groupSet != null) {
                    groupSet.remove(id);
                    if (groupSet.isEmpty()) {
                        playerGroupsMap.remove(memberUUID);
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

    // Returns all groups player is part of
    public Set<Group> getGroupsByPlayer(UUID player) {
        Set<String> groupIds = playerGroupsMap.getOrDefault(player, Collections.emptySet());
        Set<Group> playerGroups = new HashSet<>();
        for (String groupId : groupIds) {
            Group group = groups.get(groupId);
            if (group != null) {
                playerGroups.add(group);
            }
        }
        return playerGroups;
    }

    // Joins a player to a group; adds as "member" rank by default
    public void joinGroup(UUID player, String groupId) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.addMember(player, "member");
            playerGroupsMap.computeIfAbsent(player, k -> new HashSet<>()).add(groupId);
        }
    }

    // Player leaves a specific group
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

    // Optional: remove player from all groups they belong to
    public void leaveAllGroups(UUID player) {
        Set<String> groupSet = playerGroupsMap.remove(player);
        if (groupSet != null) {
            for (String groupId : groupSet) {
                Group group = groups.get(groupId);
                if (group != null) {
                    group.removeMember(player);
                }
            }
        }
    }

    // Get all groups in system
    public Collection<Group> getAllGroups() {
        return Collections.unmodifiableCollection(groups.values());
    }
}
