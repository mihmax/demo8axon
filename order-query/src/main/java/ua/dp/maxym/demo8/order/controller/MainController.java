package ua.dp.maxym.demo8.order.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dp.maxym.demo8.order.command.CreateOrderCommand;
import ua.dp.maxym.demo8.order.query.Order;
import ua.dp.maxym.demo8.order.query.OrderRepository;

import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    private final OrderRepository orderRepo;
    private final CommandGateway commandGateway;

    @Autowired
    public MainController(OrderRepository orderRepo, CommandGateway commandGateway) {
        this.orderRepo = orderRepo;
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
        int nPreviousOrders = list().size();
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
    public List<Order> list() {
        return orderRepo.findAll();
    }
}
