package com.github.fddqdcb.parser.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 *
 * @author
 */
@Entity
public class Person implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private LocalDate birthdate;

    private Double decimalNumber;
    private Integer integerNumber;


    public Person()
    {
    }


    public Person(Long id, String name, LocalDate birthdate, Double decimalNumber, Integer integerNumber)
    {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.decimalNumber = decimalNumber;
        this.integerNumber = integerNumber;
    }


    @Override
    public String toString()
    {
        return "Person{" + "id=" + id + ", name=" + name + ", birthdate=" + birthdate + ", decimalNumber=" + decimalNumber + ", integerNumber=" + integerNumber + '}';
    }


    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }


    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        final Person other = (Person) obj;
        if(!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }


    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public LocalDate getBirthdate()
    {
        return birthdate;
    }


    public void setBirthdate(LocalDate birthdate)
    {
        this.birthdate = birthdate;
    }


    public Double getDecimalNumber()
    {
        return decimalNumber;
    }


    public void setDecimalNumber(Double decimalNumber)
    {
        this.decimalNumber = decimalNumber;
    }


    public Integer getIntegerNumber()
    {
        return integerNumber;
    }


    public void setIntegerNumber(Integer integerNumber)
    {
        this.integerNumber = integerNumber;
    }

}
