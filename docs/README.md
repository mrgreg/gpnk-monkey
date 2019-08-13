# Documentation

### Getting Started

[A few useful tips on how to get the tools setup.](GETTING_STARTED.md) 

### Static Analysis Tools

#### CheckStyle

- Install CheckStyle in IntelliJ:
  - In IntelliJ, go to ``` File | Settings | Plugins ```.
  - Search for `CheckStyle` and install it.
  - Restart IntelliJ. 

- CheckStyle is executed via `maven-checkstyle-plugin` in Maven's `validate` phase.
  - CheckStyle can be run via `mvn checkstyle:check` command.  