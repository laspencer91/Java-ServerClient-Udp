package shared.Messages.Types;

public enum NetMessageId {
    PLAYER_INPUT ((short) 0);

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
