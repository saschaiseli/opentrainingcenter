package ch.opentrainingcenter.db.internal;

import ch.opentrainingcenter.db.DatabaseAccess;
import ch.opentrainingcenter.db.internal.Dao;
import ch.opentrainingcenter.db.internal.Dao.USAGE;

public class CreateDatabase {
    private static DatabaseAccess access;

    public static void main(final String[] args) {
        access = new DatabaseAccess(new Dao(USAGE.TEST));
        access.createDatabase();
    }
}
