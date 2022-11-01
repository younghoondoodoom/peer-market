package doodoom.project.peermarket.controller;

import doodoom.project.peermarket.configuration.SecurityConfig;
import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.MemberRegisterInput;
import doodoom.project.peermarket.service.member.MemberService;
import doodoom.project.peermarket.type.MemberStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class)
class MemberControllerTest {

    @MockBean
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;
    private static final List<Member> members = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        members.add(Member.builder()
                .email("test@test.com")
                .password("test123!")
                .nickname("test")
                .status(MemberStatus.ACTIVE)
                .build());
    }

    @Test
    public void registerTest() throws Exception {
        //given
        Member member = members.get(0);
        MemberRegisterInput input =
                new MemberRegisterInput(member.getEmail(), member.getPassword(),
                        member.getPassword(), member.getNickname());
        given(memberService.register(input)).willReturn(true);

        //then
        mockMvc.perform(post("/member/register")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("email", input.getEmail())
                .param("password", input.getPassword())
                .param("passwordCheck", input.getPasswordCheck())
                .param("nickname", input.getNickname())
        ).andExpect(status().isFound());
    }

    @Test
    public void getRegister() throws Exception {
        mockMvc.perform(get("/member/register"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getLogin() throws Exception {
        mockMvc.perform(get("/member/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}