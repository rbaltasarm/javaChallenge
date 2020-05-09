package com.javachallenge.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.Assert;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	
        
	protected User() {

	}

	public User(String name) {
            Assert.hasLength(name, "User name can not be empty");
            Assert.isTrue(name.length()<20, "User name lenght has to be lower than 20");
            Assert.isTrue(name.split(" ").length==1, "User name lenght has to be an unique word");
            this.name = name;
		
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

        //maybe later we need to compare using ignore case names    
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
