package ru.kata.spring.boot_security.demo.services.dto;

import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleDtoService {
    RoleDTO convertToDto(Role role);

    List<RoleDTO> convertToListDto(List<Role> roles);

}
