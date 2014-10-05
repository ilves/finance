package ee.golive.finants.menu;

public class MenuItem {
    private String label;
    private String url;
    private boolean active;
    private String key;

    public MenuItem(String label, String url) {
        this.setLabel(label);
        this.setUrl(url);
    }

    public MenuItem(String label, String url, String key) {
        this.setLabel(label);
        this.setUrl(url);
        this.setKey(key);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
