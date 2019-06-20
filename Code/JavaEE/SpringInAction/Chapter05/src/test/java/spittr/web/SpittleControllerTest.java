package spittr.web;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import spittr.Spittle;
import spittr.data.SpittleRepository;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class SpittleControllerTest {

    @Test
    public void houldShowRecentSpittles() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(20);

        SpittleRepository mockRepository = mock(SpittleRepository.class);

        when(mockRepository.findSpittles(Long.MAX_VALUE, 20)).thenReturn(expectedSpittles);


        SpittleController controller = new SpittleController(mockRepository);
        /*
        在MockMvc构造器上调用setSingleView()，mock框架就不用解析控制器中的视图名了。在很多场景中，其实没有必要这样做。
        但是对于这个控制器方法，视图名与请求路径是非常相似的，这样按照默认的视图解析规则时，MockMvc就会发生失败，
        因为无法区分视图路径和控制器的路径。在这个测试中，构建InternalResourceView时所设置的实际路径是无关紧要的，
        但我们将其设置为与InternalResourceViewResolver配置一致。
         */
        MockMvc mockMvc = standaloneSetup(controller)
                .setSingleView(new InternalResourceView("/WEB-INF/views/spittles.jsp"))
                .build();

        //这个测试对“/spittles”发起GET请求，然后断言视图的名称为spittles并且模型中包含名为spittleList的属性，在spittleList中包含预期的内容。
        mockMvc.perform(get("/spittles"))
                .andExpect(view().name("spittles"))
                .andExpect(model().attributeExists("spittleList"))
                .andExpect(model().attribute("spittleList", hasItems(expectedSpittles.toArray())));
    }

    @Test
    public void shouldShowPagedSpittles() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(50);
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findSpittles(238900, 50))
                .thenReturn(expectedSpittles);

        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller)
                .setSingleView(new InternalResourceView("/WEB-INF/views/spittles.jsp"))
                .build();

        mockMvc.perform(get("/spittles?max=238900&count=50"))
                .andExpect(view().name("spittles"))
                .andExpect(model().attributeExists("spittleList"))
                .andExpect(model().attribute("spittleList",
                        hasItems(expectedSpittles.toArray())));
    }

    @Test
    public void testSpittle() throws Exception {
        Spittle expectedSpittle = new Spittle("Hello", new Date());

        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findOne(12345)).thenReturn(expectedSpittle);

        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/spittles/12345"))
                .andExpect(view().name("spittle"))
                .andExpect(model().attributeExists("spittle"))
                .andExpect(model().attribute("spittle", expectedSpittle));
    }

    @Test
    public void saveSpittle() throws Exception {
        SpittleRepository mockRepository = mock(SpittleRepository.class);

        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/spittles")
                .param("message", "Hello World") // this works, but isn't really testing what really happens
                .param("longitude", "-81.5811668")
                .param("latitude", "28.4159649"))
                .andExpect(redirectedUrl("/spittles"));

        verify(mockRepository, atLeastOnce()).save(new Spittle(null, "Hello World", new Date(), -81.5811668, 28.4159649));
    }

    private List<Spittle> createSpittleList(int count) {
        List<Spittle> spittles = new ArrayList<Spittle>();
        for (int i = 0; i < count; i++) {
            spittles.add(new Spittle("Spittle " + i, new Date()));
        }
        return spittles;
    }
}
