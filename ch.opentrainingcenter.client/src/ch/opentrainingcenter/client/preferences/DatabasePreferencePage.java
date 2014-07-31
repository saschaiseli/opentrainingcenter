package ch.opentrainingcenter.client.preferences;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.IDatabaseConnection;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;

public class DatabasePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private static final int INTENT = 5;
    private static final Logger LOGGER = Logger.getLogger(DatabasePreferencePage.class);
    private IPreferenceStore store;
    private IDatabaseConnection access;
    private boolean connectionSuccess = false;
    private Group groupAdminDb;
    private ComboFieldEditor dbChooser;
    private Composite dbAdminComposite;
    private StringFieldEditor dbAdminUrl;
    private StringFieldEditor dbAdminUser;
    private StringFieldEditor dbAdminPass;
    private StringFieldEditor dbUrl;
    private final IDatabaseService service;

    public DatabasePreferencePage() {
        super(GRID);
        setDescription(Messages.DatabasePreferencePage_0);
        service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
    }

    @Override
    public void init(final IWorkbench workbench) {
        store = Activator.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }

    @Override
    protected void createFieldEditors() {
        final Composite parent = getFieldEditorParent();
        parent.setLayout(GridLayoutFactory.swtDefaults().create());
        final GridData gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        parent.setLayoutData(gd);

        // ------------ Normale DB Connection
        final Group groupDb = new Group(parent, SWT.NONE);
        groupDb.setText(Messages.DatabasePreferencePage_10);
        groupDb.setLayout(GridLayoutFactory.swtDefaults().create());

        final Composite dbComposite = new Composite(groupDb, SWT.NONE);
        dbComposite.setLayout(GridLayoutFactory.swtDefaults().create());

        dbUrl = new StringFieldEditor(PreferenceConstants.DB_URL, Messages.DatabasePreferencePage_2, dbComposite);
        final StringFieldEditor dbUser = new StringFieldEditor(PreferenceConstants.DB_USER, Messages.DatabasePreferencePage_3, dbComposite);
        final StringFieldEditor dbPass = new StringFieldEditor(PreferenceConstants.DB_PASS, Messages.DatabasePreferencePage_4, dbComposite);

        addField(dbUser);
        addField(dbPass);
        addField(dbUrl);
        final Map<String, IDatabaseConnection> model = service.getAvailableConnections();
        initDatabaseAccess(model, store.getString(PreferenceConstants.DB));
        final String[][] entries = new String[model.size() + 1][model.size() + 1];
        entries[0] = new String[] { "", "" }; //$NON-NLS-1$//$NON-NLS-2$
        int i = 1;
        for (final Map.Entry<String, IDatabaseConnection> entry : model.entrySet()) {
            entries[i] = new String[] { entry.getValue().getName(), entry.getValue().getName() };
            i++;
        }

        dbChooser = new ComboFieldEditor(PreferenceConstants.DB, Messages.DatabasePreferencePage_6, entries, dbComposite) {

            @Override
            protected void fireValueChanged(final String property, final Object oldValue, final Object newValue) {
                super.fireValueChanged(property, oldValue, newValue);
                connectionSuccess = false;
                checkState();
                if (newValue != null) {
                    final String dbKey = (String) newValue;
                    initDatabaseAccess(model, dbKey);
                }
                boolean withAdmin = false;
                if (access != null) {
                    withAdmin = access.isUsingAdminDbConnection();
                }
                handleAdminConposite(withAdmin);
            }

        };

        addField(dbChooser);

        // ------------ Admin DB Connection

        groupAdminDb = new Group(parent, SWT.NONE);
        groupAdminDb.setText(Messages.DatabasePreferencePage_11);
        groupAdminDb.setLayout(GridLayoutFactory.swtDefaults().create());

        dbAdminComposite = new Composite(groupAdminDb, SWT.NONE);
        dbAdminComposite.setLayout(GridLayoutFactory.swtDefaults().create());

        dbAdminUrl = new StringFieldEditor(PreferenceConstants.DB_ADMIN_URL, Messages.DatabasePreferencePage_12, dbAdminComposite);
        dbAdminUser = new StringFieldEditor(PreferenceConstants.DB_ADMIN_USER, Messages.DatabasePreferencePage_13, dbAdminComposite);
        dbAdminPass = new StringFieldEditor(PreferenceConstants.DB_ADMIN_PASS, Messages.DatabasePreferencePage_14, dbAdminComposite);

        addField(dbAdminUser);
        addField(dbAdminPass);
        addField(dbAdminUrl);

        // -- layout
        GridDataFactory.defaultsFor(groupDb).grab(true, true).span(2, 1).indent(INTENT, INTENT).applyTo(groupDb);
        GridDataFactory.defaultsFor(groupAdminDb).grab(true, true).span(2, 1).indent(INTENT, INTENT).applyTo(groupAdminDb);

        setErrorMessage(Messages.DatabasePreferencePage_7);

        final Button validate = new Button(parent, SWT.NONE);
        validate.setText(Messages.DatabasePreferencePage_8);

        validate.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                connectionSuccess = false;
                if (access != null) {

                    final String url = dbUrl.getStringValue();
                    final String user = dbUser.getStringValue();
                    final String password = dbPass.getStringValue();
                    final DbConnection dbConnection = new DbConnection(access.getDriver(), access.getDialect(), url, user, password);
                    DbConnection dbAdminConnection = null;

                    if (access.isUsingAdminDbConnection()) {
                        final String adminUrl = dbAdminUrl.getStringValue();
                        final String adminUser = dbAdminUser.getStringValue();
                        final String adminPassword = dbAdminPass.getStringValue();
                        dbAdminConnection = new DbConnection(access.getDriver(), access.getDialect(), adminUrl, adminUser, adminPassword);
                    }

                    access.setConfig(new DatabaseConnectionConfiguration(dbConnection, dbAdminConnection));

                    connectionSuccess = DBSTATE.OK.equals(access.validateConnection(url, user, password, false).getState());

                    if (access.isUsingAdminDbConnection()) {
                        final String adminUrl = dbAdminUrl.getStringValue();
                        final String adminUser = dbAdminUser.getStringValue();
                        final String adminPassword = dbAdminPass.getStringValue();
                        connectionSuccess = DBSTATE.OK.equals(access.validateConnection(adminUrl, adminUser, adminPassword, true).getState());
                    }
                }
                if (!connectionSuccess) {
                    setErrorMessage(Messages.DatabasePreferencePage_9);
                } else {
                    dbChooser.store();
                }
                checkState();
                dbUrl.setFocus();
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
                // do nothing
            }
        });
        if (access != null) {
            handleAdminConposite(access.isUsingAdminDbConnection());
        }
    }

    private void initDatabaseAccess(final Map<String, IDatabaseConnection> model, final String dbKey) {
        access = model.get(dbKey);
    }

    private void handleAdminConposite(final boolean withAdmin) {
        groupAdminDb.setEnabled(withAdmin);
        dbAdminComposite.setEnabled(withAdmin);
        dbAdminUrl.setEnabled(withAdmin, dbAdminComposite);
        dbAdminUser.setEnabled(withAdmin, dbAdminComposite);
        dbAdminPass.setEnabled(withAdmin, dbAdminComposite);
        dbAdminUrl.setEmptyStringAllowed(!withAdmin);
        dbAdminUser.setEmptyStringAllowed(!withAdmin);
        dbAdminPass.setEmptyStringAllowed(!withAdmin);
    }

    @Override
    protected void checkState() {
        setValid(connectionSuccess);
    }
}
