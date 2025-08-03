package net.invoke;

import java.util.*;

public class Group {
    private String id;
    private String name;
    private UUID owner;
    private List<GroupMember> members;
    private List<GroupRank> ranks;

    public Group(String id, String name, UUID owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.members = new ArrayList<>();
        this.ranks = new ArrayList<>();

        // Add owner as member
        members.add(new GroupMember(owner, "owner"));

        // Add default ranks
        ranks.add(new GroupRank("owner", "Owner", Set.of("invite", "kick", "delete", "promote")));
        ranks.add(new GroupRank("member", "Member", Set.of()));
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public UUID getOwner() { return owner; }
    public List<GroupMember> getMembers() { return members; }
    public List<GroupRank> getRanks() { return ranks; }

    public Optional<GroupMember> getMember(UUID uuid) {
        return members.stream().filter(m -> m.getUuid().equals(uuid)).findFirst();
    }

    public Optional<GroupRank> getRank(String id) {
        return ranks.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public boolean memberHasPermission(UUID player, String permission) {
        Optional<GroupMember> memberOpt = getMember(player);
        if (memberOpt.isEmpty()) return false;
        String rankId = memberOpt.get().getRankId();
        return getRank(rankId).map(r -> r.hasPermission(permission)).orElse(false);
    }
    public void addMember(UUID uuid, String rankId) {
        members.add(new GroupMember(uuid, rankId));
    }
    public void removeMember(UUID uuid) {
        members.removeIf(m -> m.getUuid().equals(uuid));
    }
    public void setName(String name) {
        this.name = name;
    }

}