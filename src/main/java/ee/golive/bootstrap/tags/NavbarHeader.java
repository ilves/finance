package ee.golive.bootstrap.tags;


import ee.golive.boostrap.helper.TagBuilder;

import javax.servlet.jsp.JspWriter;

public class NavbarHeader extends  Tag {

    TagBuilder container;

    private String brand = null;
    private String link = "#";

    @Override
    public void startTag() throws Exception {
        container = createTagBuilder("div");
        container.addStyle("navbar-header");

        JspWriter out = pageContext.getOut();
        out.write(container.compileStartTag());
        if (!brand.isEmpty()) {
            TagBuilder brandLink = createTagBuilder("a");
            brandLink.addAttribute("href", link);
            brandLink.addStyle("navbar-brand");
            out.write(brandLink.compileStartTag());
            out.write(brand);
            out.write(brandLink.compileEndTag());
        }
    }

    @Override
    public void endTag() throws Exception {
        JspWriter out = pageContext.getOut();
        out.write(container.compileEndTag());
    }

    public void setBrand(String brand) {
       this.brand = brand;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
