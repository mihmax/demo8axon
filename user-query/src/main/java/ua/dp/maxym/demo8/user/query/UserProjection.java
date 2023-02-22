package ua.dp.maxym.demo8.user.query;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.dp.maxym.demo8.user.event.UserCreatedEvent;
import ua.dp.maxym.demo8.user.event.UserMoneyChangedEvent;

@Component
public class UserProjection {

    private final UserRepository userRepo;

    @Autowired
    public UserProjection(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @EventHandler
    public void on(UserCreatedEvent event) {
        userRepo.save(new User(event.email(), event.firstName(), event.lastName(), event.money()));
    }

    @EventHandler
    public void on(UserMoneyChangedEvent event) {
        User user = userRepo.findByEmail(event.userId());
        if (user == null) throw new RuntimeException(String.format("WTF User %s not found", event.userId()));

        user.setMoney(event.newMoney());
        userRepo.save(user);
    }
}
