package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.Role;

public class RoleDTO {

    private int id;

    private String authority;

    public RoleDTO() {}

    public RoleDTO(String authority) {
        this.authority = authority;
    }

    public RoleDTO(Role role) {
        authority = role.getAuthority().replace("ROLE_", "");
        this.id = role.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
