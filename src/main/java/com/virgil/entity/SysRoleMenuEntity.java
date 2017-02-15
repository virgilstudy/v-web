package com.virgil.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Virgil on 2017/1/31.
 */
@Entity
@Table(name = "sys_role_menu")
public class SysRoleMenuEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "menu_id")
    private Long menuId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
