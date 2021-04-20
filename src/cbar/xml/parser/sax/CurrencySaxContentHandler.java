package cbar.xml.parser.sax;

import cbar.xml.domain.Currency;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CurrencySaxContentHandler extends DefaultHandler {
    private Currency temp;
    private boolean isCurrency;
    private boolean isNominal;
    private boolean isName;
    private boolean isValue;
    List<Currency> currencies;

    @Override
    public void startDocument() {
        this.currencies = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        if(qName.equals("ValType") && attributes.getValue("Type").equalsIgnoreCase("Xarici valyutalar")){
            isCurrency = true;
        }

        if(isCurrency){
            switch (qName) {
                case "Valute":
                    temp = new Currency();
                    temp.setCode(attributes.getValue("Code"));
                    break;
                case "Nominal":
                    isNominal = true;
                    break;
                case "Name":
                    isName = true;
                    break;
                case "Value":
                    isValue = true;
                    break;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length){
        String data = new String(ch, start, length);
        if(isCurrency){
            if(isNominal){
                temp.setNominal(Integer.parseInt(data));
            } else if(isName){
                temp.setName(data);
            } else if(isValue){
                temp.setValue(new BigDecimal(data));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(isCurrency && qName.equals("Valute")) {
            this.currencies.add(temp);
            temp = null;
        }
        isNominal = false;
        isName    = false;
        isValue   = false;
    }


    public List<Currency> getCurrencies() {
        return currencies;
    }
}
