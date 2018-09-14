package com.tvestergaard.ca;

import com.tvestergaard.ca.entities.Customer;
import com.tvestergaard.ca.entities.ItemType;
import com.tvestergaard.ca.entities.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class TransactionalRepositoryTest
{

    private static final EntityManagerFactory    emf = Persistence.createEntityManagerFactory("jpau-test");
    private              TransactionalRepository instance;

    @Before
    public void setUp() throws Exception
    {
        instance = new TransactionalRepository(emf.createEntityManager());
    }

    @After
    public void tearDown() throws Exception
    {
        instance.close();
    }

    @Test
    public void getTransaction()
    {
        assertNotNull(instance.getTransaction());
        assertFalse(instance.getTransaction().isActive());
        instance.begin();
        assertTrue(instance.getTransaction().isActive());
    }

    @Test
    public void onTransaction()
    {

    }

    @Test
    public void begin()
    {
        EntityTransaction transaction = instance.getTransaction();
        assertFalse(transaction.isActive());
        instance.begin();
        assertTrue(transaction.isActive());
    }

    @Test
    public void commit()
    {

    }

    @Test
    public void rollback()
    {
    }

    @Test
    public void close()
    {
    }

    @Test
    public void createCustomer()
    {
        instance.begin();
        Customer customer = instance.createCustomer("Name", "Email");
        Customer find     = instance.findCustomer(customer.getId());
        assertEquals(customer, find);
    }

    @Test
    public void findCustomer()
    {
        Customer customer = instance.findCustomer(1);
        assertEquals(1, customer.getId());
        assertEquals("Thomas Vestergaard", customer.getName());
        assertEquals("tvestergaard@hotmail.com", customer.getEmail());
    }

    @Test
    public void getCustomers()
    {
        List<Customer> customers = instance.getCustomers();

        assertEquals(4, customers.size());
        assertEquals("Thomas Vestergaard", customers.get(0).getName());
        assertEquals("Sanne Vestergaard", customers.get(1).getName());
        assertEquals("Kasper Vestergaard", customers.get(2).getName());
        assertEquals("Thorbjørn Vestergaard", customers.get(3).getName());
    }

    @Test
    public void createOrder()
    {
        instance.begin();
        Customer customer = instance.createCustomer("Purchaser", "email@email.com");
        Order    order    = instance.createOrder(customer);
        Order    find     = instance.getOrder(order.getId());
        assertEquals(order, find);
    }

    @Test
    public void getOrder()
    {
        instance.begin();
        Customer customer = instance.createCustomer("Purchaser", "email@email.com");
        Order    order    = instance.createOrder(customer);
        Order    find     = instance.getOrder(order.getId());
        assertEquals(order, find);
    }

    @Test
    public void getOrderReturnsNull()
    {
        assertNull(instance.getOrder(34546l));
    }

    @Test
    public void getOrders()
    {
        instance.begin();
        List<Order> created  = new ArrayList<>();
        Customer    customer = instance.createCustomer("Purchaser", "email@email.com");
        for (int x = 0; x < 4; x++)
            created.add(instance.createOrder(customer));

        assertEquals(created, instance.getOrders());
    }

    @Test
    public void getOrdersFromCustomer()
    {
        instance.begin();
        List<Order> created  = new ArrayList<>();
        Customer    customer = instance.createCustomer("Purchaser", "email@email.com");
        for (int x = 0; x < 4; x++)
            created.add(instance.createOrder(customer));

        // Add another order from a different customer
        instance.createOrder(instance.createCustomer("Another", "Customer"));
        List<Order> customerOrders = instance.getOrders(customer);
        assertEquals(4, customerOrders.size());
        assertEquals(created, customerOrders);
    }

    @Test
    public void createOrderLine()
    {
        instance.begin();

        Customer       customer = instance.createCustomer("Orderer", "orderer@order.com");
        Order          order    = instance.createOrder(customer);
        List<ItemType> items    = instance.getItems();

        for (int x = 0; x < 4; x++)
            instance.createOrderLine(order, x + 1, items.get(x));

        assertEquals(order, instance.getOrder(order.getId()));
    }

    @Test
    public void createItemType()
    {
        instance.begin();

        String name        = "ItemName";
        String description = "ItemDescription";
        long   price       = 98234l;

        ItemType itemType = instance.createItemType(name, description, price);
        assertEquals(itemType, instance.getItem(itemType.getId()));
    }

    @Test
    public void getTotal()
    {
        instance.begin();

        Customer       customer = instance.findCustomer(1);
        Order          order    = instance.createOrder(customer);
        List<ItemType> items    = instance.getItems();

        long expected = 0l;
        for (ItemType item : items) {
            // item.getId() used as quantity
            instance.createOrderLine(order, item.getId(), item);
            expected += item.getId() * item.getPrice();
        }

        assertEquals(expected, instance.getTotal(order));
    }
}