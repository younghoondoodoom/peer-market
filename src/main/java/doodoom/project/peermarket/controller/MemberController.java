package doodoom.project.peermarket.controller;

import doodoom.project.peermarket.dto.MemberRegisterInput;
import doodoom.project.peermarket.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/register")
    public String getRegister(Model model) {
        model.addAttribute("input", new MemberRegisterInput());
        return "member/register";
    }

    @PostMapping("/member/register")
    public String register(@Valid MemberRegisterInput input) {
        memberService.register(input);
        return "redirect:/";
    }

    @RequestMapping("/member/login")
    public String getLogin() {
        return "member/login";
    }
}
