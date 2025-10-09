package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDTO {

    private int id;
    @NotBlank(message = "First name is required")
    @Size(min = 3, message = "Minimum 3 characters!")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, message = "Minimum 3 characters!")
    private String lastName;

    @Min(14)
    private int age;

    @Size(min = 3, message = "Minimum 3 characters!")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "enter email!")
    private String email;

    private List<RoleDTO> roles;

    public UserDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
}
