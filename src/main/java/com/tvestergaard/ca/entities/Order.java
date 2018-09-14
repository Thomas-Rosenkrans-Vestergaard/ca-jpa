package com.tvestergaard.ca.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "order_")
@NamedQueries({
        @NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o"),
        @NamedQuery(name = "Order.findById", query = "SELECT o FROM Order o WHERE o.id = :id"),
        @NamedQuery(name = "Order.findByCustomer", query = "SELECT o FROM Order o WHERE o.customer = :customer")
})
public class Order
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderLine> lines = new ArrayList<>();

    public Order()
    {

    }

    public Order(Customer customer, List<OrderLine> lines)
    {
        this.customer = customer;
        this.lines = lines;
    }

    public Order(Customer customer)
    {
        this(customer, new ArrayList<>());
    }

    public long getId()
    {
        return this.id;
    }

    public Customer getCustomer()
    {
        return this.customer;
    }

    public List<OrderLine> getLines()
    {
        return this.lines;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public void setLines(List<OrderLine> lines)
    {
        this.lines = lines;
    }

    public void addLine(OrderLine line)
    {
        this.lines.add(line);
    }

    @Override public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                Objects.equals(customer, order.customer) &&
                Objects.equals(new ArrayList<>(lines), new ArrayList<>(order.lines));
    }

    @Override public int hashCode()
    {
        return Objects.hash(id, customer, new ArrayList<>(lines));
    }

    @Override public String toString()
    {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                '}';
    }
}
