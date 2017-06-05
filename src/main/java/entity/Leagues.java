package entity;
import javax.persistence.*;

@Entity @Table(name="league")
public class Leagues {
    
        @Id @Column(name="league_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	@Column(name="league_name_en")
	String name_en;
        @Column(name="league_name_th")
	String name_th;
        @Column(name="league_name_cn")
	String name_cn;
        @Column(name="league_img")
	String img;
        @Column(name="sport_id")
	Long sport_id;
        
        public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
        public Long getSportId() { return sport_id; }
	public void setSportId(Long sport_id) { this.sport_id = sport_id; }
	public String getNameEN() { return name_en; }
	public void setNameEN(String name_en) { this.name_en = name_en; }
        public String getNameTH() { return name_th; }
	public void setNameTH(String name_th) { this.name_th = name_th; }
        public String getNameCN() { return name_cn; }
	public void setNameCN(String name_cn) { this.name_cn = name_cn; }
        public String getImg() { return img; }
	public void setImg(String img) { this.img = img; }
        public Long getLeague() { return sport_id; }
	public void setLeague(Long sport_id) { this.sport_id = sport_id; }
        
        public Leagues() {}

}
