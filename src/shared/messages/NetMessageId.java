package shared.messages;

public enum NetMessageId {
    CONNECTION_REQUEST((short) 0),
    PLAYER_INPUT ((short) 1);

    private short value;

    private static NetMessageId[] values = values();

    NetMessageId(short value) {
        this.value = value;
    }

    public static NetMessageId fromShort(short readShort) {
        return values[readShort];
    }

    public short shortValue() {
        return value;
    }
}
