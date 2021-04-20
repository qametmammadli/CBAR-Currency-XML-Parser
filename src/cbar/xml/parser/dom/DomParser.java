package cbar.xml.parser.dom;

import cbar.xml.domain.Currency;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DomParser {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            String uri = "https://www.cbar.az/currencies/" + currentDate() + ".xml";
            Document document = builder.parse(uri);
            Element valType = (Element) document.getElementsByTagName("ValType").item(1);
            NodeList nodeList = valType.getElementsByTagName("Valute");

            List<Currency> currencies = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Currency currency = new Currency();

                Element element = (Element) nodeList.item(i);
                currency.setCode(element.getAttribute("Code"));

                String nominal = element.getElementsByTagName("Nominal").item(0).getTextContent();
                currency.setNominal(Integer.parseInt(nominal));

                String name = element.getElementsByTagName("Name").item(0).getTextContent();
                currency.setName(name);

                String value = element.getElementsByTagName("Value").item(0).getTextContent();
                currency.setValue(new BigDecimal(value));

                currencies.add(currency);
            }

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
