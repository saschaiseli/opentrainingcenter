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
 * Java class for TriggerMethod_t.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="TriggerMethod_t">
 *   &lt;restriction base="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}Token_t">
 *     &lt;enumeration value="Manual"/>
 *     &lt;enumeration value="Distance"/>
 *     &lt;enumeration value="Location"/>
 *     &lt;enumeration value="Time"/>
 *     &lt;enumeration value="HeartRate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TriggerMethod_t")
@XmlEnum
public enum TriggerMethodT {

    @XmlEnumValue("Manual")
    MANUAL("Manual"), @XmlEnumValue("Distance")//$NON-NLS-1$
    DISTANCE("Distance"), @XmlEnumValue("Location")//$NON-NLS-1$
    LOCATION("Location"), @XmlEnumValue("Time")//$NON-NLS-1$
    TIME("Time"), @XmlEnumValue("HeartRate")//$NON-NLS-1$
    HEART_RATE("HeartRate");//$NON-NLS-1$
    private final String value;

    TriggerMethodT(final String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TriggerMethodT fromValue(final String v) {
        for (final TriggerMethodT c : TriggerMethodT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}