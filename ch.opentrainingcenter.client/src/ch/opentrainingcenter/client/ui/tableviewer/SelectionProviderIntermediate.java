package ch.opentrainingcenter.client.ui.tableviewer;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * IPostSelectionProvider implementation that delegates to another
 * ISelectionProvider or IPostSelectionProvider. The selection provider used for
 * delegation can be exchanged dynamically. Registered listeners are adjusted
 * accordingly. This utility class may be used in workbench parts with multiple
 * viewers.
 * 
 * @author Marc R. Hoffmann
 */
public class SelectionProviderIntermediate implements IPostSelectionProvider {

    private final ListenerList selectionListeners = new ListenerList();

    private final ListenerList postSelectionListeners = new ListenerList();

    private ISelectionProvider delegate;

    private final ISelectionChangedListener selectionListener = new ISelectionChangedListener() {
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            if (event.getSelectionProvider() == delegate) {
                fireSelectionChanged(event.getSelection());
            }
        }
    };

    private final ISelectionChangedListener postSelectionListener = new ISelectionChangedListener() {
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            if (event.getSelectionProvider() == delegate) {
                firePostSelectionChanged(event.getSelection());
            }
        }
    };

    /**
     * Sets a new selection provider to delegate to. Selection listeners
     * registered with the previous delegate are removed before.
     * 
     * @param newDelegate
     *            new selection provider
     */
    public void setSelectionProviderDelegate(final ISelectionProvider newDelegate) {
        if (delegate == newDelegate) {
            return;
        }
        if (delegate != null) {
            delegate.removeSelectionChangedListener(selectionListener);
            if (delegate instanceof IPostSelectionProvider) {
                ((IPostSelectionProvider) delegate).removePostSelectionChangedListener(postSelectionListener);
            }
        }
        delegate = newDelegate;
        if (newDelegate != null) {
            newDelegate.addSelectionChangedListener(selectionListener);
            if (newDelegate instanceof IPostSelectionProvider) {
                ((IPostSelectionProvider) newDelegate).addPostSelectionChangedListener(postSelectionListener);
            }
            fireSelectionChanged(newDelegate.getSelection());
            firePostSelectionChanged(newDelegate.getSelection());
        }
    }

    protected void fireSelectionChanged(final ISelection selection) {
        fireSelectionChanged(selectionListeners, selection);
    }

    protected void firePostSelectionChanged(final ISelection selection) {
        fireSelectionChanged(postSelectionListeners, selection);
    }

    private void fireSelectionChanged(final ListenerList list, final ISelection selection) {
        final SelectionChangedEvent event = new SelectionChangedEvent(delegate, selection);
        final Object[] listeners = list.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            final ISelectionChangedListener listener = (ISelectionChangedListener) listeners[i];
            listener.selectionChanged(event);
        }
    }

    // IPostSelectionProvider Implementation

    @Override
    public void addSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionListeners.add(listener);
    }

    @Override
    public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionListeners.remove(listener);
    }

    @Override
    public void addPostSelectionChangedListener(final ISelectionChangedListener listener) {
        postSelectionListeners.add(listener);
    }

    @Override
    public void removePostSelectionChangedListener(final ISelectionChangedListener listener) {
        postSelectionListeners.remove(listener);
    }

    @Override
    public ISelection getSelection() {
        return delegate == null ? null : delegate.getSelection();
    }

    @Override
    public void setSelection(final ISelection selection) {
        if (delegate != null) {
            delegate.setSelection(selection);
        }
    }

}