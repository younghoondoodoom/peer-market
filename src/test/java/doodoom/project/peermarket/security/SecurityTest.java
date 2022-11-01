package doodoom.project.peermarket.security;

import doodoom.project.peermarket.configuration.SecurityConfig;
import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.service.member.MemberService;
import doodoom.project.peermarket.type.MemberStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Import(SecurityConfig.class)
public class SecurityTest {

    @MockBean
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    public void loginSuccessTest() throws Exception {
        //given
        Member member = members.get(0);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        given(memberService.loadUserByUsername(anyString()))
                .willReturn(
                        User.builder()
                                .username(member.getEmail())
                                .password(passwordEncoder.encode(member.getPassword()))
                                .authorities(grantedAuthorities)
                                .build());
        //when
        //then
        mockMvc.perform(formLogin("/member/login")
                        .user(member.getEmail())
                        .password(member.getPassword()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated());
    }

    @Test
    public void loginFailureTest() throws Exception {
        //given
        Member member = members.get(0);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        given(memberService.loadUserByUsername(anyString()))
                .willReturn(
                        User.builder()
                                .username(member.getEmail())
                                .password(passwordEncoder.encode(member.getPassword()))
                                .authorities(grantedAuthorities)
                                .build());
        //when
        //then
        mockMvc.perform(formLogin("/member/login")
                        .user(anyString())
                        .password(anyString()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/member/login?error=true"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = "test", roles = "USER")
    public void logoutTest() throws Exception {
        //then
        mockMvc.perform(logout())
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }
}
