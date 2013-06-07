package ch.opentrainingcenter.client.views.navigation;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jface.action.IStatusLineManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.model.navigation.INavigationItem;

@SuppressWarnings("nls")
public class StatusLineWriterTest {
    private IStatusLineManager manager;
    private Date date;
    private StatusLineWriter writer;

    class DummyItem implements INavigationItem {

        @Override
        public int compareTo(final INavigationItem o) {
            return 0;
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public Date getDate() {
            return date;
        }

        @Override
        public String getImage() {
            return null;
        }
    }

    @Before
    public void before() {
        manager = Mockito.mock(IStatusLineManager.class);

        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 07, 11, 21, 23, 42);
        date = cal.getTime();
        writer = new StatusLineWriter(manager);
    }

    @Test
    public void testParentNull() {
        writer.writeStatusLine(null);
        Mockito.verify(manager).setMessage(""); //$NON-NLS-1$
    }

}
