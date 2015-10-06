import ca.carleton.blackjack.Launcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for spring greeting controller.
 *
 * Created by Mike on 10/6/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Launcher.class)
@WebAppConfiguration
public class GreetingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mock;

    @Before
    public void setUp() throws Exception {
        this.mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void canLoadPage() throws Exception {
        this.mock.perform(get("/")).andExpect(status().isOk());
    }
}
