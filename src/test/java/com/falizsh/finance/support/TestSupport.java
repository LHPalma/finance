package com.falizsh.finance.support;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class TestSupport {

    @Mock
    protected ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        init();
    }

    public abstract void init();

    public static <T> T valid(Class<T> type) {
        return Instancio.create(type);
    }

    public static <T> List<T> valid(Class<T> type, Integer size) {
        return Instancio.ofList(type).size(size).create();
    }

    public InOrder inOrder(Object... mocks) {
        return Mockito.inOrder(mocks);
    }

}