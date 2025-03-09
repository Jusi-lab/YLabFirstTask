package util;

import java.util.UUID;

// Генератор уникальных идентификаторов

public class IdGenerator {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}