package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Juliano Alcione de Souza
 * */

@Getter
@Setter
public class UserForm {
  @NotNull(message = "O fullname não pode ser nulo.")
  @Size(min = 5, max =  30, message = "O fullname deve conter entre 5 a 10 caracteres.")
  private String fullname;

  @NotNull(message = "O email não pode ser nulo.")
  @Email(message = "Email deve ser válido")
  private String email;

  @NotNull(message = "O password não pode ser nulo.")
  @Size(min = 6, message = "O password deve conter no mínimo 6 caracteres.")
  private String password;

  private UserRole role;

  public User convertToEntity(){
    return new User(null, fullname, email, password, role);
  }
}
