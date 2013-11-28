//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.26 at 11:38:59 AM CET 
//


package com.garmin.xmlschemas.gpxextensions.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DisplayMode_t.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DisplayMode_t">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="SymbolOnly"/>
 *     &lt;enumeration value="SymbolAndName"/>
 *     &lt;enumeration value="SymbolAndDescription"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DisplayMode_t")
@XmlEnum
public enum DisplayModeT {

    @XmlEnumValue("SymbolOnly")
    SYMBOL_ONLY("SymbolOnly"),
    @XmlEnumValue("SymbolAndName")
    SYMBOL_AND_NAME("SymbolAndName"),
    @XmlEnumValue("SymbolAndDescription")
    SYMBOL_AND_DESCRIPTION("SymbolAndDescription");
    private final String value;

    DisplayModeT(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DisplayModeT fromValue(String v) {
        for (DisplayModeT c: DisplayModeT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
