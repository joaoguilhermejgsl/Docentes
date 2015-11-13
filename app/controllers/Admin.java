package controllers;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import models.docente;
import org.apache.commons.io.IOUtils;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

import java.util.ArrayList;
import javax.imageio.stream.FileImageInputStream;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.*;
import play.mvc.Http.MultipartFormData;


//a anotation a seguir informa que todas as actions deste controllers
//devem ser feitas apenas por usuarios autenticados
/**
 * Esta classe é um controller no qual todas as suas actions sao 
 * permitidas apenas para usuarios autenticados.
 * 
 * Isto é feito com uso da anotation abaixo
 */
@Security.Authenticated(Secured.class)
public class Admin extends Controller {
	
    //CONTROLLERS PRECEDIDOS DE LOGIN
  
	public static Result index() {
		return ok(index.render("Docentes FMU"));
	}

	// ADD DOCENTE
	static Form<docente> formdocente = Form.form(docente.class);

	public static Result adddocente() {

		return ok(adddocente.render(formdocente));
	}

	public static Result addNovodocente() throws IOException,
			FileNotFoundException {
		Form<docente> novoform = formdocente.bindFromRequest();
		if (novoform.hasErrors()) {
			flash("error", "Preencha todos os campos");
			return badRequest(adddocente.render(novoform));
		} else {
		docente novodocente = novoform.get();
		// docente newSystem = docente.find.byId(novodocente.id);
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("picture");
		if (picture==null) {
			flash("error", "Imagem obrigatória");
			return badRequest(adddocente.render(novoform));
		} else {
		novodocente.imgsistema = IOUtils.toByteArray(new FileInputStream(
				picture.getFile()));
		novodocente.save();
		flash("success", "Docente adicionado com sucesso.");
		return ok(adddocente.render(formdocente));
	}
	}
	}

	// UPLOAD IMG
	// Vamos supor que voce deseja obter o arquivo. Ele é uma imagem, neste
	// caso. Entao
	public static Result getImage(Long idDocente) {
		docente professor = docente.find.byId(idDocente);

		// voce deve mudar o MIMETYPE para PDF, JPEG, DOC, etc...
		response().setContentType("image/jpeg");

		return ok(professor.imgsistema);
	}
	
	//DEL DOCENTE
	public static Result delDocente(Long id) {
		docente deletedoc = docente.find.byId(id);
		deletedoc.delete();
		flash ("success", "Docente removido com sucesso");
		return ok(visudocente.render(null));
	}
	
	//EDIT DOCENTE
	public static Result editDocente(Long id) {
		docente editdoc = docente.find.byId(id);
		editdoc.delete();
		return redirect(routes.Admin.adddocente());
	}

	// Visualizar Docente
	public static Result visudocente() {
		return ok(visudocente.render(null));
	}

	// Pesquisando Docente	
	public static Result listaDocente() {
		String inome =  Form.form().bindFromRequest().get("nome");
		//System.out.println("PESQUISANDO POR " + inome);
		inome = "%"+inome+"%";
		List<docente> pesqdoc =  docente.find.where()
									.ilike("nome", inome)
									.findList();
		//*for(docente doc : pesqdoc) {
	//		System.out.println("DOCENTE: " + doc.nome);
		//}
	
		return ok(visudocente.render(pesqdoc));
	}

	// Sobre
	public static Result sobre() {
		return ok(sobre.render());
	}
    
}
