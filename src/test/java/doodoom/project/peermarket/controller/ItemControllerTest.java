package doodoom.project.peermarket.controller;

import doodoom.project.peermarket.configuration.SecurityConfig;
import doodoom.project.peermarket.dto.ItemRegisterInput;
import doodoom.project.peermarket.service.item.ItemService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Import(SecurityConfig.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void getRegister() throws Exception {
        mockMvc.perform(get("/item/register"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void register() throws Exception {
        //given
        Principal principal = Mockito.mock(Principal.class);
        given(principal.getName()).willReturn("test");
        MultipartFile file = new MockMultipartFile("file", new byte[]{});
        ItemRegisterInput input = ItemRegisterInput.builder()
                .name("test")
                .description("test")
                .price(10000L)
                .file(file)
                .build();

        //then
        mockMvc.perform(post("/item/register")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("name", input.getName())
                .param("description", input.getDescription())
                .param("price", String.valueOf(input.getPrice()))
                .param("file", String.valueOf(input.getFile()))
        ).andExpect(status().isOk());
    }

}