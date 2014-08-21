package ee.golive.boostrap.helper;

import java.util.*;

public class TagBuilder {

    private Map<String, String> attributes;
    private List<String> styles;
    private String tagName;

    public TagBuilder(String name) {
        this.tagName = name;
        attributes = Collections.synchronizedMap(new HashMap<String, String>());
        styles = new ArrayList<String>();
    }

    public void addStyle(String style) {
        styles.add(style);
    }

    public void addStyles(String styles) {
        String[] splitStyles = styles.split("\\s");
        this.styles.addAll(Arrays.asList(splitStyles));
    }

    public void addAttribute(String attribute, String value) {
        attributes.put(attribute, value);
    }


    private String getCompiledStyles() {
        StringBuilder builder = new StringBuilder();
        for (String style : styles) {
            builder.append(style + " ");
        }
        if (!styles.isEmpty())
            builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

    public String compileStartTag() {
        StringBuilder tag = new StringBuilder();
        tag.append("<");
        tag.append(tagName);

        if (!styles.isEmpty()) {
            addAttribute("class", getCompiledStyles());
        }

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            tag.append(" ");
            tag.append(key);
            tag.append("=\"");
            tag.append(value);
            tag.append("\"");
        }

        tag.append(">");

        return tag.toString();
    }

    public String compileEndTag() {
        return "</" + tagName + ">";
    }
}
