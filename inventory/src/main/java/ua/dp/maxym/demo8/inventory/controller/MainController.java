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
import ua.dp.maxym.demo8.inventory.aggregate.WarehouseAggregate;
import ua.dp.maxym.demo8.inventory.command.AddSKUCommand;
import ua.dp.maxym.demo8.inventory.command.CreateWarehouseCommand;

import java.util.Map;

@RestController
public class MainController {

    public static final String WAREHOUSE_NAME = "warehouse1";
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    Repository<WarehouseAggregate> warehouseRepository;
    @Autowired
    private CommandGateway commandGateway;

    @GetMapping("/")
    public String index() {
        return """
                Call this to create warehouse and add SKUs: <a href="/create">Create the repository / add more stuff</a></a>
                <br/>
                <a href="/list">List the warehouse</a>
                """;
    }

    @GetMapping("/create")
    public String create() {
        commandGateway.send(new CreateWarehouseCommand(WAREHOUSE_NAME));
        commandGateway.send(new AddSKUCommand(WAREHOUSE_NAME, "Item1", 10, 100.0));
        commandGateway.send(new AddSKUCommand(WAREHOUSE_NAME, "Item2", 5, 50.0));
        return """
                Created, see <a href="/list">Contents of warehouse</a>
                """;
    }

    @GetMapping("/list")
    public Map<String, WarehouseAggregate> list() {
        UnitOfWork<Message<?>> uow = DefaultUnitOfWork.startAndGet(null);
        try {
            return Map.of(WAREHOUSE_NAME, AxonUtil.unwrap(warehouseRepository.load(WAREHOUSE_NAME)));
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(e.getMessage(), null);
        } finally {
            uow.rollback();
        }
    }
}
