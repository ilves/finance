package ee.golive.finants.menu;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Scope("request")
public class MenuService {
    private LinkedHashMap<String, Menu> menus = new LinkedHashMap<String, Menu>();

    public void addMenu(Menu menu, String key) {
        menus.put(key, menu);
    }

    public Menu getMenu(String key) {
        return (Menu)menus.get(key);
    }
}
