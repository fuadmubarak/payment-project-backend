package com.payment.portal.controller;

import com.payment.portal.entity.Role;
import com.payment.portal.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
 
    @GetMapping
    public List<Role> getRoles() {
        return roleService.getAllRoles();
    }
}
