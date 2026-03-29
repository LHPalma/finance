package com.falizsh.finance.infrastructure.config.graphql;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Configuration
public class GraphQlConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.GraphQLLong)
                .scalar(localDateTimeScalar());
    }

    @Bean
    public GraphQLScalarType localDateTimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("Java 8 LocalDateTime as GraphQL scalar (ISO 8601)")
                .coercing(new Coercing<LocalDateTime, String>() {

                    @Override
                    public String serialize(Object dataFetcherResult, GraphQLContext graphQLContext,
                                            java.util.Locale locale) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof LocalDateTime localDateTime) {
                            return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        }
                        throw new CoercingSerializeException(
                                "Expected a LocalDateTime object but got: " + dataFetcherResult.getClass().getName()
                        );
                    }

                    @Override
                    public LocalDateTime parseValue(Object input, GraphQLContext graphQLContext,
                                                    java.util.Locale locale) throws CoercingParseValueException {
                        if (input instanceof String stringInput) {
                            try {
                                return LocalDateTime.parse(stringInput, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseValueException(
                                        "Invalid DateTime format: " + stringInput + ". Expected ISO-8601 format.", e
                                );
                            }
                        }
                        throw new CoercingParseValueException("Expected a String but got: " + input.getClass().getName());
                    }

                    @Override
                    public LocalDateTime parseLiteral(graphql.language.Value<?> input, CoercedVariables variables,
                                                      GraphQLContext graphQLContext, java.util.Locale locale) throws CoercingParseLiteralException {
                        if (input instanceof StringValue stringValue) {
                            try {
                                return LocalDateTime.parse(stringValue.getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseLiteralException(
                                        "Invalid DateTime literal: " + stringValue.getValue(), e
                                );
                            }
                        }
                        throw new CoercingParseLiteralException(
                                "Expected StringValue but got: " + input.getClass().getSimpleName()
                        );
                    }
                }).build();
    }
}