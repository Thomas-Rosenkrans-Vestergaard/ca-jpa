package com.tvestergaard.ca.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQuery(name = "ItemType.findAll", query = "SELECT i FROM ItemType i")
public class ItemType
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long   id;
    private String name;
    private String description;
    private long   price;

    public ItemType()
    {

    }

    public ItemType(String name, String description, long price)
    {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public long getPrice()
    {
        return this.price;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setPrice(long price)
    {
        this.price = price;
    }

    @Override public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemType itemType = (ItemType) o;
        return id == itemType.id &&
               price == itemType.price &&
               Objects.equals(name, itemType.name) &&
               Objects.equals(description, itemType.description);
    }

    @Override public int hashCode()
    {
        return Objects.hash(id, name, description, price);
    }

    @Override public String toString()
    {
        return "ItemType{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", price=" + price +
               '}';
    }
}
