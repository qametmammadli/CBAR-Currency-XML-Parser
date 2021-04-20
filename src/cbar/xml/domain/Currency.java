package cbar.xml.domain;

import java.math.BigDecimal;

public class Currency {
    private String code;
    private int nominal;
    private String name;
    private BigDecimal value;

    public String getCode() {
        return code;
    }

    public int getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
