package br.com.delogic.asd.content;

import org.junit.Assert;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class BaseTest extends Assert {

    @Before
    public final void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

}
