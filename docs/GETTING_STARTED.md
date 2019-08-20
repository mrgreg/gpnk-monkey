# Getting Started

### Setup SSH Authorization with GitHub

If you are not sure whether or not you have an existing SSH key, [check for existing SSH keys.](https://help.github.com/en/articles/checking-for-existing-ssh-keys)

If there is no existing SSH key, [generate a new SSH key.](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)

[Add the SSH key to your GitHub account.](https://help.github.com/en/articles/adding-a-new-ssh-key-to-your-github-account)

You should now be able to checkout/clone GitHub projects via SSH.

### Increase IntelliJ Memory Limits

IntelliJ IDEA's default settings for `-Xms` and `-Xms` are rather small, so it may take a long time to start up and struggle to run our applications.

To increase IDEA's memory settings, go to `Help | Edit Custom VM Options...` and increase the `-Xms` and `-Xms` settings.

The default value of `-XX:ReservedCodeCacheSize=240m` seems fine and does not contribute to the sluggishness of the IDE.