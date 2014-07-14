package ch.opentrainingcenter.client.views.navigation.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Locale;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class KalenderWocheTreeLabelProviderTest {

    private KalenderWocheTreeLabelProvider provider;

    @Before
    public void before() {
        provider = new KalenderWocheTreeLabelProvider();
    }

    @Test
    public void testGetTextNoElement() {
        final INavigationParent element = ModelFactory.createNavigationParent();
        final String result = provider.getText(element);
        assertEquals("KW", result);
    }

    @Test
    public void testGetTextElement() {
        final INavigationParent element = ModelFactory.createNavigationParent();
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(2013, 0, 01);
        final IHealth health = CommonTransferFactory.createHealth(null, null, null, cal.getTime());
        final INavigationItem item = ModelFactory.createConcreteHealth(health, null);
        element.add(item);
        final String result = provider.getText(element);
        assertEquals("KW1", result);
    }

    @Test
    public void testGetTextElementInteger() {
        final String jahr = "2013";
        final Integer element = Integer.valueOf(jahr);

        final String result = provider.getText(element);
        assertEquals("Das Jahr wird dargestellt", jahr, result);
    }

    @Test
    public void testGetTextElementINavigationItem() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(2013, 11, 25);
        final IHealth health = CommonTransferFactory.createHealth(null, null, null, cal.getTime());
        final INavigationItem item = ModelFactory.createConcreteHealth(health, null);

        final String result = provider.getText(item);
        assertEquals("Das Jahr wird dargestellt", "25.12.2013", result);
    }

    @Test
    public void testGetTooltip_Jahr() {
        final String result = provider.getToolTipText(2014);
        assertNull("Das Jahr hat keinen Tooltip", result);
    }

    @Test
    public void testGetTooltip_INavigationItem() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(2013, 11, 25);
        final ConcreteImported item = mock(ConcreteImported.class);
        final String tooltip = "tooltip";
        when(item.getTooltip()).thenReturn(tooltip);

        final String result = provider.getToolTipText(item);

        assertEquals(tooltip, result);
    }

    @Test
    public void testGetTooltip_NavigationParent() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(2013, 11, 25);
        final INavigationParent item = mock(INavigationParent.class);
        final String tooltip = "tooltip";
        when(item.getTooltip()).thenReturn(tooltip);

        final String result = provider.getToolTipText(item);

        assertEquals(tooltip, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTooltip_Other() {
        provider.getToolTipText(Boolean.FALSE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTextElementException() {
        provider.getText(Boolean.TRUE);
    }

    @Test
    @Ignore
    public void testGetImageObjectINavigationParent() {
        final INavigationParent element = ModelFactory.createNavigationParent();
        final Image result = provider.getImage(element);
        assertEquals(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER), result);
    }

    @Test
    @Ignore
    public void testGetImageObjectInteger() {
        final Image result = provider.getImage(Integer.valueOf(42));
        assertEquals(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER), result);
    }

    @Test
    @Ignore
    public void testGetImageObjectOther() {
        final Image result = provider.getImage(Boolean.FALSE);
        assertEquals(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE), result);
    }
}
