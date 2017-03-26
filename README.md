### Task manager
---
We are using [Asana](https://app.asana.com/0/303125811073727). 
### Git GUI tools
---
  * [GitKraken](https://www.gitkraken.com/)
  * [GitExtensions](https://github.com/gitextensions/gitextensions/releases/latest)
  * [Github Desktop](https://desktop.github.com/)
  
  For merge conflicts, you can use the built-in manager, or [KDiff3](https://sourceforge.net/projects/kdiff3/files/).
  
  
### Get started
---
#### Generate SSH key
  After you download one of the tools mentioned above, you firstly need to generate a local SSH key. This will provide you the rights to download(pull) and upload(push) files on git. 
  ##### Option A
  Some GUI tools have an in-place mechanism for generating an SSH key. For example, in GitKraken you can generate the key from File->Authentication. Check if your downloaded tool have something like this.
  ##### Option B
  It seems like Option A didn't worked for you.
  Now, download a [Git Bash](https://git-scm.com/downloads). After that, follow [the instructions](https://help.github.com/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent/) to set you ssh key.
      
  #### Set ssh key in tool
  If you generated the key with your tool, probably it is already set. Else, put the path to it somewhere in settings. In GitKraken it is placed in the same menu as mentioned at Option A. For other tools, google it.
  
  #### Add key to git
   Now you shoud have 2 files generated:
   * some_name      --> a private file, you are the only one who should possess it on your local PC
   * some_name.pub  --> a public file, that should be placed in other places

  How it works? Shortly, the public key is sent to the PC, where it is processed and then checked if it matches the private key.

  Copy all the content from the public key. Then access tiny_image_top_right->settings->SSH and GPG keys->New SSH key. Enter any title(for example, which computer has that key), then paste the copied key in the next field, and select Add SSH key
  
  #### Get the content from repository
  When you take it for the first time, it is named cloning.
  
  1. In git, select clone or download, and copy the link ( https://github.com/HDT-Tibi/explosiveButter.git ). 
  2. In your tool, select Clone repository. In the field "repository to clone" (or something similar) paste the link, and choose where to download it.
  3. Open the repository in tool (the place where you downloaded the files).
  Now you have the project on your computer.
