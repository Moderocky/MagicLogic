MagicLogic
=====
A collections and data management library.

MagicLogic is designed to provide functional and usable methods for lists and other collection types, as well as various other logic and procedure utilities.
It was originally hosted on GitLab under my private collection, but for the ML v4 rewrite I have decided to make parts of it publicly available.

The main component is `MagicList`. This is an extension of the JDK ArrayList class, but with some actual functional methods to allow for simpler transformations, such as conversions, recollections, chain methods, etc.
As it extends from a JDK class, it gains the speed 'cheat' advantage that JDK classes have, and so would perform better than any list type from a third-party library.

Also contained in this are some array transformers, backers and handlers for array-based collections.

This has a soft-dependency of [Overlord](https://github.com/Moderocky/Overlord) for unsafe modifications.

### Maven Information
```xml
<repository>
    <id>pan-repo</id>
    <name>Pandaemonium Repository</name>
    <url>https://gitlab.com/api/v4/projects/18568066/packages/maven</url>
</repository>
``` 

```xml
<dependency>
    <groupId>mx.kenzie</groupId>
    <artifactId>magiclogic</artifactId>
    <version>4.0.0</version>
    <scope>compile</scope>
</dependency>
```