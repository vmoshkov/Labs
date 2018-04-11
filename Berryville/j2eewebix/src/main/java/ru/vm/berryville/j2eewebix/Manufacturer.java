package ru.vm.berryville.j2eewebix;
// Generated 30.06.2016 14:46:07 by Hibernate Tools 4.3.1.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Manufacturer generated by hbm2java
 */
@Entity
@Table(name = "manufacturer", catalog = "UNO", uniqueConstraints = @UniqueConstraint(columnNames = "name") )
public class Manufacturer implements java.io.Serializable {

	private Integer id;
	private String name;
	private String country;
	private Set<Car> cars = new HashSet<Car>(0);

	public Manufacturer() {
	}

	public Manufacturer(String name, String country) {
		this.name = name;
		this.country = country;
	}

	public Manufacturer(String name, String country, Set<Car> cars) {
		this.name = name;
		this.country = country;
		this.cars = cars;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", unique = true, nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "country", nullable = false)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "manufacturer")
	public Set<Car> getCars() {
		return this.cars;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}

@Override
	public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof Manufacturer) ) return false;

        final Manufacturer mnftr = (Manufacturer) other;

        if ( !mnftr.getId().equals(getId()) ) return false;
        if ( !mnftr.getName().equals(getName())) return false;
        
        return true;
    }

	@Override
    public int hashCode() {
        int result;
        result = getName().hashCode();
        result = 29 * result + getId();
        return result;
    }


 @Override
    public String toString()
    {
    	return "Manufacturer[" + getName() + "; " + getCountry() + "] ";
     }

}
