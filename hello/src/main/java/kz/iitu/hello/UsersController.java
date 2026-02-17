package kz.iitu.hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UsersController {

    private final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @GetMapping("/users")
    public String read(
            @RequestParam(name="id", required = false) Long id,
            Model model){
        User form = (id != null)? usersRepository.getById(id) : new User();

        model.addAttribute("editMode", id != null);
        model.addAttribute("form", form);
        model.addAttribute("users", usersRepository.findAll());
        return "users";
    }

    //create
    @PostMapping("/users")
    public String create(User user){
        usersRepository.save(user);
        return "redirect:/users";
    }

    //update
    @PutMapping("/users/{id}")
    public String update(@PathVariable(name="id") Long id, User userForm){
        User userModel = usersRepository.getById(id);

        userModel.setUserName(userForm.getUserName());

        usersRepository.save(userModel);
        return "redirect:/users";
    }

    //delete
    @DeleteMapping("/users/{id}")
    public String delete(@PathVariable(name="id") Long id){
        usersRepository.deleteById(id);
        return "redirect:/users";
    }
}
