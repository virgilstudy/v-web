package com.virgil.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Virgil on 2017/1/31.
 */
@Entity
@Table(name = "sys_menu")
public class SysMenuEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;
    /**
     * 父菜单ID，一级菜单为0
     */
    @Column(name = "parent_id")
    private Long parentId;
    /**
     * 父菜单名称
     */
    @Column(name = "parent_name")
    private String parentName;
    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String name;

    @Column(name = "menu_url")
    private String url;
    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @Column(name = "perms")
    private String perms;
    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    @Column(name = "menu_type")
    private Integer type;
    /**
     * 菜单图标
     */
    @Column(name = "icon")
    private String icon;
    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;
    /**
     * ztree属性
     */
    @Column(name = "ztree_open")
    private Boolean open;

    @Transient
    private List<?> list;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
