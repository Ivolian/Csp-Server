package com.withub.csp.entity.base;


import com.withub.csp.entity.Menu;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class MenuEntity extends BaseEntity {

    protected Menu menu;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

}
