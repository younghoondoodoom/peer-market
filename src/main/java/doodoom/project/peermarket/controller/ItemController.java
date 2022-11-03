package doodoom.project.peermarket.controller;

import doodoom.project.peermarket.dto.ItemRegisterInput;
import doodoom.project.peermarket.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/item/register")
    public String getRegister(Model model) {
        model.addAttribute("input", new ItemRegisterInput());
        return "item/register";
    }

    @PostMapping("/item/register")
    public String register(ItemRegisterInput input, Principal principal) {
        itemService.register(input, principal.getName());
        return "/index";
    }
}
