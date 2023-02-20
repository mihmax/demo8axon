package ua.dp.maxym.demo8.user.aggregate;

import com.mongodb.lang.NonNull;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ua.dp.maxym.demo8.common.event.UserCreatedEvent;

@Component
@ProcessingGroup("validateUniqueEmail")
class UserCreatedEventSubscriber {
    @EventHandler
    public void on(@NonNull UserCreatedEvent userCreatedEvent, @NonNull EmailRepository emailRepository) {
        emailRepository.save(new Email(userCreatedEvent.email()));
    }
}
