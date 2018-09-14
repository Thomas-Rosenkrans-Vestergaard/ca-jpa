package com.tvestergaard.ca;

import com.tvestergaard.ca.entities.Customer;
import com.tvestergaard.ca.entities.ItemType;
import com.tvestergaard.ca.entities.Order;
import com.tvestergaard.ca.entities.OrderLine;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class Facade implements Repository
{

    /**
     * The {@code EntityManagerFactory} that the facade performs operations upon.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Creates a new {@link Facade}.
     *
     * @param entityManagerFactory The {@code EntityManagerFactory} that the facade performs operations upon.
     */
    public Facade(EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Creates a new {@link TransactionalRepository} from a newly created {@code EntityManager} from the declared
     * {@code EntityManagerFactory}.
     *
     * @return The resulting {@link TransactionalRepository}.
     */
    private TransactionalRepository newTransactionalRepository()
    {
        return new TransactionalRepository(entityManagerFactory);
    }

    /**
     * Creates a new customer with the provided name and email. The results are committed automatically when no
     * exception occurs. When an exception occurs the results are rolled back.
     *
     * @param name  The name of the customer to create.
     * @param email The email of the customer to create.
     * @return The newly created customer entity.
     */
    @Override public Customer createCustomer(String name, String email)
    {
        TransactionalRepository transactionalRepository = newTransactionalRepository();
        try {
            transactionalRepository.begin();
            Customer customer = transactionalRepository.createCustomer(name, email);
            transactionalRepository.commit();
            return customer;
        } catch (Exception e) {
            transactionalRepository.rollback();
            throw e;
        } finally {
            transactionalRepository.close();
        }
    }

    /**
     * Finds the customer with the provided id.
     *
     * @param id The id of the customer to find.
     * @return The customer with the provided id, {@code null} when no such customer exists.
     */
    @Override public Customer findCustomer(long id)
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            return transactionalRepository.findCustomer(id);
        }
    }

    /**
     * Returns a complete list of customers.
     *
     * @return The complete list of customers.
     */
    @Override public List<Customer> getCustomers()
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            return transactionalRepository.getCustomers();
        }
    }

    /**
     * Creates a new order from the provided arguments. The results are committed automatically when no
     * exception occurs. When an exception occurs the results are rolled back.
     *
     * @param customer The customer who owns the order.
     * @return The newly created order entity.
     */
    @Override public Order createOrder(Customer customer)
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            transactionalRepository.begin();
            Order order = transactionalRepository.createOrder(customer);
            transactionalRepository.commit();
            return order;
        }
    }

    /**
     * Returns the order with the provided id.
     *
     * @param id The id of the order to return.
     * @return The order with the provided id, {@code null} when no such order exists.
     */
    @Override public Order getOrder(long id)
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            return transactionalRepository.getOrder(id);
        }
    }

    /**
     * Returns all the orders in the repository.
     *
     * @return The complete list of the orders in the repository.
     */
    @Override public List<Order> getOrders()
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            return transactionalRepository.getOrders();
        }
    }

    /**
     * Returns all the orders for the provided customers.
     *
     * @param customer The customer to return the order of.
     * @return The complete list of the orders belonging to the provided customer.
     */
    @Override public List<Order> getOrders(Customer customer)
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            return transactionalRepository.getOrders(customer);
        }
    }

    /**
     * Creates a new {@code OrderLine} and adds it to the provided {@code Order}.
     *
     * @param order    The order to add the newly created {@code OrderLine} to.
     * @param quantity The number of the provided item to include in the {@code OrderLine}.
     * @param itemType The {@code ItemType} to place in the {@code OrderLine}.
     * @return The newly created {@code OrderLine} entity.
     */
    @Override public OrderLine createOrderLine(Order order, long quantity, ItemType itemType)
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            transactionalRepository.begin();
            OrderLine orderLine = transactionalRepository.createOrderLine(order, quantity, itemType);
            transactionalRepository.commit();
            return orderLine;
        }
    }

    /**
     * Creates a new {@code ItemType}.
     *
     * @param name        The name of the {@code ItemType} to create.
     * @param description The description of the {@code ItemType} to create.
     * @param price       The price of the {@code ItemType} to create.
     * @return The newly created {@code ItemType} entity.
     */
    @Override public ItemType createItemType(String name, String description, long price)
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            transactionalRepository.begin();
            ItemType itemType = transactionalRepository.createItemType(name, description, price);
            transactionalRepository.commit();
            return itemType;
        }
    }

    /**
     * Returns the total price of the provided {@code Order}.
     *
     * @param order The order to find the total of.
     * @return The total price of the provided {@code Order.}
     */
    @Override public long getTotal(Order order)
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            return transactionalRepository.getTotal(order);
        }
    }

    /**
     * Returns the {@code ItemType} with the provided id.
     *
     * @param id The id of the {@code ItemType} to return.
     * @return The {@code ItemType} with the provided id, {@code null} when no such {@code ItemType} exists.
     */
    @Override public ItemType getItem(long id)
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            return transactionalRepository.getItem(id);
        }
    }

    /**
     * Returns all the items in the repository.
     *
     * @return The complete list of the items in the repository.
     */
    @Override public List<ItemType> getItems()
    {
        try (TransactionalRepository transactionalRepository = newTransactionalRepository()) {
            return transactionalRepository.getItems();
        }
    }
}
