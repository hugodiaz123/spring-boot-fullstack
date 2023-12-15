package com.practica.customer;

import com.practica.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void selectAllCustomers() {
        //Given
        String name = FAKER.name().firstName();
        Customer customer = new Customer(name, name + "@gmail.com" + UUID.randomUUID(), FAKER.random().nextInt(17, 100));
        underTest.insertCustomer(customer);
        //When
        List<Customer> actual = underTest.selectAllCustomers();
        //Then
        assertThat(!actual.isEmpty()).isTrue();
    }

    @Test
    void selectCustomerById() {
        //Given
        String name = FAKER.name().firstName();
        String email = name + "@gmail.com" + UUID.randomUUID();
        Customer customer = new Customer(name, email, FAKER.random().nextInt(17, 100));
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(name);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        int id = -1;
        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        //Given
        String name = FAKER.name().firstName();
        Customer customer = new Customer(name, name + "@gmail.com" + UUID.randomUUID(), FAKER.random().nextInt(17, 100));
        underTest.insertCustomer(customer);
        //When
        List<Customer> actual = underTest.selectAllCustomers();
        //Then
        assertThat(!actual.isEmpty()).isTrue();
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        String name = FAKER.name().firstName();
        String email = name + "@gmail.com" + UUID.randomUUID();
        Customer customer = new Customer(name, email, FAKER.random().nextInt(17, 100));
        underTest.insertCustomer(customer);
        //When
        boolean actual = underTest.existsPersonWithEmail(email);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithEmailReturnsFalseWhenDoesNotExist() {
        //Given
        String email = FAKER.name().firstName() + "@gmail.com" + UUID.randomUUID();
        //When
        boolean actual = underTest.existsPersonWithEmail(email);
        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existPersonWithId() {
        //Given
        String name = FAKER.name().firstName();
        String email = name + "@gmail.com" + UUID.randomUUID();
        Customer customer = new Customer(name, email, FAKER.random().nextInt(17, 100));
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //When
        boolean actual = underTest.existPersonWithId(id);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithIdReturnsFalseWhenIdNotPresent() {
        //Given
        Integer id = -1;
        //When
        boolean actual = underTest.existPersonWithId(id);
        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String name = FAKER.name().firstName();
        String email = name + "@gmail.com" + UUID.randomUUID();
        Customer customer = new Customer(name, email, FAKER.random().nextInt(17, 100));
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //When
        underTest.deleteCustomerById(id);
        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        //Given
        String name = FAKER.name().firstName();
        String email = name + "@gmail.com" + UUID.randomUUID();
        Integer age = FAKER.random().nextInt(17, 100);
        Customer customer = new Customer(name, email, age);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        String newName = "foo";
        //When
        customer.setId(id);
        customer.setName(newName);
        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId().equals(id)).isTrue();
            assertThat(c.getName().equals(newName)).isTrue();
            assertThat(c.getEmail().equals(email)).isTrue();
            assertThat(c.getAge().equals(age)).isTrue();
        });
    }

    @Test
    void updateCustomerEmail() {
        //Given
        String name = FAKER.name().firstName();
        String email = name + "@gmail.com" + UUID.randomUUID();
        Integer age = FAKER.random().nextInt(17, 100);
        Customer customer = new Customer(name, email, age);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        String newEmail = name + "@gmail.com" + UUID.randomUUID();
        //When
        customer.setId(id);
        customer.setEmail(newEmail);
        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId().equals(id)).isTrue();
            assertThat(c.getName().equals(name)).isTrue();
            assertThat(c.getEmail().equals(newEmail)).isTrue();
            assertThat(c.getAge().equals(age)).isTrue();
        });
    }

    @Test
    void updateCustomerAge() {
        //Given
        String name = FAKER.name().firstName();
        String email = name + "@gmail.com" + UUID.randomUUID();
        Integer age = FAKER.random().nextInt(17, 100);
        Customer customer = new Customer(name, email, age);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        Integer newAge = FAKER.random().nextInt(17, 100);
        //When
        customer.setId(id);
        customer.setAge(newAge);
        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId().equals(id)).isTrue();
            assertThat(c.getName().equals(name)).isTrue();
            assertThat(c.getEmail().equals(email)).isTrue();
            assertThat(c.getAge().equals(newAge)).isTrue();
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        //Given
        String name = FAKER.name().firstName();
        String email = name + "@gmail.com" + UUID.randomUUID();
        Integer age = FAKER.random().nextInt(17, 100);
        Customer customer = new Customer(name, email, age);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //When
        customer.setId(id);
        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getName().equals(name)).isTrue();
            assertThat(c.getEmail().equals(email)).isTrue();
            assertThat(c.getAge().equals(age)).isTrue();
        });
    }
}