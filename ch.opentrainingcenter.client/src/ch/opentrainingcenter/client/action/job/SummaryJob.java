package ch.opentrainingcenter.client.action.job;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.opentrainingcenter.client.views.summary.SummaryView;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ITraining;

public class SummaryJob extends Job {

    private final SummaryView view;
    private final SummaryAction action;

    public SummaryJob(final String name, final List<ITraining> trainings, final SummaryView view) {
        super(name);
        action = new SummaryAction(trainings);
        this.view = view;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.SummaryJob_Summary, action.size());
        view.setData(action.calculateSummary());
        return Status.OK_STATUS;
    }

}
