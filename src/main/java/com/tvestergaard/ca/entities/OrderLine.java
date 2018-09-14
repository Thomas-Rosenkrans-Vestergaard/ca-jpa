package com.tvestergaard.ca.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLine
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long quantity;

    @ManyToOne
    @JoinColumn(name = "item_fk")
    private ItemType item;

    @ManyToOne
    private Order order;

    public OrderLine()
    {

    }

    public OrderLine(long quantity, ItemType item)
    {
        this.quantity = quantity;
        this.item = item;
    }

    public long getId()
    {
        return this.id;
    }

    public long getQuantity()
    {
        return this.quantity;
    }

    public ItemType getItem()
    {
        return this.item;
    }

    public Order getOrder()
    {
        return this.order;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }

    public void setItem(ItemType item)
    {
        this.item = item;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

    @Override public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine line = (OrderLine) o;
        return id == line.id &&
               quantity == line.quantity &&
               Objects.equals(item, line.item) &&
               Objects.equals(order, line.order);
    }

    @Override public int hashCode()
    {
        return Objects.hash(id, quantity, item, order);
    }

    @Override public String toString()
    {
        return "OrderLine{" +
               "id=" + id +
               ", quantity=" + quantity +
               ", item=" + item +
               ", order=" + order +
               '}';
    }
}
