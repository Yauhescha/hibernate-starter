package com.yauhescha.hibernate;

import com.yauhescha.hibernate.entity.User;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HibernateRunnerTest {

    @Test
    public void checkReflectionAli() throws SQLException, IllegalAccessException {

        User user = User.builder()
                .username("ivan@gmail1.com")
                .firstname("Ivan")
                .lastname("Push")
                .birthDate(LocalDate.of(2000, 1, 30))
                .age(20)
                .build();
        final String sql = "insert\n"
                + "into\n"
                + "%s\n"
                + "(%s)\n"
                + "values\n"
                + "(%s)";

        final String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        final Field[] declaredFields = user.getClass().getDeclaredFields();
        final String columnName = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(Collectors.joining(", "));
        final String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        System.out.println(String.format(sql, tableName, columnName, columnValues));


        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(String.format(sql, tableName, columnName, columnValues));
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            preparedStatement.setObject(1, declaredField.get(user));
        }

    }

}