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
  
#### SpotBugs, Successor for FindBugs

SpotBugs is a successor for FindBugs which works with Java versions 8 and 11. 
See [SpotBugs' website](https://spotbugs.readthedocs.io/en/latest/introduction.html) 
for more information about it.
  
- SpotBugs is executed via `spotbugs-maven-plugin` in Maven's `test-compile` phase.
  - SpotBugs can be run via `mvn spotbugs:check` command.
  - Running `mvn spotbugs:gui` launches a graphical interface for examining SpotBugs errors. 