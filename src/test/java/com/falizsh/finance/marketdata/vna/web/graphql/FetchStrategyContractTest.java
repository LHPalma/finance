package com.falizsh.finance.marketdata.vna.web.graphql;

import com.falizsh.finance.infrastructure.config.graphql.GraphQlConfig;
import com.falizsh.finance.marketdata.vna.model.VnaFetchStrategy;
import com.falizsh.finance.marketdata.vna.usecase.FetchVnaDataUseCase;
import com.falizsh.finance.support.TestSupport;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLSchema;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@GraphQlTest(VnaGraphQLAction.class)
@Import(GraphQlConfig.class)
class FetchStrategyContractTest extends TestSupport {

    @Autowired
    private GraphQlSource graphQlSource;

    @MockitoBean
    private FetchVnaDataUseCase fetchVnaDataUseCase;

    private GraphQLSchema schema;

    @Override
    public void init() {
        this.schema = graphQlSource.schema();
    }

    @Test
    void shouldKeepFetchStrategyEnumInSyncWithGraphQLSchema() {
        GraphQLEnumType graphQlEnum = (GraphQLEnumType) schema.getType("FetchStrategy");

        assertThat(graphQlEnum)
                .withFailMessage("O Enum 'FetchStrategy' não existe no arquivo vna.graphqls!")
                .isNotNull();

        List<String> schemaEnumValues = graphQlEnum.getValues().stream()
                .map(GraphQLEnumValueDefinition::getName)
                .toList();

        List<String> javaEnumValues = Arrays.stream(VnaFetchStrategy.values())
                .map(Enum::name)
                .toList();

        assertThat(javaEnumValues)
                .hasSameSizeAs(schemaEnumValues)
                .containsExactlyInAnyOrderElementsOf(schemaEnumValues);
    }
}