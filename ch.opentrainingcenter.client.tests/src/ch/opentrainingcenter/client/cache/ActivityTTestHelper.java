package ch.opentrainingcenter.client.cache;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.opentrainingcenter.tcx.ActivityT;

public class ActivityTTestHelper {

    public static ActivityT createActivity(final int year) throws DatatypeConfigurationException {
        final ActivityT activity = new ActivityT();
        final GregorianCalendar gcal = new GregorianCalendar();
        final XMLGregorianCalendar id = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        id.setTime(11, 55, 05, 1);
        id.setYear(year);
        id.setMonth(8);
        id.setDay(29);
        activity.setId(id);
        return activity;
    }
}
