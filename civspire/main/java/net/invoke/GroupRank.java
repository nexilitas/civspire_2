package net.invoke;

import java.util.Set;

public class GroupRank {
    private String id;                   // owner, 1, 2, 3, etc.
    private String name;                 // Display name
    private Set<String> permissions;     // kick, invite, build, place, etc.

    public GroupRank(String id, String name, Set<String> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Set<String> getPermissions() { return permissions; }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
