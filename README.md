# Teamwork CLI

### Getting Started
1. Install [Babashka](https://github.com/babashka/babashka#installation) - This is a Clojure scripting runtime based on GraalVM. Installation instructions can be found at the link or easily installed with Homebrew
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
	export PATH="<PATH TO THIS DIR>:$PATH" >> ~/.bashrc
	```
    
    **_zsh_**
	```sh
	export PATH="<PATH TO THIS DIR>:$PATH" >> ~/.zshrc
	```
5. **Please Note:** The commands below assume you have added this to PATH, if you have not you will need to invoke the commands by running the main `tw` script in the directory. For Example:
	```sh
	./tw tasks
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
```sh
tw tasks
```


### Help
```sh
tw help
```
