package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.io.IOUtils;

import models.User;
import models.docente;
import play.*;
import play.data.Form;
import play.mvc.*;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.*;
import play.mvc.Http.MultipartFormData;

public class Application extends Controller {

	// formulario para fazer login
	static Form<Login> formLogin = Form.form(Login.class);

	/*
	 * ACTIONS DO CONTROLLERS
	 */

	/*
	 * METODOS E CLASSES PARA LOGIN
	 */
	// -- Classe para autenticacao
	public static class Login {

		// dados necessarios para login
		public String email;
		public String password;

		// AQUI ESTA O TRUQUE, COMO VALIDAR UM FORMULARIO
		// NA HORA EM QUE O FORMULARIO É VALIDADO, EXECUTAMOS O LOGIN!
		// O metodo validade é invocado quando o framework play valida o
		// formulario de login
		// Observe que ele invoca o metodo authenticate do model User
		// e nao o authenticate que encontra-se neste controller
		//AUTHENTICATE
		public String validate() {
			if (User.authenticate(email, password) == null) {
				return "Usuario ou senha inválidos";
			}
			return null;
		}

	} // -- fim da classe login

	/**
	 * Login page.
	 */
	public static Result login() {
		return ok(login.render(formLogin));
	}

	/**
	 * Atua na verificacao do formulario do login
	 */
	public static Result authenticate() {

		// recebe formulario de login
		Form<Login> loginForm = formLogin.bindFromRequest();
		// valida o formulario e, ao mesmo tempo, se o usuario conseguiu se
		// autenticar
		if (loginForm.hasErrors()) {
			flash("error", "Login ou senha inválida. Tente novamente");
			// caso nao, envia novamente para o usuario
			return badRequest(login.render(loginForm));
		} else {
			// usuario autenticado
			// posso inserir dados na sessão

			session("connected", loginForm.get().email);

			// rediciono ele para a pagina inicial
			return redirect(routes.Admin.index());
		}
	}

	// ADD USUARIO
	static Form<User> formUser = Form.form(User.class);

	public static Result addCadastro() {
		Form<User> formulario = formUser.bindFromRequest();
		if (formulario.hasErrors()) {
			flash("error", "Preencha todos os campos");
			return badRequest(cadastro.render(formulario));
		} else {
			User user = formulario.get();
			user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
			Logger.info("Criando Usuario:" + user.email + " com senha: "
					+ user.password);
			user.save();
			flash("success", "Usuario adicionado com sucesso!");
			return login();
		}
	}

	public static Result cadastro() {
		return ok(cadastro.render(formUser));
	}

	/**
	 * Logout and clean the session.
	 */
	public static Result logout() {
		session().clear();
		flash("success", "Sessão finalizada com sucesso.");
		return redirect(routes.Admin.index());
	}

}
