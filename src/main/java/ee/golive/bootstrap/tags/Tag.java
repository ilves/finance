package ee.golive.bootstrap.tags;


import ee.golive.boostrap.helper.TagBuilder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public abstract class Tag extends TagSupport {

    protected String styles = "";
    protected String id = "";

    public abstract void startTag() throws Exception;
    public abstract void endTag() throws Exception;

    public final TagBuilder createTagBuilder(String tagName) {
        TagBuilder tag = new TagBuilder(tagName);
        if (!styles.isEmpty()) {
            tag.addStyles(styles);
        }
        if (!id.isEmpty()) {
            tag.addAttribute("id", id);
        }
        return tag;
    }


    @Override
    public int doStartTag() throws JspException {
        try {
            startTag();
        } catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            endTag();
        } catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

}
