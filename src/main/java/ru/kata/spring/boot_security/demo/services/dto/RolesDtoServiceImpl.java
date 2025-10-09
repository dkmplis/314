package ru.kata.spring.boot_security.demo.services.dto;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolesDtoServiceImpl implements RoleDtoService {

    @Override
    public RoleDTO convertToDto(Role role) {
        return new RoleDTO(role);
    }

    @Override
    public List<RoleDTO> convertToListDto(List<Role> roles) {
        return roles.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }


}
