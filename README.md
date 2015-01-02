# Locela - API for Java

[![Build status](https://api.travis-ci.org/echocat/locela-api-java.png)](https://travis-ci.org/echocat/locela-api-java)

Java API of a localization framework for an easy and platform independent work.

## Download

See [Central Repository](http://search.maven.org/#search|ga|1|g:org.echocat.locela.api%20AND%20a:java) for available downloads.

If you use caching optimize resources also download [org.echocat.jomon.cache](http://search.maven.org/#search|ga|1|g:org.echocat.jomon%20AND%20a:cache).

## Dependency Management

See [Central Repository](http://search.maven.org/#search|ga|1|g:org.echocat.locela.api%20AND%20a:java) for available versions.

For avaible versions of org.echocat.jomon.cache also see [Central Repository](http://search.maven.org/#search|ga|1|g:org.echocat.jomon%20AND%20a:cache).

### Apache Maven

```xml 
<dependency>
    <groupId>org.echocat.locela.api</groupId>
    <artifactId>java</artifactId>
    <version>${versions.org.echocat.locela.api.java}</version>
</dependency>
```
If you use caching optimize resources also include:
```xml 
<dependency>
    <groupId>org.echocat.jomon</groupId>
    <artifactId>cache</artifactId>
    <version>${versions.org.echocat.jomon}</version>
</dependency>
```

### Apache Ivy

```xml
<dependency org="org.echocat.locela.api" name="java" rev="${versions.org.echocat.locela.api.java}" />
```

If you use caching optimize resources also include:
```xml 
<dependency org="org.echocat.jomon" name="cache" rev="${versions.org.echocat.jomon}" />
```

### Grandle/Grails

```txt
compile 'org.echocat.locela.api:java:${versions.org.echocat.locela.api.java}'
```

If you use caching optimize resources also include:
```txt 
compile 'org.echocat.jomon:cache:${versions.org.echocat.jomon}'
```
# License

echocat Locela Java API is licensed under [MPL 2.0](http://mozilla.org/MPL/2.0/).

# Want to help?

You are welcome. [Fork](https://github.com/echocat/locela-api-java/fork) or [contact](mailto:contact@echocat.org) us.

# Thanks

Many thanks to the companies which helps to develop this great project.

* [Lautsprecher Teufel GmbH](https://teufelaudio.com) which uses this project for their [Raumfeld product series](https://raumfeld.com).
