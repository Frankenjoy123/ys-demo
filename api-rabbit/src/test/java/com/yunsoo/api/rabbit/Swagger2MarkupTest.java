package com.yunsoo.api.rabbit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler;

/**
 * Created by:   Lijian
 * Created on:   2015/9/15
 * Descriptions:
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class Swagger2MarkupTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void convertSwaggerToAsciiDoc() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v2/api-docs")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(Swagger2MarkupResultHandler.outputDirectory("build/docs/asciidoc/generated").build())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
//
//    @Test
//    public void convertSwaggerToMarkdown() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/v2/api-docs")
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(Swagger2MarkupResultHandler.outputDirectory("${buildDir}/docs/markdown/generated")
//                        .withMarkupLanguage(MarkupLanguage.MARKDOWN).build())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }

}
