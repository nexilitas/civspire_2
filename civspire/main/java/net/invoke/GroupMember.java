package net.invoke;

import java.util.*;

public class GroupMember {
    private UUID uuid;
    private String rankId; /* refers to internal id in GroupRank */

    public GroupMember(UUID uuid, String rankId) {
        this.uuid = uuid;
        this.rankId = rankId;
    }

    public UUID getUuid() { return uuid; }
    public String getRankId() { return rankId; }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }
}
