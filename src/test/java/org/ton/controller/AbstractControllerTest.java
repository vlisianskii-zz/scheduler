package org.ton.controller;

import org.junit.Before;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.ton.scheduler.TaskScheduler;

import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AbstractControllerTest {
    private MockMvc mockMvc;
    protected TaskScheduler scheduler;

    @Before
    public void before() {
        scheduler = mock(TaskScheduler.class);
        mockMvc = standaloneSetup(new SchedulerController(scheduler))
                .alwaysDo(print())
                .build();
    }

    protected void assertGet(String endpoint, ResultMatcher expectedHttpCode, String expected) throws Exception {
        assertRest(get(endpoint), expectedHttpCode, expected);
    }

    protected void assertPost(String endpoint, ResultMatcher expectedHttpCode, String expected) throws Exception {
        assertRest(post(endpoint), expectedHttpCode, expected);
    }

    protected void assertPut(String endpoint, ResultMatcher expectedHttpCode, String expected) throws Exception {
        assertRest(put(endpoint), expectedHttpCode, expected);
    }

    private void assertRest(MockHttpServletRequestBuilder requestBuilder, ResultMatcher expectedHttpCode, String expected) throws Exception {
        String actual = mockMvc.perform(requestBuilder)
                .andExpect(expectedHttpCode)
                .andReturn().getResponse().getContentAsString();
        assertEquals("Response is not identical", expected, actual);
    }
}
