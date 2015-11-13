package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class docente extends Model {

	@Id
	public Long id;
	@Required
	public String nome;
	@Required
	public String grad;
	@Required
	public String ie_grad;
	public String pgrad;
	public String ie_pgrad;
	public String mest;
	public String ie_mest;
	public String dout;
	public String ie_dout;
	@Required
	public int ano_adm;
	
	@Column(columnDefinition="LONGBLOB")
	public byte[] imgsistema;
	
    public static Model.Finder<Long,docente> find = new Model.Finder(Long.class, docente.class);
}
