package dl.chatty.datetime;

import java.util.Date;

public class DefaultDateTimeSupplier implements DateTimeSupplier {

    @Override
    public Date get() {
        return new Date();
    }

}
