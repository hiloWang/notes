package spittr.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import spittr.Spitter;
import spittr.data.SpitterRepository;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/spitter")
public class SpitterController {

    private SpitterRepository spitterRepository;

    @Autowired
    public SpitterController(SpitterRepository spitterRepository) {
        this.spitterRepository = spitterRepository;
    }

    @RequestMapping(value = "/register", method = GET)
    public String showRegistrationForm() {
        return "registerForm";
    }

    /*
    @Valid注解所标注的就是要检验的参数。processRegistration()方法所做的第一件事就是调用Errors.hasErrors()来检查是否有错误。
     */
    @RequestMapping(value = "/register", method = POST)
    public String processRegistration(@Valid/*开启字段验证*/ Spitter spitter, Errors errors/*验证结果放在errors中*/) {
        if (errors.hasErrors()) {
            return "registerForm";
        }

        spitterRepository.save(spitter);
        return "redirect:/spitter/" + spitter.getUsername();
    }

    @RequestMapping(value = "/{username}", method = GET)
    public String showSpitterProfile(@PathVariable String username, Model model) {
        Spitter spitter = spitterRepository.findByUsername(username);
        //当调用addAttribute()方法并且不指定key的时候，那么key会根据值的对象类型推断确定。比如
        // Spitter 推断为 spitter
        // List<Spittle> 推断为 spittleList。
        model.addAttribute(spitter);
        return "profile";
    }

}
