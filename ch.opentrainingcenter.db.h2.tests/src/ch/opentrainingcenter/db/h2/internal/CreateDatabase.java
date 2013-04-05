package ch.opentrainingcenter.db.h2.internal;

import ch.opentrainingcenter.db.h2.DatabaseAccess;
import ch.opentrainingcenter.db.h2.internal.Dao.USAGE;

public class CreateDatabase {
    private static DatabaseAccess access;

    public static void main(final String[] args) {
        access = new DatabaseAccess(new Dao(USAGE.TEST));
        access.createDatabase();
    }
}
