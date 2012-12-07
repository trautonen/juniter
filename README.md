JUniter
=======

Framework that extends JUnit functionality

Test life cycle
---------------

Test life cycle `org.eluder.juniter.core.TestLifeCycle` provides the hooks to
different phases of a unit test. The main reason for custom interface over the
JUnit `org.junit.rules.TestRule` interface is that the test rules do not allow
access to the test instance.

Comparison between the TestLifeCycle and TestRule interfaces:

<table>
  <th>
    <td></td>
    <td>TestLifeCycle</td>
    <td>TestRule</td>
  </th>
  <tr>
    <td>Easy access to test instance</td>
    <td>yes</td>
    <td>no</td>
  </tr>
  <tr>
    <td>Easy access to test class</td>
    <td>yes</td>
    <td>yes</td>
  </tr>
  <tr>
    <td>Easy access to test method</td>
    <td>yes</td>
    <td>no</td>
  </tr>
  <tr>
    <td>Must reset its own state after each test</td>
    <td>no</td>
    <td>yes</td>
  </tr>
  <tr>
    <td>Requires a custom JUnit runner</td>
    <td>yes</td>
    <td>no</td>
  </tr>
</table>


Usage
-----

Core module requires the following dependency:

```xml
<dependency>
    <groupId>org.eluder.juniter</groupId>
    <artifactId>juniter-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Using test life cycles requires defining the juniter test runner and defining
the life cycles to use:

```java
@RunWith(TestRunner.class)
@TestLifeCycles({ FirstLifeCycle.class, SecondLifeCycle.class })
public class MyTest {
    ...
}
```

Automatic mocking can be achieved with the shorthand mock runner or defining
the mock life cycle as one test life cycle:

```java
@RunWith(MockTestRunner.class)
public class MockTest {
    ...
}
```

is equivalent to

```java
@RunWith(TestRunner.class)
@TestLifeCycles({ MockLifeCycle.class })
public class MockTest {
    ...
}
```
Guice integration requires the following dependency:

```xml
<dependency>
    <groupId>org.eluder.juniter</groupId>
    <artifactId>juniter-guice</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

License
-------

The project juniter is licensed under the MIT license.