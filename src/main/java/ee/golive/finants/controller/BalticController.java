package ee.golive.finants.controller;

import ee.golive.finants.model.Account;
import ee.golive.finants.model.BalticStock;
import ee.golive.finants.services.AccountService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class BalticController {

    @Autowired
    AccountService accountService;

    @RequestMapping("/baltic")
    public String test(Model model) throws IOException {
        String content = getContentOfUrl("http://www.nasdaqomxbaltic.com/market/?pg=mainlist&lang=et");
        Pattern p = Pattern.compile("(?s)\\<tr.id\\=\\\"eq\\-.*?\\\"\\>(.*?)<\\/tr");
        Pattern p2 = Pattern.compile("(?s)\\<td.*?\\>([a-zA-Z0-9,]+).*?<\\/td");
        Matcher m = p.matcher(content);


        HashMap<String, Integer> number = new HashMap<>();
        number.put("OEG1T", 151791206);

        HashMap<String, Integer> profit = new HashMap<>();
        profit.put("OEG1T", 27);

        HashMap<String, BalticStock> stockList = new HashMap<>();

        while(m.find()){
            BalticStock tmp = new BalticStock();
            Matcher m2 = p2.matcher(m.group(1));

            int n = 0;
            while(m2.find()){
                switch (n) {
                    case 1:
                        int last = m2.group(1).indexOf('<');
                        if (last <= 0) last = m2.group(1).length();
                        tmp.code = m2.group(1).substring(0, last);
                        break;
                    case 4:
                        tmp.price = Float.parseFloat(m2.group(1).replace(",","."));
                        break;
                }
                n++;
            }

            tmp.totalStock = number.get(tmp.code);
            tmp.profit = profit.get(tmp.code);

            stockList.put(tmp.code, tmp);
        }



        model.addAttribute("list", stockList);


        return "baltic";
    }

    public String getContentOfUrl(String rawUrl) throws IOException {
        URL url = new URL(rawUrl);
        URLConnection con = url.openConnection();
        StringBuilder buf = new StringBuilder();
        Reader r = new InputStreamReader(con.getInputStream());
        while (true) {
            int ch = r.read();
            if (ch < 0)
                break;
            buf.append((char) ch);
        }

        return  buf.toString();
    }
}
