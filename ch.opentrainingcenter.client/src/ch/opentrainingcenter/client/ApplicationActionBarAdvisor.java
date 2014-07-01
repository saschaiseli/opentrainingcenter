package ch.opentrainingcenter.client;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import ch.opentrainingcenter.client.action.RestartOtc;
import ch.opentrainingcenter.i18n.Messages;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction restart;
    private IWorkbenchAction exitAction;
    private IAction windowsAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction introAction;

    public ApplicationActionBarAdvisor(final IActionBarConfigurer configurer) {
        super(configurer);

    }

    @Override
    protected void makeActions(final IWorkbenchWindow window) {

        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);

        restart = new RestartOtc(window, Messages.ApplicationActionBarAdvisorRestart);
        register(restart);

        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);

        windowsAction = ActionFactory.PREFERENCES.create(window);
        register(windowsAction);

        introAction = ActionFactory.INTRO.create(window);
        register(introAction);
    }

    @Override
    protected void fillMenuBar(final IMenuManager menuBar) {
        final MenuManager fileMenu = new MenuManager(Messages.ApplicationActionBarAdvisorFile, IWorkbenchActionConstants.M_FILE);
        final MenuManager helpMenu = new MenuManager(Messages.ApplicationActionBarAdvisorHelp, IWorkbenchActionConstants.M_HELP);
        final MenuManager windowsMenu = new MenuManager(Messages.ApplicationActionBarAdvisorWindows, IWorkbenchActionConstants.M_WINDOW);
        menuBar.add(fileMenu);
        menuBar.add(windowsMenu);
        menuBar.add(helpMenu);

        // File
        fileMenu.add(restart);
        fileMenu.add(exitAction);

        // Window
        windowsMenu.add(windowsAction);

        // Help
        helpMenu.add(aboutAction);

        // Intro
        helpMenu.add(introAction);
    }

    @Override
    protected void fillCoolBar(final ICoolBarManager coolBar) {
    }
}
