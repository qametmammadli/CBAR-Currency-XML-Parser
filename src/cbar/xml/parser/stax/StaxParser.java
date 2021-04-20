package cbar.xml.parser.stax;

import cbar.xml.domain.Currency;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaxParser {
    public static void main(String[] args) {
        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            URL url = new URL("https://www.cbar.az/currencies/" + currentDate() + ".xml");
            XMLStreamReader reader = factory.createXMLStreamReader(url.openStream());
            Map<String, Boolean> isCurrentElementMap = new HashMap<>();
            isCurrentElementMap.put("Nominal", false);
            isCurrentElementMap.put("Name", false);
            isCurrentElementMap.put("Value", false);
            List<Currency> currencies = new ArrayList<>();
            Currency temp = null;
            boolean isCurrencyStarted = false; // to get only currencies
            while (reader.hasNext()) {
                int eventType = reader.next();

                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    String element = reader.getLocalName();
                    if(element.equals("ValType") && reader.getAttributeValue(0).equalsIgnoreCase("Xarici valyutalar")){
                        isCurrencyStarted = true;
                    }
                    if(isCurrencyStarted) {
                        if(element.equals("Valute")) {
                            temp = new Currency();
                            temp.setCode(reader.getAttributeValue(0));
                        } else if(isCurrentElementMap.containsKey(element)) {
                            isCurrentElementMap.put(element, true);
                        }
                    }

                } else if(isCurrencyStarted && eventType == XMLStreamConstants.CHARACTERS) {
                    String data = reader.getText();
                    if(isCurrentElementMap.get("Nominal")) {
                        temp.setNominal(Integer.parseInt(data));
                    } else if(isCurrentElementMap.get("Name")) {
                        temp.setName(data);
                    }else if(isCurrentElementMap.get("Value")) {
                        temp.setValue(new BigDecimal(data));
                    }
                } else if(isCurrencyStarted && eventType == XMLStreamConstants.END_ELEMENT) {
                    if(reader.getLocalName().equals("Valute")) {
                        currencies.add(temp);
                        temp = null;
                    }else if(isCurrentElementMap.containsKey(reader.getLocalName())) {
                        isCurrentElementMap.put(reader.getLocalName(), false);
                    }
                }
            }

            System.out.println(" ----- DATE : " + currentDate() + " ----- ");
            currencies.forEach(currency -> System.out.printf("%s : %s -> %s AZN \n",
                    currency.getCode(),
                    currency.getName(),
                    currency.getValue()
                    )
            );

        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String currentDate(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return now.format(formatter);
    }
}
