package ch.opentrainingcenter.client.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.ExtensionHelper;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.i18n.Messages;

public class ImportPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public ImportPreferencePage() {
        super(GRID);
        setDescription(Messages.ImportPreferencePage0);
    }

    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        final Composite fieldEditorParent = getFieldEditorParent();

        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION, Messages.SamplePreferencePage1, fieldEditorParent));

        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION_PROG, Messages.SamplePreferencePage2, fieldEditorParent));
        final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());
        for (final IConvert2Tcx tcx : cc.getAllConverter()) {
            final String paramName = PreferenceConstants.FILE_SUFFIX_FOR_BACKUP + tcx.getFilePrefix();
            final String label = tcx.getName() + "   (*." + tcx.getFilePrefix() + ")";//$NON-NLS-1$ //$NON-NLS-2$

            final BooleanFieldEditor formatOnSave = new BooleanFieldEditor(paramName, label, getFieldEditorParent());
            addField(formatOnSave);
        }
    }
}
