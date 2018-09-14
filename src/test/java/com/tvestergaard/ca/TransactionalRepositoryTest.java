package com.tvestergaard.ca;

import com.tvestergaard.ca.entities.Customer;
import com.tvestergaard.ca.entities.ItemType;
import com.tvestergaard.ca.entities.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tvestergaard.ca.TransactionStrategy.COMMIT;
import static com.tvestergaard.ca.TransactionStrategy.ROLLBACK;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class TransactionalRepositoryTest
{

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpau-test");
    private TransactionalRepository instance;

    @Before
    public void setUp() throws Exception
    {
        instance = new TransactionalRepository(emf);
    }

    @After
    public void tearDown() throws Exception
    {
        instance.close();
    }

    @Test
    public void begin()
    {
        assertTrue(instance.isActive());
        instance.rollback();
        assertFalse(instance.isActive());
    }

    @Test
    public void commit()
    {
        Customer customer = instance.createCustomer("Name", "Email");
        instance.commit();
        TransactionalRepository other = new TransactionalRepository(emf);
        assertEquals(customer, other.findCustomer(customer.getId()));
    }

    @Test
    public void rollback()
    {
        TransactionalRepository other    = new TransactionalRepository(emf);
        Customer                customer = instance.createCustomer("Name", "Email");
        assertEquals(customer, instance.findCustomer(customer.getId()));
        instance.rollback();
        assertNull(instance.findCustomer(customer.getId()));
        assertNull(other.findCustomer(customer.getId()));
    }

    @Test
    public void onCloseCommit() throws Exception
    {
        instance.onClose(COMMIT);
        Customer customer = instance.createCustomer("Name", "Email");
        instance.close();
        TransactionalRepository other = new TransactionalRepository(emf);
        assertEquals(customer, other.findCustomer(customer.getId()));
    }

    @Test
    public void onCloseRollback() throws Exception
    {
        instance.onClose(ROLLBACK);
        TransactionalRepository other    = new TransactionalRepository(emf);
        Customer                customer = instance.createCustomer("Name", "Email");
        assertEquals(customer, instance.findCustomer(customer.getId()));
        instance.close();
        assertNull(other.findCustomer(customer.getId()));
    }

    @Test
    public void isActive() throws Exception
    {
        assertTrue(instance.isActive());
        instance.rollback();
        assertFalse(instance.isActive());
        instance.begin();
        assertTrue(instance.isActive());
    }

    @Test
    public void createCustomer()
    {
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

        assertEquals("Thomas Vestergaard", customers.get(0).getName());
        assertEquals("Sanne Vestergaard", customers.get(1).getName());
        assertEquals("Kasper Vestergaard", customers.get(2).getName());
        assertEquals("Thorbjørn Vestergaard", customers.get(3).getName());
    }

    @Test
    public void createOrder()
    {
        Customer customer = instance.createCustomer("Purchaser", "email@email.com");
        Order    order    = instance.createOrder(customer);
        Order    find     = instance.getOrder(order.getId());
        assertEquals(order, find);
    }

    @Test
    public void getOrder()
    {
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
        List<Order> created  = new ArrayList<>();
        Customer    customer = instance.createCustomer("Purchaser", "email@email.com");
        for (int x = 0; x < 4; x++)
            created.add(instance.createOrder(customer));


        Set<Order> orders = new HashSet<>(instance.getOrders());
        for (Order order : created)
            assertTrue(orders.contains(order));
    }

    @Test
    public void getOrdersFromCustomer()
    {
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
        String name        = "ItemName";
        String description = "ItemDescription";
        long   price       = 98234l;

        ItemType itemType = instance.createItemType(name, description, price);
        assertEquals(itemType, instance.getItem(itemType.getId()));
    }

    @Test
    public void getTotal()
    {
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