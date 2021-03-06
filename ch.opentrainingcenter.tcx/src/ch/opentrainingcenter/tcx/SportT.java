//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2011.09.04 at 07:10:05 PM MESZ
//

package ch.opentrainingcenter.tcx;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Sport_t.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="Sport_t">
 *   &lt;restriction base="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}Token_t">
 *     &lt;enumeration value="Running"/>
 *     &lt;enumeration value="Biking"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Sport_t")
@XmlEnum
public enum SportT {

    @XmlEnumValue("Running")
    RUNNING("Running"), @XmlEnumValue("Biking")//$NON-NLS-1$
    BIKING("Biking"), @XmlEnumValue("Other")//$NON-NLS-1$
    OTHER("Other");//$NON-NLS-1$
    private final String value;

    SportT(final String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SportT fromValue(final String v) {
        for (final SportT c : SportT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
