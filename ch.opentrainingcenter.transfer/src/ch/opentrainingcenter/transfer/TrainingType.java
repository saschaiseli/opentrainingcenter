package ch.opentrainingcenter.transfer;

public enum TrainingType {
    NONE(0, "UNBEKANNT", "Unbekannter Typ", "icons/man_u_32_32.png"), //
    EXT_INTERVALL(1, "EXT_INTERVALL", "Unbekannter Typ", "icons/man_u_32_32.png"), //
    INT_INTERVALL(2, "INT_INTERVALL", "Unbekannter Typ", "icons/man_u_32_32.png"), //
    LONG_JOG(3, "LONG_JOG", "Unbekannter Typ", "icons/man_u_32_32.png"), //
    POWER_LONG_JOG(4, "POWER_LONG_JOG", "Unbekannter Typ", "icons/man_u_32_32.png"), //
    TEMPO_JOG(5, "TEMPO_JOG", "Unbekannter Typ", "icons/man_u_32_32.png");

    private final int index;
    private final String name;
    private final String message;
    private final String image;

    private TrainingType(final int index, final String name, final String message, final String image) {
        this.index = index;
        this.name = name;
        this.message = message;
        this.image = image;
    }

    public int getId() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    public static TrainingType getById(final int index) {
        switch (index) {
        case 0:
            return NONE;
        case 1:
            return EXT_INTERVALL;
        case 2:
            return INT_INTERVALL;
        case 3:
            return LONG_JOG;
        case 4:
            return POWER_LONG_JOG;
        case 5:
            return TEMPO_JOG;
        default:
            throw new IllegalArgumentException(String.format("TrainingTyp mit dem Index %s existiert nicht", index)); //$NON-NLS-1$
        }
    }
}
