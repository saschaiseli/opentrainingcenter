package ch.opentrainingcenter.route;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class CompareRouteFactoryTest {

    @Test
    public void test() {
        assertNotNull(RouteFactory.getRouteComparator(false, "kmlDumpPath"));
    }

}
