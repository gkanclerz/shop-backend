package pl.nullpointerexception.shop.security.model;

public enum UserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_CUSTOMER("CUSTOMER");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
