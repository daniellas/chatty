package dl.chatty.id;

import org.junit.Assert;
import org.junit.Test;

public class UUIDIdSupplierTest {

    @Test
    public void shouldReturnId() {
        Assert.assertNotNull(new UUIDIdSupplier().get());
    }
}
