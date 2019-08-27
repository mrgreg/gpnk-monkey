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

### IntelliJ plugins

* Lombok
* CheckStyle-IDEA


### Increase IntelliJ Memory Limits

IntelliJ IDEA's default settings for `-Xms` and `-Xms` are rather small, so it may take a long time to start up and struggle to run our applications.

To increase IDEA's memory settings, go to `Help | Edit Custom VM Options...` and increase the `-Xms` and `-Xms` settings.

The default value of `-XX:ReservedCodeCacheSize=240m` seems fine and does not contribute to the sluggishness of the IDE.