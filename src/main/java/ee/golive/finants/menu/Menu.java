package ee.golive.finants.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private List<MenuItem> items;

    public Menu() {
        items = new ArrayList<MenuItem>();
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void setActive(String key) {
        for(MenuItem item : items) {
            item.setActive(item.getKey().equals(key));
        }
    }
}
