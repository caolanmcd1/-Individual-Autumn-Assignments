package registerationlogin.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import registerationlogin.dto.UserDto;
import registerationlogin.entity.User;
import registerationlogin.service.UserService;

import java.util.List;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService){
        this.userService=userService;
    }

    //Gets home page
    @GetMapping("/index")
    public String home(){
        return "index";

    }

    //Gets user registeration form request.
    @GetMapping("/register")
    public String showRegistrationForm(Model model){

        UserDto userDto = new UserDto();
        model.addAttribute("user",userDto); //model.addAttribute stores data from form
        return "register";

    }
    //Handles user registration form submit request.
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result,Model model){ //@ModelAttribute is used to extract model object which is a form data.

        //Checks if entered email already exists or not.
        User existingUser = userService.findByEmail(userDto.getEmail());

        if(existingUser!=null && existingUser.getEmail()!=null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email",null,"There is already an account existed with this email.");
        }

        if(result.hasErrors()){
            model.addAttribute("user",userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    //Gets list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users",users);
        return "users";
    }

    //Gets user to login page
    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
