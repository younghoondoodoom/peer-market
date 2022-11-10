package doodoom.project.peermarket.controller;

import doodoom.project.peermarket.dto.ItemDto;
import doodoom.project.peermarket.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String main(
        @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable,
        Model model) {
        Page<ItemDto> items = itemService.list(pageable);
        model.addAttribute("items", items);
        return "index";
    }
}
