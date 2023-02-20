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
import ua.dp.maxym.demo8.inventory.aggregate.GoodsAggregate;
import ua.dp.maxym.demo8.order.aggregate.OrderAggregate;
import ua.dp.maxym.demo8.order.command.CreateOrderCommand;
import ua.dp.maxym.demo8.user.aggregate.UserAggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    Repository<OrderAggregate> orderRepository;
    //@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    //@Autowired
    //Repository<UserAggregate> userRepository;
    //@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    //@Autowired
    //Repository<GoodsAggregate> goodsRepository;
    @Autowired
    private CommandGateway commandGateway;

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
        String orderId = null;
        int nPreviousOrders = list().get("orders").size();
        var user = new UserAggregate();
        user.email = "test1@gmail.com";
        var item1 = new GoodsAggregate();
        item1.name = "Item1";
        item1.quantity = 1;
        item1.pricePerItem = 100.0;
        var item2 = new GoodsAggregate();
        item2.name = "Item2";
        item2.quantity = 10;
        item2.pricePerItem = 50.0;
        commandGateway.send(new CreateOrderCommand(nPreviousOrders, user, List.of(item1, item2)));
        return String.format("""
                                     Created %s, see <a href="/list">the list of orders</a>
                                     """, orderId);
    }

    @GetMapping("/list")
    public Map<String, List<OrderAggregate>> list() {
        UnitOfWork<Message<?>> uow = DefaultUnitOfWork.startAndGet(null);
        List<OrderAggregate> orders = new ArrayList<>();
        try {
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
