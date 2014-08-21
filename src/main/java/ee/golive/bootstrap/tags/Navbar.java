package ee.golive.bootstrap.tags;

import ee.golive.boostrap.helper.TagBuilder;

import javax.servlet.jsp.JspWriter;

public class Navbar extends Tag {

    TagBuilder nav;
    TagBuilder container;
    TagBuilder collapse;

    @Override
    public void startTag() throws  Exception {
        nav = createTagBuilder("nav");
        container = createTagBuilder("div");
        collapse = createTagBuilder("div");

        nav.addStyle("navbar");
        nav.addStyle("navbar-default");
        nav.addStyle("navbar-fixed-top");
        nav.addAttribute("role", "navigation");

        container.addStyle("container");
        collapse.addStyles("collapse navbar-collapse");

        JspWriter out = pageContext.getOut();
        out.write(nav.compileStartTag());
        out.write(container.compileStartTag());
        out.write(collapse.compileStartTag());
    }

    @Override
    public void endTag() throws Exception {
        JspWriter out = pageContext.getOut();
        out.write(collapse.compileEndTag());
        out.write(container.compileEndTag());
        out.write(nav.compileEndTag());
    }
}
