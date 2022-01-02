package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {

    @RequestMapping("/perform_logout")
    public void logout() {
        System.out.println("LOGOUT");
    }
}
