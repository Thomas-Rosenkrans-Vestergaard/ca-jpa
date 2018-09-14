package com.tvestergaard.ca.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
public class Customer
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    public Customer()
    {
    }

    public Customer(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    public long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getEmail()
    {
        return this.email;
    }

    public List<Order> getOrders()
    {
        return this.orders;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setOrders(List<Order> orders)
    {
        this.orders = orders;
    }

    public void addOrder(Order order)
    {
        this.orders.add(order);
    }

    @Override public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getId() == customer.getId() &&
                Objects.equals(getName(), customer.getName()) &&
                Objects.equals(getEmail(), customer.getEmail()) &&
                Objects.equals(new ArrayList<>(getOrders()), new ArrayList<>(customer.getOrders()));
    }

    @Override public int hashCode()
    {
        return Objects.hash(getId(), getName(), getEmail(), getOrders());
    }

    @Override public String toString()
    {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
