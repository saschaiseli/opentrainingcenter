package ch.iseli.sportanalyzer.client;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import ch.iseli.sportanalyzer.client.action.ImportManualGpsFiles;
import ch.iseli.sportanalyzer.client.action.RestartOtc;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction restart;
    private IWorkbenchAction exitAction;
    private IAction windowsAction;
    private IWorkbenchAction aboutAction;
    private Action importGpsFilesManual;
    private IContributionItem perspectiveShortList;

    public ApplicationActionBarAdvisor(final IActionBarConfigurer configurer) {
        super(configurer);
    }

    @Override
    protected void makeActions(final IWorkbenchWindow window) {

        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);

        restart = new RestartOtc(window, Messages.ApplicationActionBarAdvisor_Restart);
        register(restart);

        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);

        windowsAction = ActionFactory.PREFERENCES.create(window);
        register(windowsAction);

        importGpsFilesManual = new ImportManualGpsFiles(window, Messages.ApplicationActionBarAdvisor_ImportGpsFiles);
        register(importGpsFilesManual);

        perspectiveShortList = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
    }

    @Override
    protected void fillMenuBar(final IMenuManager menuBar) {
        final MenuManager fileMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_File, IWorkbenchActionConstants.M_FILE);
        final MenuManager helpMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_Help, IWorkbenchActionConstants.M_HELP);
        final MenuManager windowsMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_Windows, IWorkbenchActionConstants.M_WINDOW);
        menuBar.add(fileMenu);
        menuBar.add(windowsMenu);
        menuBar.add(helpMenu);

        final MenuManager layoutMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_SwitchPerspective, "layout"); //$NON-NLS-2$
        layoutMenu.add(perspectiveShortList);

        // File
        fileMenu.add(layoutMenu);
        fileMenu.add(restart);
        fileMenu.add(exitAction);

        // Window
        windowsMenu.add(windowsAction);

        // Help
        helpMenu.add(aboutAction);

    }

    @Override
    protected void fillCoolBar(final ICoolBarManager coolBar) {
        final IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main")); //$NON-NLS-1$
        toolbar.add(importGpsFilesManual);
    }
}
