package dl.chatty.id;

import java.util.UUID;

public class UUIDIdSupplier implements IdSupplier<String> {

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }

}
