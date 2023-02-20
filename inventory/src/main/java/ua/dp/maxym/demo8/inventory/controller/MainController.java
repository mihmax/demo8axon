package ua.dp.maxym.demo8.inventory.controller;

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
import ua.dp.maxym.demo8.inventory.command.CreateGoodsCommand;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class MainController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    Repository<GoodsAggregate> goodsRepository;
    @Autowired
    private CommandGateway commandGateway;

    @GetMapping("/")
    public String index() {
        return """
                Call this once: <a href="/create">Create the repository</a></a>
                <br/>
                <a href="/list">List the repository</a>
                """;
    }

    @GetMapping("/create")
    public String create() {
        commandGateway.send(new CreateGoodsCommand("Item1", 10, 100.0));
        commandGateway.send(new CreateGoodsCommand("Item2", 5, 50.0));
        return """
                Created, see <a href="/list">the list of goods in inventory</a>
                """;
    }

    @GetMapping("/list")
    public Map<String, List<GoodsAggregate>> list() {
        UnitOfWork<Message<?>> uow = DefaultUnitOfWork.startAndGet(null);
        try {
            return Map.of("goods",
                          Stream.of("Item1", "Item2").map((goods) -> AxonUtil.unwrap(goodsRepository.load(goods)))
                                .toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(e.getMessage(), List.of());
        } finally {
            uow.rollback();
        }
    }
}
