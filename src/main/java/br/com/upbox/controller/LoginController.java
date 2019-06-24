package br.com.upbox.controller;

import br.com.upbox.models.Usuario;
import br.com.upbox.repository.UsuarioRepository;
import br.com.upbox.retrofit.GoogleWebClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@Api(tags = "Login")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private GoogleWebClient client;

    @ApiOperation(value = "Login")
    @PostMapping
    public String login(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirect,
                        HttpSession session, HttpServletRequest request) throws IOException {
        String recaptcha = request.getParameter("g-recaptcha-response");
        boolean recaptchaValido = client.verifica(recaptcha);

        if(recaptchaValido) {

        }
        return "";
    }

}
