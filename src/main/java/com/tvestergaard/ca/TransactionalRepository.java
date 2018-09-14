package com.tvestergaard.ca;

import com.tvestergaard.ca.entities.Customer;
import com.tvestergaard.ca.entities.ItemType;
import com.tvestergaard.ca.entities.Order;
import com.tvestergaard.ca.entities.OrderLine;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.function.Consumer;

/**
 * Repository implementation that provides support for transactions across operations.
 */
public class TransactionalRepository implements Repository, AutoCloseable
{

    /**
     * The {@code EntityManager} that the object performs operations on.
     */
    private final EntityManager entityManager;

    /**
     * The transaction that is currently active.
     */
    private final EntityTransaction transaction;

    /**
     * Creates a new {@link TransactionalRepository}. Begins the transaction.
     *
     * @param entityManager The {@code EntityManager} that the object performs operations on.
     */
    public TransactionalRepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
        this.transaction = entityManager.getTransaction();
    }

    /**
     * Creates a new {@link TransactionalRepository}.
     *
     * @param entityManager        The {@code EntityManager} that the object performs operations on.
     * @param transactionOperation Some operation that is executed on the transaction.
     */
    public TransactionalRepository(EntityManager entityManager, Consumer<EntityTransaction> transactionOperation)
    {
        this.entityManager = entityManager;
        this.transaction = entityManager.getTransaction();
        transactionOperation.accept(transaction);
    }

    /**
     * Returns the currently active transaction.
     *
     * @return The currently active transaction.
     */
    public EntityTransaction getTransaction()
    {
        return this.transaction;
    }

    /**
     * Performs an operation on the currently active transaction.
     *
     * @param operation The operation to perform on the currently active transaction.
     */
    public void onTransaction(Consumer<EntityTransaction> operation)
    {
        operation.accept(transaction);
    }

    /**
     * Begins the currently active transaction.
     */
    public void begin()
    {
        transaction.begin();
    }

    /**
     * Commits the currently active transaction.
     */
    public void commit()
    {
        transaction.commit();
    }

    /**
     * Rolls back changes made to the currently active transaction.
     */
    public void rollback()
    {
        transaction.rollback();
    }

    /**
     * Closes the {@code EntityManager} used in the repository.
     */
    @Override public void close()
    {
        if (transaction.isActive())
            transaction.rollback();

        entityManager.close();
    }

    /**
     * Creates a new customer with the provided name and email.
     *
     * @param name  The name of the customer to create.
     * @param email The email of the customer to create.
     * @return The newly created customer entity.
     */
    @Override public Customer createCustomer(String name, String email)
    {
        Customer customer = new Customer(name, email);
        entityManager.persist(customer);
        return customer;
    }

    /**
     * Finds the customer with the provided id.
     *
     * @param id The id of the customer to find.
     * @return The customer with the provided id, {@code null} when no such customer exists.
     */
    @Override public Customer findCustomer(long id)
    {
        return entityManager.find(Customer.class, id);
    }

    /**
     * Returns a complete list of customers.
     *
     * @return The complete list of customers.
     */
    @Override public List<Customer> getCustomers()
    {
        return entityManager.createNamedQuery("Customer.findAll", Customer.class).getResultList();
    }

    /**
     * Creates a new order from the provided arguments.
     *
     * @param customer The customer who owns the order.
     * @return The newly created order entity.
     */
    @Override public Order createOrder(Customer customer)
    {
        Order order = new Order(customer);
        entityManager.persist(order);
        return order;
    }

    /**
     * Returns the order with the provided id.
     *
     * @param id The id of the order to return.
     * @return The order with the provided id, {@code null} when no such order exists.
     */
    @Override public Order getOrder(long id)
    {
        try {
            TypedQuery<Order> query = entityManager.createNamedQuery("Order.findById", Order.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Returns all the orders in the repository.
     *
     * @return The complete list of the orders in the repository.
     */
    @Override public List<Order> getOrders()
    {
        return entityManager.createNamedQuery("Order.findAll", Order.class).getResultList();
    }

    /**
     * Returns all the orders for the provided customers.
     *
     * @param customer The customer to return the order of.
     * @return The complete list of the orders belonging to the provided customer.
     */
    @Override public List<Order> getOrders(Customer customer)
    {
        return entityManager
                .createNamedQuery("Order.findByCustomer", Order.class)
                .setParameter("customer", customer)
                .getResultList();
    }

    /**
     * Creates a new {@code OrderLine} and adds it to the provided {@code Order}.
     *
     * @param order    The order to add the newly created {@code OrderLine} to.
     * @param quantity The number of the provided item to include in the {@code OrderLine}.
     * @param itemType The {@code ItemType} to place in the {@code OrderLine}.
     * @return The newly created {@code OrderLine} entity.
     */
    public OrderLine createOrderLine(Order order, long quantity, ItemType itemType)
    {
        OrderLine line = new OrderLine(quantity, itemType);
        entityManager.persist(line);
        order.addLine(line);
        entityManager.merge(order);
        return line;
    }

    /**
     * Creates a new {@code OrderLine} and adds it to the provided {@code OrderLine}.
     *
     * @param name        The orderLine to add the newly created {@code ItemType} to.
     * @param description
     * @param price
     * @return The newly created {@code ItemType} entity.
     */
    @Override public ItemType createItemType(String name, String description, long price)
    {
        ItemType type = new ItemType(name, description, price);
        entityManager.persist(type);
        return type;
    }

    /**
     * Returns the total price of the provided {@code Order}.
     *
     * @param order The order to find the total of.
     * @return The total price of the provided {@code Order.}
     */
    @Override public long getTotal(Order order)
    {
        return order.getLines()
                .stream()
                .map(l -> l.getQuantity() * l.getItem().getPrice())
                .reduce(0l, Math::addExact);
    }

    /**
     * Returns the {@code ItemType} with the provided id.
     *
     * @param id The id of the {@code ItemType} to return.
     * @return The {@code ItemType} with the provided id, {@code null} when no such {@code ItemType} exists.
     */
    @Override public ItemType getItem(long id)
    {
        return entityManager.find(ItemType.class, id);
    }

    /**
     * Returns all the items in the repository.
     *
     * @return The complete list of the items in the repository.
     */
    @Override public List<ItemType> getItems()
    {
        return entityManager.createNamedQuery("ItemType.findAll").getResultList();
    }
}
