package ee.golive.finants.menu;


import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public class MenuPreparer implements ViewPreparer {

    @Autowired
    MenuService menuService;

    public void execute(Request tilesContext, AttributeContext attributeContext){
        Attribute attr = new Attribute();
        String menu = attributeContext.getAttribute("menu").toString();
        attr.setValue(menuService.getMenu(menu));
        attributeContext.putAttribute("menuItems", attr);
    }
}
