# Locela - API for Java

Java API of a localization framework for an easy and platform independent work.

## Topics

* [Getting started](#getting-started)
* [Contributing](#contributing)
* [License](#license)

## Getting started

### Dependency

#### 1. Register our repository (optional)

You can directly register our repository if you want always the latest version. The central can be versions behind.

##### Maven

```xml
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo.maven.apache.org/maven2</url>
    </repository>
    <repository>
        <id>echocat</id>
        <url>https://packages.echocat.org/maven</url>
    </repository>
</repositories>
```

##### Gradle

```groovy
repositories {
    mavenCentral()
    maven {
        url "https://packages.echocat.org/maven"
    }
}
```

#### 2. Pick your version


[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.echocat.locela.api/java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.echocat.locela.api/java)

Find your desired version you want to install (usually the latest one) [by looking it up in our repository](https://github.com/echocat/locela-api-java/packages/1846697/) or directly at [the Maven Central](http://search.maven.org/#search|ga|1|g:org.echocat.locela.api%20AND%20a:java).

#### 3. Add the dependency

##### Maven

```xml 
<dependency>
    <groupId>org.echocat.locela.api</groupId>
    <artifactId>java</artifactId>
    <version><!-- THE VERSION --></version>
</dependency>
```

##### Gradle

```groovy
compile 'org.echocat.locela.api:java:<THE VERSION>'
```

## Contributing

**Locela - API for Java** is an open source project by [echocat](https://echocat.org). So if you want to make this project even better, you can contribute to this project on [GitHub](https://github.com/echocat/locela-api-java) by [fork us](https://github.com/echocat/locela-api-java/fork).

If you commit code to this project, you have to accept that this code will be released under the [license](#license) of this project.

## License

See the [LICENSE](LICENSE) file.
