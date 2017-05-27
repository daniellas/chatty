package dl.chatty.datetime;

import org.junit.Assert;
import org.junit.Test;

public class DefaultDateTimeSupplierTest {

    @Test
    public void shouldReturnNonEmptyCurrentDate() {
        Assert.assertNotNull(new DefaultDateTimeSupplier().get());
    }
}
