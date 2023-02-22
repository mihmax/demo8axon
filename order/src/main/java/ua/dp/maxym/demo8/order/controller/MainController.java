package ua.dp.maxym.demo8.order.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dp.maxym.demo8.common.util.AxonUtil;
import ua.dp.maxym.demo8.order.aggregate.OrderAggregate;
import ua.dp.maxym.demo8.order.command.CreateOrderCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    private final Repository<OrderAggregate> orderRepository;
    private final CommandGateway commandGateway;

    @Autowired
    public MainController(
            @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            Repository<OrderAggregate> orderRepository,
            CommandGateway commandGateway) {
        this.orderRepository = orderRepository;
        this.commandGateway = commandGateway;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/")
    public String index() {
        return """
                Call this to <a href="/create">create the order</a></a>
                <br/>
                <a href="/list">List the orders</a>
                """;
    }

    @GetMapping("/create")
    public String create() {
        int nPreviousOrders = list().get("orders").size();
        String orderId = commandGateway
                .sendAndWait(new CreateOrderCommand(String.valueOf(nPreviousOrders),
                                                    "test2@gmail.com",
                                                    Map.of("Item1", 2,
                                                           "Item2", 1)));
        return String.format("""
                                     Created %s, see <a href="/list">the list of orders</a>
                                     """, orderId);
    }

    @GetMapping("/list")
    public Map<String, List<OrderAggregate>> list() {
        UnitOfWork<Message<?>> uow = DefaultUnitOfWork.startAndGet(null);
        List<OrderAggregate> orders = new ArrayList<>();
        try {
            // Only way without view model
            //noinspection InfiniteLoopStatement
            for (int i = 0; ; i++) {
                orders.add(AxonUtil.unwrap(orderRepository.load(String.valueOf(i))));
            }
        } catch (Exception e) {
            // ignore
        } finally {
            uow.rollback();
        }
        return Map.of("orders", orders);
    }
}
