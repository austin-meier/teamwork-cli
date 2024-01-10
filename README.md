# Teamwork CLI

### Getting Started
1. Install [Babashka](https://github.com/babashka/babashka#installation) - This is a native Clojure interpreter scripting runtime based on Java's GraalVM. Installation instructions can be found at the link or easily installed with Homebrew
 	```sh
  	brew install borkdude/brew/babashka
  	```
2. Clone this repository 
  	```sh
  	git clone https://github.com/austin-meier/teamwork-cli 
  	```
3. Edit your [Configuration File](#configuration-file) to contain your TeamWork settings.

4. _Optional_: Add this folder to your path so you can run it anywhere

	**_bash_**
	```sh
	echo 'export PATH="<SCRIPT DIRECTORY>:$PATH"' >> ~/.bashrc
    source ~/.bashrc
	```
    
    **_zsh_**
	```sh
	echo 'export PATH="<SCRIPT DIRECTORY>:$PATH"' >> ~/.zshrc
    source ~/.zshrc
	```
5. **Please Note:** The commands below assume you have added this to PATH, if you have not you will need to invoke the commands by running the main `tw` script in the directory. For Example:
	```sh
	./tw index
	```


### Configuration File
This creates a configuration and local index for maintaining and assigning the more memorable ticket numbers to the teamwork tasks. This file is located at `~/.config/teamwork-cli/data.edn`

**_You will need two things_**
1. Your TeamWork base URL - You can find this by logging in and copying the base URL, it should follow this format. `https://<your-team>.teamwork.com/`
2. Your TeamWork API key
``` sh
In Teamwork.com, click your avatar in the bottom left.
1) Click "Edit my details".
2) Go to "API & Mobile".
3) Click on the "Show your token".
4) Copy the "Key".
```

### Tasks

#### Index all tasks
```sh
tw index
```
#### Search for tasks
This will return a nice printout with the local index numbers for the tasks that are easy to remember. Use these number to feed in to further commands
```sh
tw search <query>
```

#### Move a task between boards 
```sh
tw move <task-id> <board-name>
```
Valid boards are:
```
open
progress
review
ready
staging
completed
```

#### Pretty print the data of a task (for now)
```sh
tw task <local
```


### Help
```sh
tw help
```
