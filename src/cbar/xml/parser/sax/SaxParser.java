package cbar.xml.parser.sax;

import cbar.xml.domain.Currency;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SaxParser {
    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            String uri = "https://www.cbar.az/currencies/" + currentDate() + ".xml";
            CurrencySaxContentHandler handler = new CurrencySaxContentHandler();
            parser.parse(uri, handler);

            List<Currency> currencies = handler.getCurrencies();
            System.out.println(" ----- DATE : " + currentDate() + " ----- ");
            currencies.forEach(currency -> System.out.printf("%s : %s -> %s AZN \n",
                    currency.getCode(),
                    currency.getName(),
                    currency.getValue()
                    )
            );

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    private static String currentDate(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return now.format(formatter);
    }
}
