# Getting Started

### Git configuration

[Atlassian Tutorial](https://www.atlassian.com/git/tutorials/setting-up-a-repository/git-config)

We want to maintain a linear git history, so we want to enforce allowing fast-forward merges, only.  This is enforced in github.  To similarly enforce ff merges only locally, please run `git config --global merge.ff only`.

If you'd like to set this for a single branch instead of globally, use `--local` instead of `--global`.


(On a new machine) You may also want to set values such as
* `git config --global user.email "your_email@example.com"`
* `git config --global user.name "MyGitName"`
* `git config --global core.editor "vim"`
* Any other shortcuts you like.  Good examples can be found [here](https://medium.com/the-lazy-developer/five-life-changing-git-aliases-e4211c090017)

### Setup SSH Authorization with GitHub

If you are not sure whether or not you have an existing SSH key, [check for existing SSH keys.](https://help.github.com/en/articles/checking-for-existing-ssh-keys)

If there is no existing SSH key, [generate a new SSH key.](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)

[Add the SSH key to your GitHub account.](https://help.github.com/en/articles/adding-a-new-ssh-key-to-your-github-account)

You should now be able to checkout/clone GitHub projects via SSH.

### Setup Maven

Maven artifacts are stored in a google cloud bucket called `gpnk-sandbox-maven-repo`.  For now, repos are configured explicitly in pom files.

TODO: make a working `~/.m2/settings.xml` and put it here.  Remove repo details from pom files

### Setup Google Cloud

Greg will need to grant appropriate permissions in our gcloud account [GPNK-sandbox](https://console.cloud.google.com/home/dashboard?project=gpnk-sandbox&pli=1) and on our [github repo](https://github.com/mrgreg/gpnk-monkey).  Once that's done:

* Install Google Cloud CLI
  * [linux](https://cloud.google.com/sdk/docs/quickstart-linux)
  * [mac](https://cloud.google.com/sdk/docs/quickstart-macos)
* Clone the develop branch of the gpnk-monkey repo
* Run `gcloud auth application-default login`
* Run `mvn clean deploy` to verify it works

### Setup Postgres

To install Postgres locally follow instructions [here](https://linux4one.com/how-to-install-postgresql-on-linux-mint-19/)

Setup the password for the default `postgres` user:
* Login to postgres:
  * `sudo su - postgres`    
  * `psql`
* Change the password for user `postgres`: 
  * `ALTER USER postgres PASSWORD 'postgres'`
  * `\q`
* Edit the following line in `pg_hba.conf` (/etc/postgresql/<postgres-version>/main/pg_hba.conf):
  * `local   all             postgres                                peer`
  * change the `peer` to `md5`
* Restart the Postgres server:
  * `sudo service postgresql restart`
* Now you should be able to login into the Postgres server with `psql -U postgres` and `postgres` as the password.
    
### IntelliJ plugins

* Lombok
* CheckStyle-IDEA


### Increase IntelliJ Memory Limits

IntelliJ IDEA's default settings for `-Xms` and `-Xms` are rather small, so it may take a long time to start up and struggle to run our applications.

To increase IDEA's memory settings, go to `Help | Edit Custom VM Options...` and increase the `-Xms` and `-Xms` settings.

The default value of `-XX:ReservedCodeCacheSize=240m` seems fine and does not contribute to the sluggishness of the IDE.


### Static Analysis Tools

#### CheckStyle

[CheckStyle](https://checkstyle.org/) is a tool that enforces a coding style in Java code. 

- Install CheckStyle in IntelliJ:
  - In IntelliJ, go to ``` File | Settings | Plugins ```.
  - Search for `CheckStyle` and install it.
  - Restart IntelliJ. 

- CheckStyle is executed via `maven-checkstyle-plugin` in Maven's `validate` phase.
  - CheckStyle can be run via `mvn checkstyle:check` command.  
  
Our CheckStyle rules are configured in the `static-analysis/src/main/resources/gpnk-checkstyle.xml` file. 

Our CheckStyle configuration requires that Java imports are arranged in an alphabetical order in the following 
groups:
- Project imports `com.gpnk.*`
- All other imports
- `java.*` packages
- `javax.*` packages

In order to make IntelliJ Import Optimization (`Ctrl + Alt + O` shortcut) comply with the above configure 
`Settings | Editor | Code Style | Java`, `Import` tab as follows:
- `General` - check `Use single class import`
- `Class count to use import with *:` - set to the max allowed value of 99 (to effectively disable to use of `*` 
imports on import optimizations)
- `Names count to use static import with *:` - set to 99 (analogous to the above)
-  `Import Layout`: configure as follows
    - blank line
    - import `com.gpnk.*`; make sure that `With Subpackages` is checked
    - blank line
    - import all other imports
    - blank line
    - import `java.*`; make sure that `With Subpackages` is checked
    - blank line
    - import `javax.*`; make sure that `With Subpackages` is checked
    - blank line
    - import static all other imports

[Here is a screenshot of my `Import` configuration tab in IntelliJ](./intellij-settings-java-import.png).  
  
#### SpotBugs vs PMD? Do we need both?

SpotBugs and PMD are considered complementary tools. While there is a small overlap in the set of problems the two tools 
detect (for example, both tools complain about the use of `System.ext()`), they largely find different problems via 
different types of static analysis. 

PMD analyzes Java source code for problems like the following, to name a few examples:
- abstract class does not contain any abstract methods
- constructors and methods receiving arrays should clone objects and store the copy
- avoid creating deeply nested if-then statements since they are harder to read and error-prone to maintain 
- a closeable resource is not closed in the code

SpotBugs analyzes Java byte code for bad patterns, like the following:
- equals() method fails on subtypes 
- clone method may return null
- null value is guaranteed to be dereferenced
- invalid type cast
 
In conclusion, we are using both of these tools to ensure code quality in this project. 
 
#### SpotBugs, Successor for FindBugs

SpotBugs is a successor for FindBugs which works with Java versions 8 and 11. 
See [SpotBugs' website](https://spotbugs.readthedocs.io/en/latest/introduction.html) 
for more information about it.

In particular [this page](https://spotbugs.readthedocs.io/en/latest/bugDescriptions.html) lists
 the standard bug patterns reported by SpotBugs.
  
- SpotBugs is executed via `spotbugs-maven-plugin` in Maven's `test-compile` phase.
  - SpotBugs can be run via `mvn spotbugs:check` command.
  - Running `mvn spotbugs:gui` launches a graphical interface for examining SpotBugs errors. 
 
Our SpotBug exclude filters (both global and class-specific) are specified in the 
`static-analysis/src/main/resources/spotbugs-exclude.xml` file. 
 
#### PMD

[PMD's website](https://pmd.github.io/latest/index.html) is a useful resource for information about this tool. 
In particular, [this page](https://pmd.github.io/latest/pmd_rules_java.html) lists PMD's rules for Java.

- PMD is executed via `pmd-maven-plugin` in Maven's `test-compile` phase.
    - PMD can be run via `mvn pmd:check` command.
    
PMD's rules are configured in the `static-analysis/src/main/resources/pmd-rulesets.xml` file.    