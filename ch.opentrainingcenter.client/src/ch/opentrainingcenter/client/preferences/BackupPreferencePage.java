package ch.opentrainingcenter.client.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.i18n.Messages;

public class BackupPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public BackupPreferencePage() {
        super(GRID);
        setDescription(Messages.BackupPreferencePage0);
    }

    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        final Composite fieldEditorParent = getFieldEditorParent();

        addField(new DirectoryFieldEditor(PreferenceConstants.BACKUP_FILE_LOCATION, Messages.BackupPreferencePage1, fieldEditorParent));
    }

}
