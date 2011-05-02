package org.jboss.seamcatch.judcon.demo.exceptionhandler;

import org.jboss.seam.exception.control.CaughtException;
import org.jboss.seam.exception.control.Handles;
import org.jboss.seam.exception.control.HandlesExceptions;
import org.jboss.seam.exception.control.TraversalMode;
import org.jboss.seam.exception.filter.ExceptionStackOutput;
import org.jboss.seam.exception.filter.StackFrame;
import org.jboss.seam.exception.filter.StackFrameFilter;
import org.jboss.seam.exception.filter.StackFrameFilterResult;
import org.jboss.seam.international.status.Messages;

import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExternalContext;
import javax.persistence.PersistenceException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.IOException;

/**
 * @author <a href="http://community.jboss.org/people/LightGuard">Jason Porter</a>
 */
@HandlesExceptions
public class StandardHandlers {
    public void rollback(@Handles CaughtException<PersistenceException> event, UserTransaction tx, Messages msgs) {
        msgs.info("Error communicating with the database, please try again");
        try {
            if (tx.getStatus() != Status.STATUS_NO_TRANSACTION && tx.getStatus() != Status.STATUS_MARKED_ROLLBACK)
                tx.rollback();
        } catch (SystemException e) {
            // Nothing to do here
        }
    }

    public void viewExpired(@Handles CaughtException<ViewExpiredException> event, ExternalContext ctx) {
        try {
            ctx.dispatch("/view_expired.html");
        } catch (IOException e) {
            // Nothing to do here
        }
        event.handled();
    }

    public void log(@Handles(during = TraversalMode.BREADTH_FIRST) CaughtException<Throwable> event) {
        System.out.println(new ExceptionStackOutput<Throwable>(event.getException(), new StackFrameFilter<Throwable>() {
            @Override
            public StackFrameFilterResult process(StackFrame stackFrame) {
                if (stackFrame.getStackTraceElement().getClassName().contains("byteman")) {
                    stackFrame.mark("byteman");
                    return StackFrameFilterResult.DROP;
                }

                if (stackFrame.isMarkSet("byteman"))
                    return StackFrameFilterResult.INCLUDE;

                return StackFrameFilterResult.DROP;
            }
        }).printTrace());
    }

    public void iae(@Handles CaughtException<IllegalArgumentException> event, Messages msgs)
    {
        msgs.info(event.getException().getMessage());
        event.handled();
    }
}
