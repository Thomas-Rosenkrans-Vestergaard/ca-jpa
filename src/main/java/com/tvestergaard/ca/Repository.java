package com.tvestergaard.ca;

import com.tvestergaard.ca.entities.Customer;
import com.tvestergaard.ca.entities.ItemType;
import com.tvestergaard.ca.entities.Order;
import com.tvestergaard.ca.entities.OrderLine;

import java.util.List;

public interface Repository
{

    /**
     * Creates a new customer with the provided name and email.
     *
     * @param name  The name of the customer to create.
     * @param email The email of the customer to create.
     * @return The newly created customer entity.
     */
    Customer createCustomer(String name, String email);

    /**
     * Finds the customer with the provided id.
     *
     * @param id The id of the customer to find.
     * @return The customer with the provided id, {@code null} when no such customer exists.
     */
    Customer findCustomer(long id);

    /**
     * Returns a complete list of customers.
     *
     * @return The complete list of customers.
     */
    List<Customer> getCustomers();

    /**
     * Creates a new order from the provided arguments.
     *
     * @param customer The customer who owns the order.
     * @return The newly created order entity.
     */
    Order createOrder(Customer customer);

    /**
     * Returns the order with the provided id.
     *
     * @param id The id of the order to return.
     * @return The order with the provided id, {@code null} when no such order exists.
     */
    Order getOrder(long id);

    /**
     * Returns all the orders in the repository.
     *
     * @return The complete list of the orders in the repository.
     */
    List<Order> getOrders();

    /**
     * Returns all the orders for the provided customers.
     *
     * @param customer The customer to return the order of.
     * @return The complete list of the orders belonging to the provided customer.
     */
    List<Order> getOrders(Customer customer);

    /**
     * Creates a new {@code OrderLine} and adds it to the provided {@code Order}.
     *
     * @param order    The order to add the newly created {@code OrderLine} to.
     * @param quantity The number of the provided item to include in the {@code OrderLine}.
     * @param itemType The {@code ItemType} to place in the {@code OrderLine}.
     * @return The newly created {@code OrderLine} entity.
     */
    OrderLine createOrderLine(Order order, long quantity, ItemType itemType);

    /**
     * Creates a new {@code ItemType}.
     *
     * @param name        The name of the {@code ItemType} to create.
     * @param description The description of the {@code ItemType} to create.
     * @param price       The price of the {@code ItemType} to create.
     * @return The newly created {@code ItemType} entity.
     */
    ItemType createItemType(String name, String description, long price);

    /**
     * Returns the total price of the provided {@code Order}.
     *
     * @param order The order to find the total of.
     * @return The total price of the provided {@code Order.}
     */
    long getTotal(Order order);

    /**
     * Returns the {@code ItemType} with the provided id.
     *
     * @param id The id of the {@code ItemType} to return.
     * @return The {@code ItemType} with the provided id, {@code null} when no such {@code ItemType} exists.
     */
    ItemType getItem(long id);

    /**
     * Returns all the items in the repository.
     *
     * @return The complete list of the items in the repository.
     */
    List<ItemType> getItems();
}
