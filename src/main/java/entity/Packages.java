package entity;

import javax.persistence.*;

@Entity @Table(name="api_package")
public class Packages {
    
        @Id @Column(name="api_package_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	@Column(name="api_package_name")
	String name;
        
        public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
        
        public Packages() {}
}
