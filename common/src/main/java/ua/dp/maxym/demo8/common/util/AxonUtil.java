package ua.dp.maxym.demo8.common.util;

import org.axonframework.eventsourcing.EventSourcedAggregate;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.LockAwareAggregate;

public class AxonUtil {

    public static <T> T unwrap(Aggregate<T> wrapped) {
        if (wrapped instanceof LockAwareAggregate<T, ?> laa) {
            if (laa.getWrappedAggregate() instanceof EventSourcedAggregate<T> esa) {
                return esa.getAggregateRoot();
            }
        }
        throw new RuntimeException("Axon framework likely changed and this hacky method no longer works");
    }
}
